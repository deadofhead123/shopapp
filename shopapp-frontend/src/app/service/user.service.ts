import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RegisterDTO } from '../dtos/user/register.dto';
import { environment } from '../environments/environment';
import { LoginDTO } from '../dtos/user/login.dto';
import { HttpUtilService } from './http.util.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiRegister: string = `${environment.apiBaseUrl}/users/register`;
  private apiLogin: string = `${environment.apiBaseUrl}/users/login`;
  private apiConfig;

  constructor(private http: HttpClient, private httpUtilService: HttpUtilService) {
    this.apiConfig = {
      headers: httpUtilService.createHeader(),
    }
  }

  register(registerDTO: RegisterDTO): Observable<any> {
    return this.http.post(this.apiRegister, registerDTO, this.apiConfig,);
  }

  login(loginDTO: LoginDTO): Observable<any> {
    return this.http.post(this.apiLogin, loginDTO, this.apiConfig,);
  }
}
