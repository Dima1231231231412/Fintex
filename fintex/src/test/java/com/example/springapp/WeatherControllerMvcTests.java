package com.example.springapp;

import com.example.springapp.controllers.WeatherController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeatherControllerMvcTests {
    @Autowired
    MockMvc mockMvc;

    @Test
    public void testGetTemperatureForDayInCity() throws Exception {
        String cityName = "Ryazan";

        MvcResult mockRes = mockMvc.perform(get("/api/weather/{city}",cityName))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(handler().handlerType(WeatherController.class))
                .andReturn();

        int responseStatus = mockRes.getResponse().getStatus();
        String responseContent = mockRes.getResponse().getContentAsString();

        if(responseStatus == HttpStatus.OK.value()){
            assertEquals(responseContent,"""
                  {"2023-10-29T15:00":23}""");
        }
        else if(responseStatus == HttpStatus.BAD_REQUEST.value()){
            assertEquals(mockRes.getResolvedException().getMessage(),"Запись(-и) с температурой на сегодняшний день по городу "+ cityName +" не найдены");
        }
        else
            assertEquals(mockRes.getResolvedException().getMessage(),"Город не найден");

    }

    @Test
    public void testDeleteCity() throws Exception {
        String cityName = "Moscow";

        MvcResult mockRes = mockMvc.perform(delete("/api/weather/{city}",cityName)).andReturn();
        if(mockRes.getResponse().getStatus() == 404){
            assertEquals(mockRes.getResolvedException().getMessage(),"Город не найден");
        }
        assertEquals(mockRes.getResponse().getStatus(),204);

    }

}

