package org.das.springmvc.service;

import org.das.springmvc.model.Pet;
import org.das.springmvc.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final Map<Long, User> userMap;
    private Long idUserCounter;
    private Long idPetCounter;

    public UserService() {
        this.userMap = new HashMap<>();
        this.idUserCounter = 0L;
        this.idPetCounter = 0L;
    }

    public User create(User userToCreate) {
        var newUserId = ++idUserCounter;

        List<Pet> pets = userToCreate.pets().stream()
                .map(pet -> new Pet(++idPetCounter, pet.name(), newUserId))
                .toList();

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
