package com.example.springapp.database.JPA;

import com.example.springapp.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpa extends JpaRepository<User,Integer> {
    User findUserByUsername(String username);

    @Override
    <S extends User> S save(S entity);
}
