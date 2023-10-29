package com.example.springapp;

import com.example.springapp.controllers.WeatherController;
import com.example.springapp.service.Weather;
import com.example.springapp.service.WeatherService;
import com.example.springapp.service.WeatherServiceStub;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class WeatherControllerStubTest {

    @InjectMocks
    private WeatherController weatherController;

    @Spy
    private WeatherService weatherService = new WeatherServiceStub();

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(weatherController).build();
    }

    @Test
    public void testUpdateTemperatureInCity() throws Exception {
        String cityName = "TestCity";
        Weather weather = new Weather(5, cityName, 17, LocalDateTime.of(2023,10,10,15,20));

        Mockito.doReturn(weather).when(weatherService).updateTemperatureInCity(Mockito.anyString(), Mockito.any(Weather.class));

        String jsonStringWeather = """
           {"idReg": 5,
            "reg": "TestCity",
            "temp": 30,
            "dateTime":"2023-10-29T15:00"}""";

        mockMvc.perform(put("/api/weather/{city}", cityName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStringWeather))
                .andExpect(status().isCreated());
    }
}
