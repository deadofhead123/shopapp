import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HttpUtilService } from './http.util.service';

@Injectable({
    providedIn: 'root'
})
export class RoleService {
    private apiGetRoles = `${environment.apiBaseUrl}/roles`;
    private apiConfig;

    constructor(private http: HttpClient, private httpUtilService: HttpUtilService) {
        this.apiConfig = {
            headers: httpUtilService.createHeader(),
        }
    }

    getRoles(): Observable<any> {
        return this.http.get(this.apiGetRoles, this.apiConfig);
    }
}
