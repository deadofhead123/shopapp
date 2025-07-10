package com.example.shopapp.components.converter;

import com.example.shopapp.dtos.UserDTO;
import com.example.shopapp.entities.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConverter {
    private final ModelMapper modelMapper;

    public User convertToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    public UserDTO convertToDTO(User user) {
        if(user == null) return null;
        return modelMapper.map(user, UserDTO.class);
    }
}
