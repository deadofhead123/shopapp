import { Component } from '@angular/core';
import { HeaderComponent } from '../header/header';
import { FooterComponent } from '../footer/footer';
import { CurrencyPipe } from '../shared/pipes/CurrencyPipe.pipe';

@Component({
  selector: 'app-order',
  imports: [
    HeaderComponent,
    FooterComponent,
    CurrencyPipe,
  ],
  templateUrl: './order.html',
  styleUrl: './order.scss'
})
export class OrderComponent {

}
