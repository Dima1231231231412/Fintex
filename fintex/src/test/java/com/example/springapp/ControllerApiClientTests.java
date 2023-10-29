package com.example.springapp;

import com.example.springapp.controllers.ControllerApiClient;
import com.example.springapp.service.CurrentWeatherDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org. junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation. Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test. web.servlet.result. MockMvcResultMatchers;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ControllerApiClientTests {

    @Autowired
    private ControllerApiClient controllerApiClient;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    WebTestClient webTestClient;

    @Test
    public void testGetCurrentWeatherInCity() throws Exception {
        String cityName = "Moscow";
        CurrentWeatherDTO actualWeather = controllerApiClient.getCurrentWeatherInCity(cityName);
        String responseJson = webTestClient.get().uri("/api/weather/get/{city}", cityName)
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody()
                .blockFirst();
        ObjectMapper objectMapper = new ObjectMapper();
        CurrentWeatherDTO webTestClientWeather = objectMapper.readValue(responseJson, CurrentWeatherDTO.class);
        Assertions.assertEquals(actualWeather, webTestClientWeather);
    }

    @Test
    public void testGetCurrentWeatherInCityBadRequest() throws Exception {
        String cityName = "Rostovsdg";
        mockMvc.perform(get("/api/weather/get/{city}",cityName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertEquals("No location found matching parameter 'q'", result.getResolvedException().getMessage()));

    }

}
