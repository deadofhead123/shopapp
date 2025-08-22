import { Component, ViewChild } from '@angular/core';
import { HeaderComponent } from '../header/header';
import { FooterComponent } from '../footer/footer';
import { FormsModule, NgForm } from '@angular/forms';
import { UserService } from '../service/user/userService.service';
import { ResponseDTO } from '../dtos/response/response.dto';

@Component({
  selector: 'app-login',
  imports: [
    HeaderComponent,
    FooterComponent,
    FormsModule
  ],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class LoginComponent {
  @ViewChild('loginForm') loginForm !: NgForm;

  phoneNumber: string;
  password: string;

  constructor(private userService: UserService) {
    this.phoneNumber = '';
    this.password = '';
  }

  login() {
    const loginDTO = {
      phone_number: this.phoneNumber,
      password: this.password
    };
    this.userService.login(loginDTO).subscribe({
      next: (response: any) => {
        alert(`Login successfully! Token: ` + response.data); // Give token
      },
      complete: () => {

      },
      error: (response: any) => {
        alert(response.error.message);
      }
    });
  }
}
