package com.example.springapp;

import com.example.springapp.controllers.WeatherController;
import com.example.springapp.service.Weather;
import com.example.springapp.service.WeatherService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDateTime;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(MockitoJUnitRunner.class)
public class WeatherControllerSpyTest {
    @InjectMocks
    private WeatherController weatherController;

    @Spy
    private WeatherService weatherService;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(weatherController).build();
    }


    @Test
    public void testAddCity() throws Exception {
        String cityName = "TestCity";

        Weather weather = new Weather(5, cityName, 30,LocalDateTime.of(2023,10,10,15,20));

        Mockito.doReturn(weather).when(weatherService).addNewRecordWeather(Mockito.anyString(), Mockito.any(Weather.class));

        String jsonStringWeather = """
               {"idReg": 5,
                "reg": "TestCity",
                "temp": 30,
                "dateTime":"2023-10-29T15:00"}""";

        mockMvc.perform(post("/api/weather/{city}", cityName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStringWeather))
                        .andExpectAll(
                                jsonPath("$.dateTime").value("2023-10-29T15:40"), //Fatal
                                jsonPath("$.temp").value(30) //Successful
                        );

    }
}
