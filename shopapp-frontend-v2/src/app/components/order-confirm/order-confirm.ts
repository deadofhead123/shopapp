import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header';
import { FooterComponent } from '../footer/footer';
import { CartService } from '../../service/cart.service';
import { Product } from '../../models/product';
import { CartItem } from '../../models/cartItem';
import { Subscription } from 'rxjs';
import { ProductService } from '../../service/product.service';
import { environment } from '../../../environments/environment';
import { CurrencyPipe } from '../../shared/pipes/CurrencyPipe.pipe';

@Component({
  selector: 'app-order-confirm',
  imports: [
    HeaderComponent,
    FooterComponent,
    CurrencyPipe,
  ],
  templateUrl: './order-confirm.html',
  styleUrl: './order-confirm.scss'
})
export class OrderConfirmComponent implements OnInit {
  products?: Product[];
  cartItems: CartItem[] = [];
  total: number = 0;
  getProductsAPI: Subscription;

  constructor(
    private cartService: CartService,
    private productService: ProductService
  ) {
    this.getProductsAPI = new Subscription();
  }

  ngOnInit(): void {
    this.getProducts();
  }

  getProducts() {
    let existingCart = this.cartService.getCart();
    let ids: number[] = Array.from(existingCart.keys());
    this.getProductsAPI = this.productService.getProductsByIds(ids).subscribe({
      next: (response: any) => {
        let data = response.data;
        // this.cartItems = data.map((item: Product) => { // 
        //   let product: Product = data.find((x: Product) => x.id === item.id);
        //   if (product) {
        //     product.thumbnail = `${environment.apiBaseUrl}/products/images/${product.thumbnail}`;
        //   }
        //   return {
        //     product: product,
        //     quantity: existingCart.get(item.id)
        //   }
        // });

        this.cartItems = ids.map((id) => {
          const product: Product = data.find((x: Product) => x.id === id);
          if (product) {
            product.thumbnail = `${environment.apiBaseUrl}/products/images/${product.thumbnail}`;
          }
          return {
            product: product,
            quantity: existingCart.get(id)!
          }
        });
      },
      error: (error: any) => {
        console.log(error.message);
      },
      complete: () => {
        debugger
        this.calculateTotal();
      },
    });
  }

  calculateTotal() {
    this.total = this.cartItems?.reduce(
      (tmpTotal, item) => tmpTotal + item.product.price * item.quantity,
      0 // Initial value for "tmpTotal"
    );
  }
}
