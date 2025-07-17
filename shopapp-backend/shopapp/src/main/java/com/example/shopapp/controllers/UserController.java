package com.example.shopapp.controllers;

import com.example.shopapp.models.dtos.ResponseDTO;
import com.example.shopapp.models.dtos.UserDTO;
import com.example.shopapp.models.dtos.UserLoginDTO;
import com.example.shopapp.services.user.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            if (bindingResult.hasErrors()) {
                responseDTO.setErrors(bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()));
                return ResponseEntity.badRequest().body(responseDTO);
            }

            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                responseDTO.setErrors(List.of("Password and retype password does not match"));
                return ResponseEntity.badRequest().body(responseDTO);
            }

            responseDTO.setData(userService.register(userDTO));
            responseDTO.setMessage("Register successfully");

            return ResponseEntity.ok(responseDTO);
        }
        catch(Exception e){
            responseDTO.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO, BindingResult bindingResult) {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            if (bindingResult.hasErrors()) {
                responseDTO.setErrors(bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()));
                return ResponseEntity.badRequest().body(responseDTO);
            }

            responseDTO.setData(userService.login(userLoginDTO));
            responseDTO.setMessage("Login successfully");
            return ResponseEntity.ok(responseDTO);
        }
        catch(Exception e){
            responseDTO.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }
}
