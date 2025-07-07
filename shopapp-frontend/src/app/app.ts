import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HomeComponent } from './home/home';
import { OrderComponent } from './order/order';
import { OrderConfirmComponent } from './order-confirm/order-confirm';
import { LoginComponent } from './login/login';
import { RegisterComponent } from './register/register';
import { DetailProductComponent } from './detail-product/detail-product';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    HomeComponent,
    OrderComponent,
    OrderConfirmComponent,
    LoginComponent,
    RegisterComponent,
    DetailProductComponent,
  ],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected title = 'shopapp-frontend';
}
