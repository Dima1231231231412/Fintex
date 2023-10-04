package com.example.springapp.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Weather {
    private int idReg;
    private String reg;
    private int temp;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateTime;

    public Weather(int idReg, String reg, int temp, LocalDateTime dateTime) {
        this.idReg = idReg;
        this.reg = reg;
        this.temp = temp;
        this.dateTime = dateTime;
    }
}