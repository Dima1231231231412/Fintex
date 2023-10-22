package com.example.springapp.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table (name = "CITY")
public class City {
    @Id
    private Integer id;

    @Column(name = "name")
    private String name;



}
