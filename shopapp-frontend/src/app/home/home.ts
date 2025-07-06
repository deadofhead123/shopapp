import { Component } from '@angular/core';
import { CurrencyPipe } from '../shared/pipes/CurrencyPipe.pipe';

@Component({
  selector: 'app-home',
  imports: [
    CurrencyPipe
  ],
  templateUrl: './home.html',
  styleUrl: './home.scss'
})
export class HomeComponent {

}
