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
    private apiGetProducts = `${environment.apiBaseUrl}/products`;

    constructor(private http: HttpClient, private httpUtilService: HttpUtilService) {

    }

    getProducts(page: number, size: number): Observable<Product[]> {
        const params = new HttpParams()
            .set('page', page)
            .set('limit', size);
        return this.http.get<Product[]>(this.apiGetProducts, { params });
    }
}
