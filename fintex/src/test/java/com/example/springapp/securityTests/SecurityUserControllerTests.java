package com.example.springapp.securityTests;

import com.example.springapp.controllers.UserController;
import com.example.springapp.dto.UserDTO;
import com.example.springapp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(UserController.class)
public class SecurityUserControllerTests {

    @Mock
    UserController userController;
    @MockBean
    private UserService userService;

    private MockMvc mockMvc;
    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    // User login

    @Test
    @WithAnonymousUser
    public void testLoginUserWithAnonymousUser() throws Exception {
        UserDTO userDTO = new UserDTO("dima", "321");
        MvcResult mockRes =  mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/login")
                        .accept("application/json;charset=UTF-8")
                        .content(new ObjectMapper().writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Assert.assertEquals(HttpStatus.OK.value(),mockRes.getResponse().getStatus());
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    public void testLoginUserWithMockUser() throws Exception {
        UserDTO userDTO = new UserDTO("dima", "321");
        MvcResult mockRes =  mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/login")
                        .accept("application/json;charset=UTF-8")
                        .content(new ObjectMapper().writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Assert.assertEquals(HttpStatus.OK.value(),mockRes.getResponse().getStatus());
    }

    // User registration

    @Test
    @WithAnonymousUser
    public void testRegUserWithAnonymousUser() throws Exception {
        UserDTO userDTO = new UserDTO("testUser", "123");
        MvcResult mockRes =  mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/reg")
                        .accept("application/json;charset=UTF-8")
                        .content(new ObjectMapper().writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Assert.assertEquals(HttpStatus.CREATED.value(),mockRes.getResponse().getStatus());
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    public void testRegUserWithMockUser() throws Exception {
        UserDTO userDTO = new UserDTO("testUser", "123");
        MvcResult mockRes =  mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/reg")
                        .accept("application/json;charset=UTF-8")
                        .content(new ObjectMapper().writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Assert.assertEquals(HttpStatus.CREATED.value(),mockRes.getResponse().getStatus());
    }
}
