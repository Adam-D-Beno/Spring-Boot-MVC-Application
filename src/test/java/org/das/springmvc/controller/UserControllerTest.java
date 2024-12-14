package org.das.springmvc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.das.springmvc.model.Pet;
import org.das.springmvc.model.User;
import org.das.springmvc.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void shouldSuccessCreateUsersWithoutPets() throws Exception {
        var newUser = new User(
                null,
                "test",
                "test@test.ru",
                35,
                new ArrayList<>()
        );
        String jsonUser = objectMapper.writeValueAsString(newUser);
        String createdUserJson = mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        User createdUser = objectMapper.readValue(createdUserJson, User.class);
        Assertions.assertNotNull(createdUser.getId());
        Assertions.assertTrue(createdUser.isPetsEmpty());
        Assertions.assertEquals(newUser, createdUser);
    }

    @Test
    void shouldSuccessCreateUsersWithPets() throws Exception {
        var newUser = new User(
                null,
                "test",
                "test@test.ru",
                35,
                new ArrayList<>(
                        List.of(new Pet(null, "cat", null)))
        );

        String jsonUser = objectMapper.writeValueAsString(newUser);
        String createdUserJson = mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        User createdUser = objectMapper.readValue(createdUserJson, User.class);

        Assertions.assertNotNull(createdUser.getId());
        Assertions.assertFalse(createdUser.isPetsEmpty());
        Assertions.assertEquals(newUser, createdUser);
        Assertions.assertNotNull(createdUser.getPets().getFirst().getId());
        Assertions.assertNotNull(createdUser.getPets().getFirst().getUserId());
        Assertions.assertEquals(createdUser.getId(), createdUser.getPets().getFirst().getUserId());
    }

    @Test
    void shouldNotCreatedUserWhenRequestInvalid() throws Exception {
        var invalidUser = new User(
                null,
                null,
                "test@test.ru",
                35,
                new ArrayList<>(
                        List.of(new Pet(null, "cat", null)))
        );

        String jsonUser = objectMapper.writeValueAsString(invalidUser);
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUser))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldSuccessUpdateUserById() {

    }

    @Test
    void shouldSuccessUserDeleteById() {
    }

    @Test
    void shouldSuccessUserFindAll() {
    }

    @Test
    void shouldSuccessUserFindById() {
    }
}