import { Component, OnInit } from '@angular/core';
import { CurrencyPipe } from '../../shared/pipes/CurrencyPipe.pipe';
import { HeaderComponent } from '../header/header';
import { FooterComponent } from '../footer/footer';
import { ProductService } from '../../service/product.service';
import { Product } from '../../models/product';
import { debug } from 'console';
import { environment } from '../../environments/environment';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  imports: [
    CurrencyPipe,
    HeaderComponent,
    FooterComponent,
    CommonModule,
  ],
  templateUrl: './home.html',
  styleUrl: './home.scss'
})
export class HomeComponent implements OnInit {
  products: Product[] = [];
  currentPage: number = 1;
  itemsPerPage: number = 12;
  // pages: number[] = [];
  totalPages: number = 0;
  visiblePages: number[] = [];

  constructor(private productService: ProductService) {

  }

  ngOnInit(): void {
    this.getProducts(this.currentPage, this.itemsPerPage);
  }

  getProducts(page: number, size: number) {
    this.productService.getProducts(this.currentPage, this.itemsPerPage).subscribe({
      next: (response: any) => {
        debugger
        response.products.forEach((product: Product) => {
          product.url = `${environment.apiBaseUrl}/products/images/${product.thumbnail}`;
        });
        this.products = response.products;
        this.totalPages = response.totalPages;
        this.visiblePages = this.generateVisiblePageArray(this.currentPage, this.totalPages);
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
    this.getProducts(this.currentPage, this.itemsPerPage);
  }

  generateVisiblePageArray(currentPage: number, totalPages: number): number[] {
    const maxVisiblePages = 5;
    const halfVisiblePages = Math.floor(maxVisiblePages / 2);

    let startPage = Math.max(currentPage - halfVisiblePages, 1); // Tránh trường hợp những trang đầu như 1, 2, 3... 1 vài số trang có thể bị âm
    let endPage = Math.min(startPage + maxVisiblePages - 1, totalPages); // Tránh trường hợp đang ở các trang gần cuối, sẽ có các số trang ko nằm trong kết quả tìm kiếm

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
