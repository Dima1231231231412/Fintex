package com.example.springapp.database.JDBC;

import com.example.springapp.database.DAO.UserDao;
import com.example.springapp.database.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserDaoJdbc implements UserDao {
    @Autowired
    private final DataSource dataSource;

    @Autowired
    public UserDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    @Override
    public <S extends User> S save(S entity) throws SQLException {
        Connection connection = dataSource.getConnection();
        try (connection){
            PreparedStatement ps = connection.prepareStatement("INSERT INTO USERS (USERNAME, PASSWORD, ROLE) VALUES (?,?,?)");
            ps.setString(1,entity.getUsername());
            ps.setString(2,entity.getPassword());
            ps.setString(3,entity.getRole());
            ps.executeUpdate();
            User user = findUserByUsername(entity.getUsername());
            entity.setId(user.getId());
            return entity;
        }
        catch (SQLException e){
            e.printStackTrace();
            connection.rollback();
        }
        finally {
            connection.close();
        }
        return entity;
    }

    @Override
    public User findUserByUsername(String username) throws SQLException {
        Connection connection = dataSource.getConnection();
        try (connection){
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM USERS WHERE USERNAME=?");
            ps.setString(1,username);
            ResultSet resultSet = ps.executeQuery();
            User user = new User();
            while (resultSet.next()){
                user.setId(resultSet.getInt(1));
                user.setUsername(resultSet.getString(2));
                user.setPassword(resultSet.getString(3));
                user.setRole(resultSet.getString(4));}
            return user;
        }
        finally {
            connection.close();
        }
    }
}
