package org.das.springmvc.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.das.springmvc.model.Pet;
import org.das.springmvc.model.User;
import org.das.springmvc.service.PetService;
import org.das.springmvc.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PetController {
    private final static Logger LOGGER = LoggerFactory.getLogger(PetController.class);
    private final PetService petService;
    private final UserService userService;

    @Autowired
    public PetController(PetService petService, UserService userService) {
        this.petService = petService;
        this.userService = userService;
    }

    @PostMapping("/pets")
    public Pet create(@RequestBody @Valid Pet petToCreate) {
        LOGGER.info("Get request in PetController for created pet: pet={}", petToCreate);
        Pet pet = petService.create(petToCreate);
        if (!petToCreate.isUserIdEmpty()) {
            userService.findById(petToCreate.getUserId()).addPet(pet);
        }
        return pet;
    }

    @PutMapping("/pets/{id}")
    public Pet updateById(
           @PathVariable @NotNull Long id,
           @RequestBody @Valid Pet petToUpdate) {
        LOGGER.info("Get request in PetController update for pet with id: id={}, pet: pet={}", id, petToUpdate);
        return petService.updateById(id, petToUpdate);
    }

    @DeleteMapping("pets/{id}")
    public void deleteById(@PathVariable @NotNull Long id) {
        LOGGER.info("Get request in PetController delete for pet with id: id={}", id);
        Pet pet = petService.deleteById(id);
        if (!pet.isUserIdEmpty()) {
            LOGGER.info("execute method findById and removePet in UserService, userId: id={}, pet: pet={}"
                    ,id ,pet);
            userService.findById(pet.getUserId()).removePet(pet);
        }
    }

    @GetMapping("/pets")
    public List<Pet> findAll(
           @RequestParam(value = "name", required = false) String name,
           @RequestParam(value = "userId", required = false) Long userId
    ) {
        LOGGER.info("Get request in PetController findAll for pet with name: name={}, userId: userId={}"
                , name, userId);
        return petService.findAll(name, userId);
    }

    @GetMapping("/pets/{id}")
    public Pet findById(@PathVariable @NotNull Long id) {
        LOGGER.info("Get request in PetController find for pet with id: id={}", id);
        return petService.findById(id);
    }
}
