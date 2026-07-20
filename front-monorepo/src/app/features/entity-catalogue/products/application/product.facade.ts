import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { Observable, firstValueFrom } from 'rxjs';
import { Product, ProductPayload } from '../domain/product.model';
import { PRODUCT_API_PORT } from './ports/product-api.port';

@Injectable({ providedIn: 'root' })
export class ProductFacade {
  private readonly productApi = inject(PRODUCT_API_PORT);

  readonly products = signal<Product[]>([]);
  readonly selectedProduct = signal<Product | null>(null);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly activeCount = computed(() => this.products().filter((product) => product.active).length);

  async loadProducts(): Promise<void> {
    await this.run(async () => {
      this.products.set(await firstValueFrom(this.productApi.list()));
    });
  }

  async createProduct(payload: ProductPayload): Promise<void> {
    await this.run(async () => {
      const created = await firstValueFrom(this.productApi.create(payload));
      this.products.update((products) => this.sortByName([...products, created]));
    });
  }

  async updateProduct(id: string, payload: ProductPayload): Promise<void> {
    await this.run(async () => {
      const updated = await firstValueFrom(this.productApi.update(id, payload));
      this.products.update((products) => this.sortByName(products.map((product) => product.id === id ? updated : product)));
      this.selectedProduct.set(null);
    });
  }

  async removeProduct(id: string): Promise<void> {
    await this.run(async () => {
      await firstValueFrom(this.productApi.remove(id));
      this.products.update((products) => products.filter((product) => product.id !== id));
      if (this.selectedProduct()?.id === id) {
        this.selectedProduct.set(null);
      }
    });
  }

  async activateProduct(id: string): Promise<void> {
    await this.patchProduct(id, () => this.productApi.activate(id));
  }

  async deactivateProduct(id: string): Promise<void> {
    await this.patchProduct(id, () => this.productApi.deactivate(id));
  }

  selectProduct(product: Product): void {
    this.selectedProduct.set(product);
  }

  clearSelection(): void {
    this.selectedProduct.set(null);
  }

  private async patchProduct(id: string, request: () => Observable<Product>): Promise<void> {
    await this.run(async () => {
      const updated = await firstValueFrom(request());
      this.products.update((products) => products.map((product) => product.id === id ? updated : product));
    });
  }

  private async run(action: () => Promise<void>): Promise<void> {
    this.loading.set(true);
    this.error.set(null);
    try {
      await action();
    } catch (error) {
      this.error.set(this.extractMessage(error));
    } finally {
      this.loading.set(false);
    }
  }

  private sortByName(products: Product[]): Product[] {
    return [...products].sort((left, right) => left.name.localeCompare(right.name));
  }

  private extractMessage(error: unknown): string {
    if (error instanceof HttpErrorResponse) {
      return error.error?.message ?? error.message;
    }
    if (error instanceof Error) {
      return error.message;
    }
    return 'Unexpected error';
  }
}