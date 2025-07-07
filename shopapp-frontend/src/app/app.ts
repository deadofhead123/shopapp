import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HomeComponent } from './home/home';
import { OrderComponent } from './order/order';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    HomeComponent,
    OrderComponent
  ],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected title = 'shopapp-frontend';
}
