import { Component } from '@angular/core';
import { HeaderComponent } from '../header/header';
import { FooterComponent } from '../footer/footer';

@Component({
  selector: 'app-login',
  imports: [
    HeaderComponent,
    FooterComponent,
  ],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class LoginComponent {

}
