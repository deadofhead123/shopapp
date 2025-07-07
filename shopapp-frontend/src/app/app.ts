import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HomeComponent } from './home/home';
import { OrderComponent } from './order/order';
import { OrderConfirmComponent } from './order-confirm/order-confirm';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    HomeComponent,
    OrderComponent,
    OrderConfirmComponent,
  ],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected title = 'shopapp-frontend';
}
