package com.example.springapp.securityTests;

import com.example.springapp.controllers.WeatherController;
import com.example.springapp.service.Weather;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(WeatherController.class)
public class SecurityWeatherTests {

    @Mock
    WeatherController weatherController;
    private MockMvc mockMvc;
    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(weatherController).build();
    }

    // Get  weather

    @Test
    @WithAnonymousUser
    public void testGetWeatherWithAnonymousUser() throws Exception {
        MvcResult mockRes =  mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/weather/Ryazan")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        Assert.assertEquals(HttpStatus.OK.value(),mockRes.getResponse().getStatus());
    }

    @Test
    @WithMockUser(roles = {"USER,ADMIN"})
    public void testGetWeatherWithMockUser() throws Exception {
        MvcResult mockRes =  mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/weather/Ryazan")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        Assert.assertEquals(HttpStatus.OK.value(),mockRes.getResponse().getStatus());
    }

    // Add  weather

    @Test
    @WithMockUser
    public void testAddWeatherWithRoleUser() throws Exception {
        String city = "Ryazan";
        Weather weather = new Weather(6,"Moscow",31, LocalDateTime.now());
        MvcResult mockRes =  mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/weather/{city}",city)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(weather)))
                .andReturn();

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),mockRes.getResponse().getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAddWeatherWithRoleAdmin() throws Exception {
        String city = "Ryazan";
        Weather weather = new Weather(6,"Moscow",31, LocalDateTime.now());
        MvcResult mockRes =  mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/weather/{city}",city)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(weather)))
                .andReturn();

        Assert.assertEquals(HttpStatus.CREATED.value(),mockRes.getResponse().getStatus());
    }

    // Update  weather

    @Test
    @WithMockUser
    public void testUpdateWeatherWithRoleUser() throws Exception {
        String city = "Ryazan";
        Weather weather = new Weather(6,"Moscow",31, LocalDateTime.now());
        MvcResult mockRes =  mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/weather/{city}",city)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(weather)))
                .andReturn();

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),mockRes.getResponse().getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateWeatherWithRoleAdmin() throws Exception {
        String city = "Ryazan";
        Weather weather = new Weather(6,"Moscow",31, LocalDateTime.now());
        MvcResult mockRes =  mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/weather/{city}",city)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(weather)))
                .andReturn();

        Assert.assertEquals(HttpStatus.CREATED.value(),mockRes.getResponse().getStatus());
    }

    // Delete weather

    @Test
    @WithMockUser
    public void testDeleteWeatherWithRoleUser() throws Exception {
        String city = "Moscow";
        MvcResult mockRes =  mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/weather/{city}",city)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),mockRes.getResponse().getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteWeatherWithRoleAdmin() throws Exception {
        String city = "Moscow";
        MvcResult mockRes =  mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/weather/{city}",city)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Assert.assertEquals(HttpStatus.NO_CONTENT.value(),mockRes.getResponse().getStatus());
    }
}
