import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HomeComponent } from './components/home/home';
import { OrderComponent } from './components/order/order';
import { OrderConfirmComponent } from './components/order-confirm/order-confirm';
import { LoginComponent } from './components/login/login';
import { RegisterComponent } from './components/register/register';
import { DetailProductComponent } from './components/detail-product/detail-product';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    HomeComponent,
    // OrderComponent,
    // OrderConfirmComponent,
    // LoginComponent,
    // RegisterComponent,
    // DetailProductComponent,
    // FormsModule
  ],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected title = 'shopapp-frontend';
}
