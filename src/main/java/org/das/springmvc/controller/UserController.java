package org.das.springmvc.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.das.springmvc.model.User;
import org.das.springmvc.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<User> create(@RequestBody @Valid User userToCreate) {
        LOGGER.info("Get request in UserController for created user: user={}", userToCreate);
        User user = userService.create(userToCreate);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateById(
           @PathVariable @NotNull Long id,
           @RequestBody @Valid User userToUpdate) {
        LOGGER.info("Get request in UserController update for user with id: id={}, user: user={}"
                ,id ,userToUpdate);
        User user = userService.updateById(id, userToUpdate);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable @NotNull Long id) {
        LOGGER.info("Get request in UserController delete for user with id: id={}", id);
        userService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> findAll(
           @RequestParam(value = "name", required = false) String name,
           @RequestParam(value = "email", required = false) String email
    ) {
        LOGGER.info("Get request in UserController findAll for user with name: name={}, email: email={}"
                ,name ,email);
        List<User> users = userService.findAll(name, email);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> findById(@PathVariable @NotNull Long id) {
        LOGGER.info("Get request in UserController find for user with id: id={}", id);
        User user = userService.findById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(user);
    }
}
