package org.das.springmvc.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.das.springmvc.model.User;
import org.das.springmvc.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public User create(@RequestBody @Valid User userToCreate) {
        LOGGER.info("Get request for created user: user={}", userToCreate);
        return userService.create(userToCreate);
    }

    @PutMapping("/users/{id}")
    public User updateById(
           @PathVariable @NotNull Long id,
           @RequestBody @Valid User userToUpdate) {
        LOGGER.info("Get request update for user with id: id={}, user: user={}", id, userToUpdate);
        return userService.updateById(id, userToUpdate);
    }

    @DeleteMapping("/users/{id}")
    public void deleteById(@PathVariable @NotNull Long id) {
        LOGGER.info("Get request delete for user with id: id={}", id);
        userService.findById(id);
    }

    @GetMapping("/users")
    public List<User> findAll(
           @RequestParam(value = "name", required = false) String name,
           @RequestParam(value = "email", required = false) String email
    ) {
        LOGGER.info("Get request findAll for user with name: name={}, email: email={}"
                , name, email);
        return userService.findAll(name, email);
    }

    @GetMapping("/users/{id}")
    public User findById(@PathVariable @NotNull Long id) {
        LOGGER.info("Get request find for user with id: id={}", id);
        return userService.findById(id);
    }
}
