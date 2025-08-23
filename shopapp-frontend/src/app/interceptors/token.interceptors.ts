import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { TokenService } from "../service/token/token.service";

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
    constructor(private tokenService: TokenService) { }

    // Xen ngang (intercept) quá trình gửi request để thêm token
    intercept(
        req: HttpRequest<any>,
        next: HttpHandler
    ): Observable<HttpEvent<any>> {
        const token = this.tokenService.getToken();
        // - Sửa 1 phần header để nó mang token đi.
        // + Ta không thể sửa request trực tiếp được (Theo tôi nghĩ thì là để tránh lỗi cái request chính đang gửi đi), 
        // mà phải tạo ra 1 bản sao của nó, sửa bản sao đó, 
        // cuối cùng cho cái chính tham chiếu cái bản sao. 
        if (token) {
            req = req.clone({
                setHeaders: {
                    Authorization: `Bearer ${token}`,
                }
            });
        }
        return next.handle(req);
    }

}