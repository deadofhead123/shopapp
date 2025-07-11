package com.example.shopapp.constant;

public enum OrderStatus {
    PENDING("pending"),
    PROCESSING("processing"),
    SHIPPED("shipped"),
    DELIVERED("delivered"),
    CANCELLED("cancelled");

    public final String name;

    OrderStatus(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
