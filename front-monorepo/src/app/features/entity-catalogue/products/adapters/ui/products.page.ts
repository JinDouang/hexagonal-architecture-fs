import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProductFacade } from '../../application/product.facade';
import { Product } from '../../domain/product.model';
import { KeycloakAuthService } from '../../../../../shared/auth/adapters/oidc/keycloak-auth.service';

@Component({
  selector: 'app-products-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './products.page.html',
  styleUrls: ['./products.page.css']
})
export class ProductsPage implements OnInit {
  readonly facade = inject(ProductFacade);
  readonly auth = inject(KeycloakAuthService);

  private readonly formBuilder = inject(FormBuilder);
  readonly form = this.formBuilder.nonNullable.group({
    name: ['', [Validators.required, Validators.maxLength(120)]],
    description: ['', [Validators.maxLength(500)]],
    price: [0, [Validators.required, Validators.min(0)]],
    moodleSyncEnabled: [false]
  });

  async ngOnInit(): Promise<void> {
    try {
      await this.auth.handleRedirectCallback();
    } catch (error) {
      console.error(error);
    }
    await this.facade.loadProducts();
  }

  async login(): Promise<void> {
    await this.auth.login();
  }

  logout(): void {
    this.auth.logout();
  }

  async submit(): Promise<void> {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload = this.form.getRawValue();
    const selected = this.facade.selectedProduct();
    if (selected) {
      await this.facade.updateProduct(selected.id, payload);
    } else {
      await this.facade.createProduct(payload);
    }
    this.resetForm();
  }

  edit(product: Product): void {
    this.facade.selectProduct(product);
    this.form.setValue({
      name: product.name,
      description: product.description,
      price: Number(product.price),
      moodleSyncEnabled: product.moodleSyncEnabled
    });
  }

  resetForm(): void {
    this.facade.clearSelection();
    this.form.reset({
      name: '',
      description: '',
      price: 0,
      moodleSyncEnabled: false
    });
  }
}