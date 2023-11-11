package com.example.springapp.database.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USERS")
public class User {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min=5, message = "Не меньше 7 знаков")
    @Column(name = "USERNAME",nullable = false,unique = true)
    private String username;

    @Size(min=7, message = "Не меньше 7 знаков")
    @Column(name = "PASSWORD",nullable = false)
    private String password;


    @Column(name = "ROLE")
    private String role;
}


