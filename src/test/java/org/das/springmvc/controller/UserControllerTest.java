package org.das.springmvc.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.das.springmvc.model.Pet;
import org.das.springmvc.model.User;
import org.das.springmvc.service.PetService;
import org.das.springmvc.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//todo export in main branch
@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private PetService petService;


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

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> userService.findById(createdUser.id()));
        Assertions.assertNotNull(createdUser.id());
        Assertions.assertTrue(createdUser.isPetsEmpty());
        Assertions.assertEquals(newUser.name(), createdUser.name());
        Assertions.assertEquals(newUser.email(), createdUser.email());
        Assertions.assertEquals(newUser.age(), createdUser.age());
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

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> userService.findById(createdUser.id()));
        Assertions.assertNotNull(createdUser.id());
        Assertions.assertFalse(createdUser.isPetsEmpty());
        Assertions.assertEquals(newUser.name(), createdUser.name());
        Assertions.assertEquals(newUser.email(), createdUser.email());
        Assertions.assertEquals(newUser.age(), createdUser.age());
        Assertions.assertEquals(newUser.pets().getFirst().name(), createdUser.pets().getFirst().name());
        Assertions.assertNotNull(createdUser.pets().getFirst().id());
        Assertions.assertNotNull(createdUser.pets().getFirst().userId());
        Assertions.assertEquals(createdUser.id(), createdUser.pets().getFirst().userId());
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
        ).id();

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

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> userService.findById(updatedUser.id()));
        Assertions.assertTrue(updatedUser.isPetsEmpty());
        Assertions.assertEquals(userForNotExpected.id(), updatedUser.id());
        Assertions.assertNotEquals(userForNotExpected.name(), updatedUser.name());
        Assertions.assertNotEquals(userForNotExpected.email(), updatedUser.email());
        Assertions.assertNotEquals(userForNotExpected.age(), updatedUser.age());
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
        ).id();
        Pet newPet = petService.create(new Pet(null, "cat", 1L));
        var userForNotExpected = new User(
                userCreatedId,
                "test",
                "test@test.ru",
                35,
                new ArrayList<>()
        );

        var userForUpdate = new User(
                null,
                "testUpdate",
                "testUpdate@test.ru",
                45,
                new ArrayList<>(List.of(newPet))
        );
        String jsonForUpdateUser = objectMapper.writeValueAsString(userForUpdate);

        String jsonUpdatedUser = mockMvc.perform(put("/users/{id}", userCreatedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonForUpdateUser))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        User updatedUser = objectMapper.readValue(jsonUpdatedUser, User.class);

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> userService.findById(updatedUser.id()));
        Assertions.assertFalse(updatedUser.isPetsEmpty());
        Assertions.assertTrue(userForNotExpected.isPetsEmpty());
        Assertions.assertEquals(userForNotExpected.id(), updatedUser.id());
        Assertions.assertNotEquals(userForNotExpected.name(), updatedUser.name());
        Assertions.assertNotEquals(userForNotExpected.email(), updatedUser.email());
        Assertions.assertNotEquals(userForNotExpected.age(), updatedUser.age());
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
    void shouldSuccessUserDeleteById() throws Exception {
        var userForDelete = userService.create(
                new User(
                        null,
                        "test",
                        "test@test.ru",
                        35,
                        new ArrayList<>(
                                List.of(new Pet(null, "cat", null))))
        );

        mockMvc.perform(delete("/users/{id}", userForDelete.id())
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
        Assertions.assertThrows(java.util.NoSuchElementException.class,
                () ->userService.findById(userForDelete.id())
        );
    }

    @Test
    void shouldNotSuccessUserDeleteWhenUserNotFoundAndThrownNoSuchElementException() throws Exception {
        Long userIdForDelete = 1L;
        Exception noSuchElementException = mockMvc.perform(delete("/users/{id}", userIdForDelete)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResolvedException();

        Assertions.assertThrows(java.util.NoSuchElementException.class,
                () -> {
                    assert noSuchElementException != null;
                    throw noSuchElementException;
                }
        );
    }

    @Test
    void shouldSuccessUserFindAll() throws Exception {
        var userFirstExpected = userService.create(
                new User(
                        null,
                        "test",
                        "test@test.ru",
                        35,
                        new ArrayList<>(
                                List.of(new Pet(null, "cat", null))))
        );

        var userSecondExpected = userService.create(
                new User(
                        null,
                        "test2",
                        "test@test2.ru",
                        45,
                        new ArrayList<>(
                                List.of(new Pet(null, "dog", null))))
        );

        String jsonFoundAllUsers = mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<User> foundUsers = objectMapper.readValue(jsonFoundAllUsers, new TypeReference<List<User>>() {});

        assertThat(foundUsers).isNotEmpty();
        assertThat(foundUsers)
                .hasSize(2)
                .contains(userFirstExpected, userSecondExpected);

        assertThat(foundUsers).usingRecursiveFieldByFieldElementComparator().contains(userFirstExpected);
        assertThat(foundUsers).usingRecursiveFieldByFieldElementComparator().contains(userSecondExpected);
    }

    @Test
    void shouldNotSuccessFindAllUserWhenAllUsersNotFoundAndThrownNoSuchElementException() throws Exception {
        Exception noSuchElementException = mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResolvedException();

        Assertions.assertThrows(java.util.NoSuchElementException.class,
                () -> {
                    assert noSuchElementException != null;
                    throw noSuchElementException;
                }
        );
    }

    @Test
    void shouldSuccessUserFindAllWithParameterNameAndEmail() throws Exception {
        var userNotExpected= userService.create(
                new User(
                        null,
                        "test2",
                        "test2@test.ru",
                        45,
                        new ArrayList<>(
                                List.of(new Pet(null, "cat", null))))
        );
        var userExpected = userService.create(
                new User(
                        null,
                        "test",
                        "test@test.ru",
                        35,
                        new ArrayList<>(
                                List.of(new Pet(null, "cat", null))))
        );
        String parameterName = "test";
        String parameterEmail = "test@test.ru";

        String jsonFoundAllUsers = mockMvc.perform(get("/users")
                        .param("name", parameterName)
                        .param("email", parameterEmail)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<User> foundUsers = objectMapper.readValue(jsonFoundAllUsers, new TypeReference<List<User>>() {});

        assertThat(foundUsers).isNotEmpty();
        assertThat(foundUsers).usingRecursiveFieldByFieldElementComparator().contains(userExpected);
        assertThat(foundUsers).doesNotContain(userNotExpected);
    }

    @Test
    void shouldNotSuccessUserFindAllWithParameterNameAndEmailAndThrownNoSuchElementException() throws Exception {
        userService.create(
                new User(
                        null,
                        "test2",
                        "test2@test.ru",
                        45,
                        new ArrayList<>(
                                List.of(new Pet(null, "cat", null))))
        );
       userService.create(
                new User(
                        null,
                        "test1",
                        "test1@test.ru",
                        35,
                        new ArrayList<>(
                                List.of(new Pet(null, "cat", null))))
        );
        String parameterName = "test";
        String parameterEmail = "test@test.ru";

        Exception noSuchElementException = mockMvc.perform(get("/users")
                        .param("name", parameterName)
                        .param("email", parameterEmail)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResolvedException();

        Assertions.assertThrows(java.util.NoSuchElementException.class,
                () -> {
                    assert noSuchElementException != null;
                    throw noSuchElementException;
                }
        );

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

        String jsonFoundUser = mockMvc.perform(get("/users/{id}", userExpected.id())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        User foundUser = objectMapper.readValue(jsonFoundUser, User.class);
        assertThat(userExpected)
                .usingRecursiveComparison()
                .isEqualTo(foundUser);
    }

    @Test
    void shouldNotSuccessFindByIdUserWhenUserNotFoundAndThrownNoSuchElementException() throws Exception {
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