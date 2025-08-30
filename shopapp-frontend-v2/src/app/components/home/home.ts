import { Component, OnDestroy, OnInit } from '@angular/core';
import { CurrencyPipe } from '../../shared/pipes/CurrencyPipe.pipe';
import { HeaderComponent } from '../header/header';
import { FooterComponent } from '../footer/footer';
import { ProductService } from '../../service/product.service';
import { Product } from '../../models/product';
import { environment } from '../../../environments/environment';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';
import { Category } from '../../models/category';
import { CategoryService } from '../../service/category.service';
import { FormsModule, NgForm } from '@angular/forms';

@Component({
  selector: 'app-home',
  imports: [
    CurrencyPipe,
    HeaderComponent,
    FooterComponent,
    CommonModule,
    FormsModule,
  ],
  templateUrl: './home.html',
  styleUrl: './home.scss'
})
export class HomeComponent implements OnInit, OnDestroy {
  products: Product[] = [];
  currentPage: number = 1;
  itemsPerPage: number = 12;
  // pages: number[] = [];
  totalPages: number = 0;
  visiblePages: number[] = [];
  getProductsAPI: Subscription;
  categories: Category[] = [];
  selectedCategory: number | undefined;

  constructor(
    private productService: ProductService,
    private categoryService: CategoryService
  ) {
    this.getProductsAPI = new Subscription();
  }

  ngOnInit(): void {
    this.getCategories();
    this.getProducts(this.currentPage);
  }

  ngOnDestroy(): void {
    if (this.getProductsAPI) {
      this.getProductsAPI.unsubscribe();
      console.log('getProducts unsubscribed');
    }
  }

  getCategories() {
    debugger
    this.getProductsAPI = this.categoryService.getCategories().subscribe({
      next: (response: any) => {
        debugger
        this.categories = response.data;
        this.selectedCategory = 0;
      },
      error: (error: any) => {
        debugger
        alert('Failed to get categories!');
      },
      complete: () => {
        debugger
      }
    });
  }

  getProducts(page: number) {
    debugger
    this.getProductsAPI = this.productService.getProducts(this.currentPage, this.itemsPerPage).subscribe({
      next: (response: any) => {
        debugger
        response.products.forEach((product: Product) => {
          product.url = `${environment.apiBaseUrl}/products/images/${product.thumbnail}`;
        });
        this.products = response.products;
        this.totalPages = response.totalPages;
        this.visiblePages = this.generateVisiblePageArray();
      },
      error: (error: any) => {
        debugger
        alert('Failed to get products!');
      },
      complete: () => {
        debugger
      }
    });
  }

  onPageChange(page: number) {
    debugger
    this.currentPage = page;
    this.getProducts(this.currentPage);
  }

  generateVisiblePageArray(): number[] {
    const maxVisiblePages = 5;
    const halfVisiblePages = Math.floor(maxVisiblePages / 2);

    let startPage = Math.max(this.currentPage - halfVisiblePages, 1); // Tránh trường hợp những trang đầu như 1, 2, 3... 1 vài số trang có thể bị âm
    let endPage = Math.min(startPage + maxVisiblePages - 1, this.totalPages); // Tránh trường hợp đang ở các trang gần cuối, sẽ có các số trang ko nằm trong kết quả tìm kiếm

    // Tránh trường hợp những trang gần cuối nó sẽ ko hiện đủ 5 con số
    // Ta có thể thử với ví dụ: {currentPage = [18, 19, hoặc 20], totalPages = 20}
    if (endPage - startPage + 1 < maxVisiblePages) {
      startPage = Math.max(endPage - maxVisiblePages + 1, 1);
    }

    // "new Array(endPage - startPage + 1)": tạo 1 mảng mới gồm (endPage - startPage + 1) phần tử
    //   + vì tôi thấy lúc nào "endPage - startPage + 1" cũng bằng maxVisiblePages nên thay vào luôn
    // ".fill(0)": lấp đầy mảng bằng các phần tử có giá trị 0
    // ".map((_, index) => startPage + index)": thay thế các phần tử trong cái mảng vừa rồi (toàn phần tử 0) bằng giá trị ta quy định
    //   + index là chỉ số mảng, bắt đầu từ 0
    return new Array(maxVisiblePages).fill(0).map((_, index) => startPage + index);
  }
}
