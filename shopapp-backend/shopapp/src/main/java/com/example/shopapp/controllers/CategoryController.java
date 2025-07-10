package com.example.shopapp.controllers;

import com.example.shopapp.dtos.CategoryDTO;
import com.example.shopapp.dtos.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    @GetMapping("")
    public ResponseEntity<?> getAllCategories() {
//        Integer page = 1, limit;
//        if(params.containsKey("page")) {
//            page = Integer.parseInt(params.get("page").toString());
//        }
        return ResponseEntity.ok("Get all categories");
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult bindingResult){
        ResponseDTO responseDTO = new ResponseDTO();

        try{
            if(bindingResult.hasErrors()) {
                responseDTO.setErrors(bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()));
                return ResponseEntity.badRequest().body(responseDTO);
            }
            responseDTO.setData(categoryDTO);

            return ResponseEntity.ok("Create a new category");
        }
        catch (Exception e){
            responseDTO.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
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
