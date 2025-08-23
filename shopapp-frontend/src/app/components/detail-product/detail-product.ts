import { Component } from '@angular/core';
import { HeaderComponent } from '../header/header';
import { FooterComponent } from '../footer/footer';

@Component({
  selector: 'app-detail-product',
  imports: [
    HeaderComponent,
    FooterComponent,
  ],
  templateUrl: './detail-product.html',
  styleUrl: './detail-product.scss'
})
export class DetailProductComponent {

}
