import { Component, OnDestroy, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header';
import { FooterComponent } from '../footer/footer';
import { ProductService } from '../../service/product.service';
import { Subscription } from 'rxjs';
import { Product } from '../../models/product';
import { CurrencyPipe } from '../../shared/pipes/CurrencyPipe.pipe';
import { environment } from '../../../environments/environment';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CartService } from '../../service/cart.service';

@Component({
  selector: 'app-detail-product',
  imports: [
    HeaderComponent,
    FooterComponent,
    CurrencyPipe,
    CommonModule,
    FormsModule,
  ],
  templateUrl: './detail-product.component.html',
  styleUrl: './detail-product.component.scss'
})
export class DetailProductComponent implements OnInit, OnDestroy {
  product?: Product;
  currentImageIndex: number = 0;
  imageQuantity: number = 0;
  quantity: number = 1;
  getProduct: Subscription;

  constructor(
    private productService: ProductService,
    private cartService: CartService
  ) {
    this.getProduct = new Subscription();
  }

  ngOnInit(): void {
    this.getProductById();
  }

  ngOnDestroy(): void {
    debugger
    if (this.getProduct) {
      this.getProduct.unsubscribe();
    }
  }

  getProductById() {
    let idParam = 7;
    idParam = +idParam;
    if (isNaN(idParam)) {
      console.log('Invalid product id! This is not a number!');
    }
    else {
      this.getProduct = this.productService.getProductById(idParam).subscribe({
        next: (response: any) => {
          this.product = response.data;
          if (this.product?.product_images && this.product?.product_images.length > 0) {
            this.product?.product_images.forEach((product_image: ProductImage) => {
              product_image.image_url = `${environment.apiBaseUrl}/products/images/${product_image.image_url}`;
            });
            this.imageQuantity = this.product?.product_images.length;
          }
        },
        error: (error: any) => {
          debugger
          alert(error.error.message);
        },
      });
    }
  }

  adjustCurrentImageIndexLimit(index: number) {
    this.currentImageIndex = index;
    if (index < 0) {
      this.currentImageIndex = 0;
    }
    else if (index >= this.imageQuantity) {
      this.currentImageIndex = index - 1;
    }
  }

  handleThumbnailClick(index: number) {
    this.currentImageIndex = index;
  }

  handlePreviousImage() {
    this.adjustCurrentImageIndexLimit(this.currentImageIndex - 1);
  }

  handleNextImage() {
    this.adjustCurrentImageIndexLimit(this.currentImageIndex + 1);
  }

  addToCart() {
    debugger
    if (this.product) {
      this.cartService.addToCart(this.product?.id, this.quantity);
    }
    else {
      alert('Error! Product not exist!');
    }
  }

  decreaseQuantity() {
    this.quantity--;
    if (this.quantity < 0) {
      this.quantity = 0;
    }
  }

  increaseQuantity() {
    this.quantity++;
  }
}