package com.example.springapp.controllers;
import com.example.springapp.service.Weather;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

import static com.example.springapp.service.WeatherService.*;

@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Data not found"),
        @ApiResponse(responseCode = "201", description = "Creating a resource"),
        @ApiResponse(responseCode = "204", description = "No content"),
})
@RestController
@RequestMapping("/api/weather/")
public class WeatherController {
    @GetMapping("/{city}")
    public ResponseEntity<?> getTempCity(@PathVariable String city) {
        return new ResponseEntity<>(getTemperatureForDayInCity(city), HttpStatus.OK);
    }

    //Добавить новый город
    @PostMapping("/{city}")
    public ResponseEntity<Weather> addCity(@PathVariable String city,@RequestBody Weather weather) throws ParseException {
        return new ResponseEntity<>(addNewRecordWeather(city,weather),HttpStatus.CREATED);
    }


    //Обновить погоду по городу
    @PutMapping("/{city}")
    public ResponseEntity<?> updateTempCity(@PathVariable String city, @RequestBody Weather weather) throws ParseException{
        return new ResponseEntity<>(updateTemperatureInCity(city,weather),HttpStatus.CREATED);

    }

    //Удалить город
    @DeleteMapping("/{city}")
    public ResponseEntity<Void> deleteCity(@PathVariable String city) {
        deleteCityInformation(city);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
