import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HomeComponent } from './home/home';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    HomeComponent,
  ],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected title = 'shopapp-frontend';
}
