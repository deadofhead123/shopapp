import { Component, OnInit, ViewChild } from '@angular/core';
import { HeaderComponent } from '../header/header';
import { FooterComponent } from '../footer/footer';
import { FormsModule, NgForm } from '@angular/forms';
import { UserService } from '../../service/user.service';
import { ResponseDTO } from '../../dtos/response/response.dto';
import { TokenService } from '../../service/token.service';
import { RoleService } from '../../service/role.service';
import { JsonPipe } from '@angular/common';

@Component({
  selector: 'app-login',
  imports: [
    HeaderComponent,
    FooterComponent,
    FormsModule,
    JsonPipe
  ],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class LoginComponent implements OnInit {
  @ViewChild('loginForm') loginForm !: NgForm;

  phoneNumber: string;
  password: string;
  roles: Role[] = [];
  selectedRole: Role | undefined;
  rememberMe: boolean;

  constructor(
    private userService: UserService,
    private tokenService: TokenService,
    private roleService: RoleService,
  ) {
    this.phoneNumber = '';
    this.password = '';
    this.rememberMe = false;
  }

  ngOnInit(): void {
    this.roleService.getRoles().subscribe({
      next: (roles: Role[]) => {
        this.roles = roles;
        this.selectedRole = roles.length > 0 ? roles[0] : undefined;
      },
      complete: () => {

      },
      error: (error: any) => {
        alert(error?.error.message);
      }
    })
  }

  login() {
    const loginDTO = {
      phone_number: this.phoneNumber,
      password: this.password,
      role_id: this.selectedRole?.id ?? 1,
    };

    this.userService.login(loginDTO).subscribe({
      next: (response: LoginResponse) => {
        const { token } = response; // Destructuring
        alert(`${response.message}. Token: ` + token); // Give token
        if (this.rememberMe) {
          this.tokenService.setToken(token);
        }
      },
      complete: () => {

      },
      error: (error: any) => {
        alert(error?.error?.message);
        debugger
      }
    });
  }
}
