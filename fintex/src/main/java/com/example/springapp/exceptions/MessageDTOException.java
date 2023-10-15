package com.example.springapp.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageDTOException{
    private final String errorMessage;
}
