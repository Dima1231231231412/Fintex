package com.example.springapp.database.DAO;

import com.example.springapp.database.JPA.UserJpa;
import com.example.springapp.database.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@AllArgsConstructor
@Component
public class UserDaoHibernate implements  UserDao{
    private final UserJpa userJpa;
    @Override
    public <S extends User> S save(S entity) throws SQLException {
        return userJpa.save(entity);
    }

    @Override
    public User findUserByUsername(String username) {
        return userJpa.findUserByUsername(username);
    }

}
