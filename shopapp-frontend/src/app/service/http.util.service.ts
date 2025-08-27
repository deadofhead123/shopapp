import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class HttpUtilService {
    createHeader(): HttpHeaders {
        return new HttpHeaders({
            'Content-Type': 'application/json',
            'Accept-Language': 'vi'
        });
    }
}
