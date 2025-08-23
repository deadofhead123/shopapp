package com.example.shopapp.controllers;

import com.example.shopapp.constant.MessageKeys;
import com.example.shopapp.models.dtos.ResponseDTO;
import com.example.shopapp.models.dtos.UserDTO;
import com.example.shopapp.models.dtos.UserLoginDTO;
import com.example.shopapp.models.responses.LoginResponse;
import com.example.shopapp.services.user.IUserService;
import com.example.shopapp.utils.LocalizationUtil;
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

import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final IUserService userService;
    private final LocalizationUtil localizationUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            if (bindingResult.hasErrors()) {
                responseDTO.setErrors(bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()));
                return ResponseEntity.badRequest().body(responseDTO);
            }

            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                responseDTO.setMessage(localizationUtil.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH));
                return ResponseEntity.badRequest().body(responseDTO);
            }

            responseDTO.setData(userService.register(userDTO));
            responseDTO.setMessage(localizationUtil.getLocalizedMessage(MessageKeys.REGISTER_SUCCESSFULLY));

            return ResponseEntity.ok(responseDTO);
        }
        catch(Exception e){
            responseDTO.setMessage(localizationUtil.getLocalizedMessage(e.getMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO,
                                   BindingResult bindingResult) {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            if (bindingResult.hasErrors()) {
                responseDTO.setErrors(bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()));
                return ResponseEntity.badRequest().body(responseDTO);
            }

            LoginResponse loginResponse = LoginResponse.builder()
                    .message(localizationUtil.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                    .token(userService.login(userLoginDTO))
                    .build();

            return ResponseEntity.ok(loginResponse);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    LoginResponse.builder().message(localizationUtil.getLocalizedMessage(MessageKeys.LOGIN_FAILED, e.getMessage())).build()
            );
        }
    }
}
