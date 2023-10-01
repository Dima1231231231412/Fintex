package com.example.springapp.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Weather {
    private int idReg;
    private String reg;
    private int temp;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private Date dateTime;

    public Weather(int idReg,String reg, int temp, Date dateTime) {
        this.idReg = idReg;
        this.reg = reg;
        this.temp = temp;
        this.dateTime = dateTime;
    }
}