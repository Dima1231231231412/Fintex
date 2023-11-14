package com.example.springapp.mappers;

import com.example.springapp.database.entity.User;
import com.example.springapp.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User of (UserDTO userDTO){
        return new User(null, userDTO.getUsername(), userDTO.getPassword(), null);
    }
}
