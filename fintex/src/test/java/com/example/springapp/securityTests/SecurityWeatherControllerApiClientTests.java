package com.example.springapp.securityTests;

import com.example.springapp.controllers.ControllerApiClient;
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
@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(ControllerApiClient.class)
public class SecurityWeatherControllerApiClientTests {

    @Mock
    ControllerApiClient controllerApiClient;

    private MockMvc mockMvc;
    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controllerApiClient).build();
    }

    @Test
    @WithAnonymousUser
    public void testApiClientGetCurrentWeatherWithAnonymousUser() throws Exception {
        MvcResult mockRes =  mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/weather/get/Ryazan")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        System.out.println(mockRes.getResponse().getContentAsString());
        Assert.assertEquals(HttpStatus.OK.value(),mockRes.getResponse().getStatus());
    }

    @Test
    @WithMockUser(roles = {"USER,ADMIN"})
    public void testApiClientGetCurrentWeatherWithMockUser() throws Exception {
        MvcResult mockRes =  mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/weather/get/Ryazan")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        System.out.println(mockRes.getResponse().getContentAsString());
        Assert.assertEquals(HttpStatus.OK.value(),mockRes.getResponse().getStatus());
    }
}
