import { Component, Pipe, PipeTransform } from "@angular/core";

@Pipe({
    name: 'currencyPipe'
})

export class CurrencyPipe implements PipeTransform {
    transform(value: any, ...args: any[]) {
        return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
    }
}