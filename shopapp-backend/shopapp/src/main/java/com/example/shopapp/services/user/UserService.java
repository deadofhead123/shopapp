package com.example.shopapp.services.user;

import com.example.shopapp.components.converter.UserConverter;
import com.example.shopapp.configurations.JwtTokenUtil;
import com.example.shopapp.entities.Role;
import com.example.shopapp.entities.User;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.dtos.UserDTO;
import com.example.shopapp.models.dtos.UserLoginDTO;
import com.example.shopapp.repositories.RoleRepository;
import com.example.shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserDTO register(UserDTO userDTO) {
        if(userRepository.existsByPhoneNumber(userDTO.getPhoneNumber())){
            throw new DataIntegrityViolationException("Phone number is already in use");
        }

        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .isActive(true)
                .build();

        Role role = roleRepository.findById(userDTO.getRoleId()).orElseThrow(() -> new DataNotFoundException("Role not found"));
        newUser.setRole(role);

        if(userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0){
            newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        return userConverter.convertToDTO(userRepository.save(newUser));
    }

    @Override
    public String login(UserLoginDTO userLoginDTO) {
        String phoneNumber = userLoginDTO.getPhoneNumber();
        String password = userLoginDTO.getPassword();

        // Normal login with phone number and password
        Optional<User> existingUser = userRepository.findByPhoneNumber(phoneNumber);
        if(existingUser.isEmpty()){
            throw new UsernameNotFoundException("Invalid phone number / password " + phoneNumber);
        }

        User user = existingUser.get();
        if(user.getFacebookAccountId() == 0 && user.getGoogleAccountId() == 0){
            if(!passwordEncoder.matches(password, user.getPassword())){
                throw new BadCredentialsException("Password is incorrect");
            }
        }

        // Authentication with Spring Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(phoneNumber, password);
        authenticationManager.authenticate(authenticationToken);

        // Generate token
        return jwtTokenUtil.generateToken(user);
    }
}
