package com.example.springapp.database.JPA;

import com.example.springapp.database.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@EnableJpaRepositories
@Repository
public interface CityJpa extends JpaRepository<City, Integer> {
    @Override
    <S extends City> S save(S entity);

    @Override
    void delete(City entity);

    City findCityByName (String name);

    @Modifying
    @Query("update City set name = ?2 where id = ?1")
    void updateCityName(Integer id,String name);
}

