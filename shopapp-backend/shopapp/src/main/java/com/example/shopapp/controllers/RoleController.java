package com.example.shopapp.controllers;

import com.example.shopapp.models.dtos.ResponseDTO;
import com.example.shopapp.services.role.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/roles")
public class RoleController {
    private final IRoleService roleService;

    @GetMapping("")
    public ResponseEntity<?> getAllRoles(){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(roleService.getAllRoles());
        return ResponseEntity.ok(responseDTO);
    }
}
