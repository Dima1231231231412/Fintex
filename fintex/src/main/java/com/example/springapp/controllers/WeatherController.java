package com.example.springapp.controllers;

import com.example.springapp.service.Weather;
import com.example.springapp.service.WeatherService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;


@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Data not found"),
        @ApiResponse(responseCode = "201", description = "Creating a resource"),
        @ApiResponse(responseCode = "204", description = "No content"),
})
@RestController
@RequestMapping("/api/weather/")
public class WeatherController {
    @Autowired
    WeatherService weatherService;

    @GetMapping("/{city}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> getTempCity(@PathVariable String city) {
        return new ResponseEntity<>(weatherService.getTemperatureForDayInCity(city), HttpStatus.OK);
    }

    //Добавить новый город
    @PostMapping("/{city}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Weather> addCity(@PathVariable String city,@RequestBody Weather weather) throws ParseException {
        return new ResponseEntity<>(weatherService.addNewRecordWeather(city,weather),HttpStatus.CREATED);
    }


    //Обновить погоду по городу
    @PutMapping("/{city}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateTempCity(@PathVariable String city, @RequestBody Weather weather) throws ParseException{
        return new ResponseEntity<>(weatherService.updateTemperatureInCity(city,weather),HttpStatus.CREATED);

    }

    //Удалить город
    @DeleteMapping("/{city}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCity(@PathVariable String city) {
        weatherService.deleteCityInformation(city);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
