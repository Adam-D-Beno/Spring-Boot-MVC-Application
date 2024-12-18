package org.das.springmvc.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.das.springmvc.dto.Mapper;
import org.das.springmvc.dto.UserDto;
import org.das.springmvc.model.User;
import org.das.springmvc.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

//todo export in main branch
@RequestMapping("/users")
@RestController
public class UserController {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final Mapper<User, UserDto> mapper;

    @Autowired
    public UserController(UserService userService,
                          Mapper<User, UserDto> mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @PostMapping()
    public ResponseEntity<UserDto> create(@RequestBody @Valid UserDto userDtoToCreate) {
        LOGGER.info("Get request in UserController for created user: user={}", userDtoToCreate);
        User saveUser = userService.create(mapper.toEntity(userDtoToCreate));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toDto(saveUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateById(
           @PathVariable("id") @NotNull Long id,
           @RequestBody @Valid UserDto userDtoToUpdate) {
        LOGGER.info("Get request in UserController update for user with id: id={}, user: user={}"
                ,id ,userDtoToUpdate);
        User updateUser = userService.updateById(id, mapper.toEntity(userDtoToUpdate));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mapper.toDto(updateUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDto> deleteById(@PathVariable("id") @NotNull Long id) {
        LOGGER.info("Get request in UserController delete for user with id: id={}", id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mapper.toDto(userService.deleteById(id)));
    }

    @GetMapping()
    public ResponseEntity<List<UserDto>> findAll(
           @RequestParam(value = "name", required = false) String name,
           @RequestParam(value = "email", required = false) String email
    ) {
        LOGGER.info("Get request in UserController findAll for user with name: name={}, email: email={}"
                ,name ,email);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mapper.toDto(userService.findAll(name, email)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable("id") @NotNull Long id) {
        LOGGER.info("Get request in UserController find for user with id: id={}", id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mapper.toDto(userService.findById(id)));
    }
}
