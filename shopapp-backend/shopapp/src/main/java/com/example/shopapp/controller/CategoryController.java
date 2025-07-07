package com.example.shopapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/categories")
public class CategoryController {
    @GetMapping("")
    public String getAllCategories(){
        return "Hello World";
    }

    @PostMapping
    public String createCategory(){
        return "Create a category";
    }

    @PutMapping("/{id}")
    public String updateCategory(@PathVariable("id") Long id){
        return "Update method by id = " + id;
    }

    @DeleteMapping("/{id}")
    public String deleteCategory(@PathVariable Long id){
        return "Delete category by id = " + id;
    }
}
