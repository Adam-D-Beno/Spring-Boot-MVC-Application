package org.das.springmvc.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.das.springmvc.model.Pet;
import org.das.springmvc.model.User;
import org.das.springmvc.service.PetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PetController {
    private final static Logger LOGGER = LoggerFactory.getLogger(PetController.class);
    private final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping("/pets")
    public Pet create(@RequestBody @Valid Pet petToCreate) {
        LOGGER.info("Get request in PetController for created pet: pet={}", petToCreate);
        return petService.create(petToCreate);
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
        petService.deleteById(id);
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
