import { Component, ViewChild } from '@angular/core';
import { HeaderComponent } from '../header/header';
import { FooterComponent } from '../footer/footer';
import { FormsModule, NgForm } from '@angular/forms';
import { NgFor } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-register',
  imports: [
    FooterComponent,
    FormsModule,
  ],
  templateUrl: './register.html',
  styleUrl: './register.scss'
})
export class RegisterComponent {
  @ViewChild('registerForm') registerForm !: NgForm; // ! có nghĩa là chắc chắn registerForm tồn tại (ta đang dùng nó bên HTML)

  phone: string;
  password: string;
  retypePassword: string;
  fullName: string;
  address: string;
  dateOfBirth: Date;
  isAccepted: boolean;

  constructor(private http: HttpClient, private router: Router) {
    this.phone = '';
    this.password = '';
    this.retypePassword = '';
    this.fullName = '';
    this.address = '';
    this.dateOfBirth = new Date();
    this.dateOfBirth.setFullYear(this.dateOfBirth.getFullYear() - 18);
    this.isAccepted = true;

  }

  onPhoneChange() {
    console.log(this.phone);
  }

  register() {
    // alert(`Register successfully!
    //         phone: ${this.phone},
    //         password: ${this.password},
    //         retypePassword: ${this.retypePassword},
    //         full name: ${this.fullName},
    //         address: ${this.address},
    //         date of birth: ${this.dateOfBirth},
    //         isAccepted: ${this.isAccepted}
    //   `);

    const registerInfo = {
      "fullname": this.fullName,
      "phone_number": this.phone,
      "password": this.password,
      "retypePassword": this.retypePassword,
      "fullName": this.fullName,
      "address": this.address,
      "date_of_birth": this.dateOfBirth,
      "facebook_account_id": 0,
      "google_account_id": 0,
      "role_id": 2
    }
    const url = "http://localhost:8088/api/v1/users/register";
    const headers = new HttpHeaders({  // Có thể bỏ cái header này lúc gửi không?
      'Content-Type': 'application/json',
    });

    this.http.post(url, registerInfo, { headers },)
      .subscribe({
        next: (response: any) => {
          alert(`Register successfully\n`);
        },
        complete: () => {

        },
        error: (response: any) => {
          alert(response.error.message);
        }
      })
  }

  checkPasswordMatch() {
    if (this.password !== this.retypePassword) {
      this.registerForm.form.controls['retypePassword'].setErrors({ 'passwordMismatch': true });
    }
    else {
      this.registerForm.form.controls['retypePassword'].setErrors(null);
    }
  }

  checkAge() {
    if (this.dateOfBirth) {
      let dob = new Date(this.dateOfBirth);
      let now = new Date();
      let age = now.getFullYear() - dob.getFullYear();
      const monthDiff = now.getMonth() - dob.getMonth();
      if (monthDiff < 0 || (monthDiff == 0 && now.getDate() - dob.getDate() < 0)) {
        age--;
      }

      if (age < 18) {
        this.registerForm.controls['dateOfBirth'].setErrors({ 'invalidAge': true });
      }
      else {
        this.registerForm.controls['dateOfBirth'].setErrors(null);
      }
    }
  }
}
