import { Component } from '@angular/core';
import { HeaderComponent } from '../header/header';
import { FooterComponent } from '../footer/footer';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-register',
  imports: [
    HeaderComponent,
    FooterComponent,
    FormsModule
  ],
  templateUrl: './register.html',
  styleUrl: './register.scss'
})
export class RegisterComponent {
  phone: string;
  password: string;
  retypePassword: string;
  fullName: string;
  address: string;
  dateOfBirth: Date;
  isAccepted: boolean;

  constructor() {
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
    alert(`Register successfully!
            phone: ${this.phone},
            password: ${this.password},
            retypePassword: ${this.retypePassword},
            full name: ${this.fullName},
            address: ${this.address},
            date of birth: ${this.dateOfBirth},
            isAccepted: ${this.isAccepted}
      `);
  }
}
