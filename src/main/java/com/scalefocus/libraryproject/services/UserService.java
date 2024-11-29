package com.scalefocus.libraryproject.services;

import com.scalefocus.libraryproject.models.LoginRequest;
import com.scalefocus.libraryproject.entities.UserEntity;
import com.scalefocus.libraryproject.models.LoginResponse;
import com.scalefocus.libraryproject.models.UserModel;
import com.scalefocus.libraryproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.scalefocus.libraryproject.exceptions.RegisterException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

import java.time.ZonedDateTime;

import static com.scalefocus.libraryproject.enums.Role.USER;

@Service
public class UserService {
    private UserRepository userRepository;
    private final AuthService authService;
    private final JwtService jwtService;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserRepository userRepository, AuthService authService, JwtService jwtService) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.jwtService = jwtService;
    }

    public UserModel getUser(Long id) {
        UserEntity userEntity = userRepository.getReferenceById(id);
        return UserModel.builder().id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .role(userEntity.getRole())
                .createdAt(userEntity.getCreatedAt()).build();
    }

    public String createUser(UserModel userModel) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userModel.getUsername());
        userEntity.setEmail(userModel.getEmail());
        userEntity.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userEntity.setRole(USER);
        userEntity.setCreatedAt(ZonedDateTime.now());
        try {
            userRepository.save(userEntity);
            return "User created";
        } catch (Exception e) {
            return new RegisterException("User already exists").getMessage();
        }
    }
    public String updateUser(Long id, UserModel user) {
        UserEntity userEntity = userRepository.getReferenceById(id);
        userEntity.setUsername(user.getUsername());
        userEntity.setEmail(user.getEmail());
        userEntity.setRole(user.getRole());
        userRepository.save(userEntity);
        return "Saved changes to account with id: " + id;
    }

    public boolean deleteUser(Long id) {
        Optional<UserEntity> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            user.setStatus((short) 0);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        UserEntity authenticatedUser = authService.authenticate(loginRequest);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        return new LoginResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());
    }
}