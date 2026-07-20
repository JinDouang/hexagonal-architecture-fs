import { InjectionToken } from '@angular/core';
import { Observable } from 'rxjs';
import { Product, ProductPayload } from '../../domain/product.model';

export interface ProductApiPort {
  list(): Observable<Product[]>;
  findById(id: string): Observable<Product>;
  create(payload: ProductPayload): Observable<Product>;
  update(id: string, payload: ProductPayload): Observable<Product>;
  remove(id: string): Observable<void>;
  activate(id: string): Observable<Product>;
  deactivate(id: string): Observable<Product>;
}

export const PRODUCT_API_PORT = new InjectionToken<ProductApiPort>('PRODUCT_API_PORT');