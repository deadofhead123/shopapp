import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  register(registerData: any): Observable<any> {
    const url = "http://localhost:8088/api/v1/users/register";
    const headers = new HttpHeaders({  // Có thể bỏ cái header này lúc gửi không?
      'Content-Type': 'application/json',
    });

    return this.http.post(url, registerData, { headers },);
  }
}
