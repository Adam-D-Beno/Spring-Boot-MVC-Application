package org.das.springmvc.controller;

import jakarta.validation.constraints.NotNull;
import org.das.springmvc.dto.Mapper;
import org.das.springmvc.dto.PetDto;
import org.das.springmvc.model.Pet;
import org.das.springmvc.model.User;
import org.das.springmvc.service.PetService;
import org.das.springmvc.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/pets")
@RestController
public class PetController {
    private final static Logger LOGGER = LoggerFactory.getLogger(PetController.class);
    private final PetService petService;
    private final UserService userService;
    private final Mapper<Pet, PetDto> mapper;

    @Autowired
    public PetController(PetService petService,
                         UserService userService,
                         Mapper<Pet, PetDto> mapper) {
        this.petService = petService;
        this.userService = userService;
        this.mapper = mapper;
    }

    @PostMapping()
    public ResponseEntity<PetDto> create(@RequestBody @Validated PetDto petDtoToCreate) {
        LOGGER.info("Get request in PetController for created pet: pet={}", petDtoToCreate);

        Pet pet = petService.create(mapper.toEntity(petDtoToCreate));
        if (petDtoToCreate.userId() != null) {
            User newUser = userService.findById(petDtoToCreate.userId()).addPet(pet);
            userService.updateById(newUser);
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toDto(pet));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetDto> updateById(
           @PathVariable("id") @NotNull Long id,
           @RequestBody @Validated PetDto petDtoToUpdate) {
        LOGGER.info("Get request in PetController update for pet with id: id={}, pet: pet={}", id, petDtoToUpdate);
        Pet updatePet = petService.updateById(id, mapper.toEntity(petDtoToUpdate));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mapper.toDto(updatePet));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PetDto> deleteById(@PathVariable("id") @NotNull Long id) {
        LOGGER.info("Get request in PetController delete for pet with id: id={}", id);
        Pet pet = petService.deleteById(id);
        if (!pet.isUserIdEmpty()) {
            LOGGER.info("execute method findById and removePet in UserService, userId: id={}, pet: pet={}"
                    ,id ,pet);
            User newUser = userService.findById(pet.userId()).removePet(pet);
            userService.updateById(newUser);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mapper.toDto(pet));
    }

    @GetMapping()
    public ResponseEntity<List<PetDto>> findAll(
           @RequestParam(value = "name", required = false) String name,
           @RequestParam(value = "userId", required = false) Long userId
    ) {
        LOGGER.info("Get request in PetController findAll for pet with name: name={}, userId: userId={}"
                , name, userId);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(mapper.toDto(petService.findAll(name, userId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetDto> findById(@PathVariable("id") @NotNull Long id) {
        LOGGER.info("Get request in PetController find for pet with id: id={}", id);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(mapper.toDto(petService.findById(id)));
    }
}
