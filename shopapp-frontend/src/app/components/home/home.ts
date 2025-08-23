import { Component } from '@angular/core';
import { CurrencyPipe } from '../../shared/pipes/CurrencyPipe.pipe';
import { HeaderComponent } from '../header/header';
import { FooterComponent } from '../footer/footer';

@Component({
  selector: 'app-home',
  imports: [
    CurrencyPipe,
    HeaderComponent,
    FooterComponent,

  ],
  templateUrl: './home.html',
  styleUrl: './home.scss'
})
export class HomeComponent {

}
