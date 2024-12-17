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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        Assertions.assertEquals(newUser.getName(), createdUser.getName());
        Assertions.assertEquals(newUser.getEmail(), createdUser.getEmail());
        Assertions.assertEquals(newUser.getAge(), createdUser.getAge());
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
        Assertions.assertEquals(newUser.getName(), createdUser.getName());
        Assertions.assertEquals(newUser.getEmail(), createdUser.getEmail());
        Assertions.assertEquals(newUser.getAge(), createdUser.getAge());
        Assertions.assertEquals(newUser.getPets().getFirst().getName(), createdUser.getPets().getFirst().getName());
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
    void shouldSuccessUpdateUserByIdWithoutPets() throws Exception {

        Long userCreatedId = userService.create(
                new User(
                        null,
                        "test",
                        "test@test.ru",
                        35,
                        new ArrayList<>())
        ).getId();

        var userForNotExpected = new User(
                userCreatedId,
                "test",
                "test@test.ru",
                35,
                new ArrayList<>()
        );

        var newUserForUpdate = new User(
                null,
                "testUpdate",
                "testUpdate@test.ru",
                45,
                new ArrayList<>()
        );
        String jsonForUpdateUser = objectMapper.writeValueAsString(newUserForUpdate);

        String jsonUpdatedUser = mockMvc.perform(put("/users/{id}", userCreatedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonForUpdateUser))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        User updatedUser = objectMapper.readValue(jsonUpdatedUser, User.class);
        Assertions.assertTrue(updatedUser.isPetsEmpty());
        Assertions.assertEquals(userForNotExpected.getId(), updatedUser.getId());
        Assertions.assertNotEquals(userForNotExpected.getName(), updatedUser.getName());
        Assertions.assertNotEquals(userForNotExpected.getEmail(), updatedUser.getEmail());
        Assertions.assertNotEquals(userForNotExpected.getAge(), updatedUser.getAge());
    }

    @Test
    void shouldSuccessUpdateUserByIdWithPets() throws Exception {
        Long userCreatedId = userService.create(
                new User(
                        null,
                        "test",
                        "test@test.ru",
                        35,
                        new ArrayList<>())
        ).getId();

        var userForNotExpected = new User(
                userCreatedId,
                "test",
                "test@test.ru",
                35,
                new ArrayList<>()
        );

        var newUserForUpdate = new User(
                null,
                "testUpdate",
                "testUpdate@test.ru",
                45,
                new ArrayList<>(
                        List.of(new Pet(null, "cat", null)))
        );
        String jsonForUpdateUser = objectMapper.writeValueAsString(newUserForUpdate);

        String jsonUpdatedUser = mockMvc.perform(put("/users/{id}", userCreatedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonForUpdateUser))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        User updatedUser = objectMapper.readValue(jsonUpdatedUser, User.class);
        Assertions.assertFalse(updatedUser.isPetsEmpty());
        Assertions.assertTrue(userForNotExpected.isPetsEmpty());
        Assertions.assertEquals(userForNotExpected.getId(), updatedUser.getId());
        Assertions.assertNotEquals(userForNotExpected.getName(), updatedUser.getName());
        Assertions.assertNotEquals(userForNotExpected.getEmail(), updatedUser.getEmail());
        Assertions.assertNotEquals(userForNotExpected.getAge(), updatedUser.getAge());
    }

    @Test
    void shouldNotUpdateUserWhenRequestInvalid() throws Exception {
        var invalidUser = new User(
                null,
                null,
                "test@test.ru",
                35,
                new ArrayList<>(
                        List.of(new Pet(null, "cat", null)))
        );
        Long userIdForUpdate = 1L;
        String jsonUserForUpdate = objectMapper.writeValueAsString(invalidUser);
        Exception HandlerMethodValidationException = mockMvc.perform(put("/users/{id}", userIdForUpdate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUserForUpdate))
                .andExpect(status().isBadRequest())
                .andReturn().getResolvedException();
        Assertions.assertThrows(HandlerMethodValidationException.class,
                () -> {
                    assert HandlerMethodValidationException != null;
                    throw HandlerMethodValidationException;
                }
        );
    }

    @Test
    void shouldNotSuccessUpdateUserByIdWhenUserNotFound() throws Exception {
        var userForUpdate = new User(
                null,
                "test",
                "test@test.ru",
                35,
                new ArrayList<>(
                        List.of(new Pet(null, "cat", null)))
        );
        Long userIdForSearch = 1L;

        String jsonUserForUpdate = objectMapper.writeValueAsString(userForUpdate);
        Exception noSuchElementException = mockMvc.perform(put("/users/{id}", userIdForSearch)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUserForUpdate))
                .andExpect(status().isNotFound())
                .andReturn().getResolvedException();
        Assertions.assertThrows(java.util.NoSuchElementException.class,
                () -> {
                    assert noSuchElementException != null;
                    throw noSuchElementException;
                }
        );
    }

    @Test
    void shouldSuccessDeleteUserById() {
    }

    @Test
    void shouldNotSuccessDeleteUserById() {
    }

    @Test
    void shouldSuccessUserFindAll() {
    }

    @Test
    void shouldNotSuccessFindAllUser() {
    }

    @Test
    void shouldSuccessFindByIdUser() throws Exception {

        var userExpected = userService.create(
                new User(
                        null,
                        "test",
                        "test@test.ru",
                        35,
                        new ArrayList<>(
                                List.of(new Pet(null, "cat", null))))
        );
        Long userIdForSearch = userExpected.getId();

        String jsonUserForSearch = objectMapper.writeValueAsString(userExpected);
        String jsonFoundUser = mockMvc.perform(get("/users/{id}", userIdForSearch)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUserForSearch))
                .andExpect(status().isFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        User foundUser = objectMapper.readValue(jsonFoundUser, User.class);
        org.assertj.core.api.Assertions.assertThat(userExpected)
                .usingRecursiveComparison()
                .isEqualTo(foundUser);
    }

    @Test
    void shouldNotSuccessFindByIdUser() throws Exception {
        Long userIdForSearch = 1L;
        Exception noSuchElementException = mockMvc.perform(get("/users/{id}", userIdForSearch)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResolvedException();
        Assertions.assertThrows(java.util.NoSuchElementException.class,
                () -> {
                    assert noSuchElementException != null;
                    throw noSuchElementException;
                }
        );
    }
}