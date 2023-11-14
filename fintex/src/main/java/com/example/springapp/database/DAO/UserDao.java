package com.example.springapp.database.DAO;

import com.example.springapp.database.entity.User;

import java.sql.SQLException;

public interface UserDao {
    <S extends User> S save(S entity) throws SQLException;
    User findUserByUsername(String username) throws SQLException;
}
