package com.example.springapp.database.JPA;

import com.example.springapp.database.entity.WeatherType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherTypeJpa extends JpaRepository<WeatherType, Integer> {
    WeatherType findWeatherTypeByName(String weatherTypeNameIn);

    @Override
    void delete(WeatherType entity);

    @Override
    <S extends WeatherType> S save(S entity);

    @Modifying
    @Query("update WeatherType set name = ?2 where id = ?1")
    void updateWeatherTypeName(Integer id,String name);
}

