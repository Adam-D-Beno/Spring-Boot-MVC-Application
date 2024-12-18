package org.das.springmvc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.das.springmvc.model.Pet;
import org.das.springmvc.model.User;
import org.das.springmvc.service.PetService;
import org.das.springmvc.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class PetControllerTest {

    @Autowired
    private UserService userService;
    @Autowired
    private PetService petService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldSuccessCreatePetAndAddThatPetInUserList() throws Exception {
        userService.create(
                new User(
                        null,
                        "test",
                        "test@test.ru",
                        35,
                        new ArrayList<>())
        );

      String jsonNewPet = objectMapper.writeValueAsString(new Pet(
                null,
                "testDoc",
                1L
        ));
        String jsonPetCreated = mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonNewPet))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Pet petCreated = objectMapper.readValue(jsonPetCreated, Pet.class);
        User userFound = userService.findById(petCreated.userId());

        org.junit.jupiter.api.Assertions.assertEquals(userFound.id(), petCreated.userId());
        org.junit.jupiter.api.Assertions.assertEquals(userFound.pets().getFirst().id(), petCreated.id());
        org.junit.jupiter.api.Assertions.assertEquals(userFound.pets().getFirst().name(), petCreated.name());
        org.junit.jupiter.api.Assertions.assertEquals(userFound.pets().getFirst().userId(), petCreated.userId());
    }
}