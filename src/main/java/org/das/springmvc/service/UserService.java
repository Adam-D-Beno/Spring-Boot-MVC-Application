package org.das.springmvc.service;

import org.das.springmvc.model.Pet;
import org.das.springmvc.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final Map<Long, User> userMap;
    private Long idUserCounter;
    private final PetService petService;

    @Autowired
    public UserService(PetService petService) {
        this.petService = petService;
        this.userMap = new HashMap<>();
        this.idUserCounter = 0L;
    }

    public User create(User userToCreate) {
        var newUserId = ++idUserCounter;
        var pets = userToCreate.pets();
        if (pets != null) {
            pets = petService.create(pets);
        } else {
            pets = Collections.emptyList();
        }

        var newUser = new User(
                idUserCounter,
                userToCreate.name(),
                userToCreate.email(),
                userToCreate.age(),
                new ArrayList<>(pets)
        );

        this.userMap.put(newUserId, newUser);
        return newUser;
    }

    public User updateById(Long id, User userToUpdate) {
        return null;
    }

    public void deleteById(Long id) {
    }

    public List<User> findAll(String name, String email) {
        return null;
    }

    public User findById(Long id) {
        return null;
    }
}
