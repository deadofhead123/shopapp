package com.example.shopapp.services.user;

import com.example.shopapp.models.dtos.UserDTO;
import com.example.shopapp.models.dtos.UserLoginDTO;

public interface IUserService {
    UserDTO register(UserDTO userDTO);
    String login(UserLoginDTO userLoginDTO);
}
