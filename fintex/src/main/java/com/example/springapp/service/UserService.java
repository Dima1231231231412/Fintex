package com.example.springapp.service;

import com.example.springapp.database.JPA.UserJpa;
import com.example.springapp.database.entity.User;
import com.example.springapp.dto.UserDTO;
import com.example.springapp.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class UserService {
    private final UserJpa userJpa;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserMapper userMapper;


    public UserService(@Qualifier("userJpa") UserJpa userJpa) {
        this.userJpa = userJpa;
    }

    public ResponseEntity<?> login(UserDTO userDTO) {
        User user = userMapper.of(userDTO);
        User findUser = userJpa.findUserByUsername(user.getUsername());
        if(findUser == null || !passwordEncoder.matches(user.getPassword(), findUser.getPassword())){
            return new ResponseEntity<>("Неверно указан логин/пароль",HttpStatus.UNAUTHORIZED);
        }
        //Запоминаем в память аутентификационную информацию
        CustomUserDetailsService customUserDetailsService = new CustomUserDetailsService();
        customUserDetailsService.userDetailsService(findUser);

        return  new ResponseEntity<>(findUser,
                getHttpHeadersAuthorization(findUser.getUsername(), findUser.getPassword()),
                HttpStatus.OK);
    }

    public ResponseEntity<?> reg (UserDTO userDTO){
        User user = userMapper.of(userDTO);
        User findUser = userJpa.findUserByUsername(user.getUsername());
        if(findUser != null){
            return new ResponseEntity<>("Логин занят",HttpStatus.NOT_FOUND);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(user.getRole() == null)
            user.setRole("USER");
        System.out.println(user);
        userJpa.save(user);
        return new ResponseEntity<>("Пользователь успешно создан!",HttpStatus.CREATED);
    }


    public HttpHeaders getHttpHeadersAuthorization(String username,String password){
        String authHeader = username + ":" + password;
        byte[] encodedAuthHeader = Base64.getEncoder().encode(authHeader.getBytes());
        String authHeaderString = "Basic " + new String(encodedAuthHeader);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeaderString);
        return headers;
    }

}
