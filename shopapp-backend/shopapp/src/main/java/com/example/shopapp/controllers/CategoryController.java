package com.example.shopapp.controllers;

import com.example.shopapp.constant.MessageKeys;
import com.example.shopapp.models.dtos.CategoryDTO;
import com.example.shopapp.models.dtos.ResponseDTO;
import com.example.shopapp.services.category.ICategoryService;
import com.example.shopapp.utils.LocalizationUtil;
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
    private final ICategoryService categoryService;
    private final LocalizationUtil localizationUtil;

    @GetMapping("")
    public ResponseEntity<?> getAllCategories() {
        ResponseDTO responseDTO = new ResponseDTO();

        responseDTO.setData(categoryService.getAllCategories());
        responseDTO.setMessage("All categories found");
//        Integer page = 1, limit;
//        if(params.containsKey("page")) {
//            page = Integer.parseInt(params.get("page").toString());
//        }

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult bindingResult){
        ResponseDTO responseDTO = new ResponseDTO();

        try{
            if(bindingResult.hasErrors()) {
                responseDTO.setErrors(bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()));
                return ResponseEntity.badRequest().body(responseDTO);
            }

            responseDTO.setData(categoryService.createCategory(categoryDTO));
            responseDTO.setMessage(localizationUtil.getLocalizedMessage(MessageKeys.INSERT_CATEGORY_SUCCESSFULLY));

            return ResponseEntity.ok(responseDTO);
        }
        catch (Exception e){
            responseDTO.setMessage(localizationUtil.getLocalizedMessage(MessageKeys.INSERT_CATEGORY_FAILED));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Long id,
                                            @Valid @RequestBody CategoryDTO categoryDTO,
                                            BindingResult bindingResult){
        ResponseDTO responseDTO = new ResponseDTO();

        try{
            if(bindingResult.hasErrors()) {
                responseDTO.setErrors(bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()));
                return ResponseEntity.badRequest().body(responseDTO);
            }

            responseDTO.setData(categoryService.updateCategory(id, categoryDTO));
            responseDTO.setMessage(localizationUtil.getLocalizedMessage(MessageKeys.UPDATE_CATEGORY_SUCCESSFULLY));

            return ResponseEntity.ok(responseDTO);
        }
        catch (Exception e){
            responseDTO.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id){
        ResponseDTO responseDTO = new ResponseDTO();

        try{
            responseDTO.setData(categoryService.deleteCategory(id));
            responseDTO.setMessage(localizationUtil.getLocalizedMessage(MessageKeys.DELETE_CATEGORY_SUCCESSFULLY, id));

            return ResponseEntity.ok(responseDTO);
        }
        catch (Exception e){
            responseDTO.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
        }
    }
}
