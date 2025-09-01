import { Injectable } from "@angular/core";

@Injectable({
    'providedIn': 'root'
})
export class CartService {
    cart: Map<number, number> = new Map<number, number>();
    cartKeyword: string = 'cart';

    constructor() {
        debugger
        const storedCart = localStorage.getItem(this.cartKeyword);
        // Lưu ở localStorage là lưu kiểu "key-value: string-string" 
        // nên muốn sử dụng được phần value thì nên convert sang JSON
        if (storedCart) {
            this.cart = new Map(JSON.parse(storedCart));
        }
    }

    // quantity: number = 1
    addToCart(productId: number, quantity: number = 1) {
        debugger
        if (this.cart.has(productId)) {
            this.cart.set(productId, this.cart.get(productId)! + quantity);
        }
        else {
            this.cart.set(productId, quantity);
        }
        this.saveCartToLocalStorage();
    }

    // Lưu ở localStorage là lưu kiểu "key-value: string-string" 
    // nên muốn lưu được phần value thì nên convert object sang string
    saveCartToLocalStorage() {
        debugger
        localStorage.setItem(this.cartKeyword, JSON.stringify(Array.from(this.cart.entries())));
    }

    clearCart() {
        this.cart.clear();
        this.saveCartToLocalStorage();
    }
}
