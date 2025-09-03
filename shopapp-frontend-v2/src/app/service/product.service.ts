import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HttpUtilService } from './http.util.service';
import { Product } from '../models/product';

@Injectable({
    providedIn: 'root'
})
export class ProductService {
    private productAPI = `${environment.apiBaseUrl}/products`;

    constructor(private http: HttpClient, private httpUtilService: HttpUtilService) {

    }

    getProducts(keyword: string, categoryId: number, page: number, size: number): Observable<Product[]> {
        const params = new HttpParams()
            .set('page', page)
            .set('limit', size)
            .set('keyword', keyword)
            .set('cateogoryId', categoryId);
        return this.http.get<Product[]>(this.productAPI, { params });
    }

    getProductById(productId: number): Observable<Product> {
        return this.http.get<Product>(`${this.productAPI}/${productId}`);
    }

    getProductsByIds(productIds: number[]): Observable<Product[]> {
        const params = new HttpParams().set('ids', productIds.toString());
        return this.http.get<Product[]>(`${this.productAPI}/orderedList`, { params });
    }
}
