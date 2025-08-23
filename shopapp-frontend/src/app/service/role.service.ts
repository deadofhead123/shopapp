import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class RoleService {
    private apiGetRoles = `${environment.apiBaseUrl}/roles`;
    private apiConfig = {
        headers: this.createHeader(),
    }

    constructor(private http: HttpClient) { }

    createHeader(): HttpHeaders {
        return new HttpHeaders({
            'Content-Type': 'application/json',
        });
    }

    getRoles(): Observable<any> {
        return this.http.get(this.apiGetRoles, this.apiConfig);
    }
}
