import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../../../environments/environment';
import { Product, ProductPayload } from '../../domain/product.model';
import { ProductApiPort } from '../../application/ports/product-api.port';

@Injectable()
export class ProductHttpAdapter implements ProductApiPort {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiBaseUrl}/products`;

  list(): Observable<Product[]> {
    return this.http.get<Product[]>(this.url);
  }

  findById(id: string): Observable<Product> {
    return this.http.get<Product>(`${this.url}/${id}`);
  }

  create(payload: ProductPayload): Observable<Product> {
    return this.http.post<Product>(this.url, payload);
  }

  update(id: string, payload: ProductPayload): Observable<Product> {
    return this.http.put<Product>(`${this.url}/${id}`, payload);
  }

  remove(id: string): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }

  activate(id: string): Observable<Product> {
    return this.http.patch<Product>(`${this.url}/${id}/activate`, {});
  }

  deactivate(id: string): Observable<Product> {
    return this.http.patch<Product>(`${this.url}/${id}/deactivate`, {});
  }
}