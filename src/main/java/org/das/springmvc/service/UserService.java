package org.das.springmvc.service;

import org.das.springmvc.dto.Mapper;
import org.das.springmvc.dto.UserDto;
import org.das.springmvc.model.Pet;
import org.das.springmvc.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {

    private final Map<Long, User> userMap;
    private AtomicLong idUserCounter;
    private final PetService petService;
    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(PetService petService) {
        this.petService = petService;
        this.userMap = new HashMap<>();
        this.idUserCounter = new AtomicLong();
    }

    public User create(User userToCreate) {
        var newUserId = idUserCounter.incrementAndGet();
        LOGGER.info("execute method Create in UserService, user: user={} and id: id={}", userToCreate, newUserId);

        List<Pet> pets = new ArrayList<>();
        if (!userToCreate.isPetsEmpty()) {
            pets = petService.create(userToCreate.pets(), newUserId);
        }
        var newUser = new User(
                newUserId,
                userToCreate.name(),
                userToCreate.email(),
                userToCreate.age(),
                pets
        );
        this.userMap.put(newUserId, newUser);
        return newUser;
    }

    public User updateById(User userToUpdate) {
       return userMap.put(userToUpdate.id(), userToUpdate);
    }

    public User updateById(Long id, User userToUpdate) {
        LOGGER.info("execute method updateById in UserService, user: user={} and id: id={}", userToUpdate, id);
        User user = Optional.ofNullable(userMap.get(id))
                .orElseThrow(() -> new NoSuchElementException("No such user with id=%s"
                        .formatted(id)));

        User userToCreate;
        if (userToUpdate.isPetsEmpty()) {
            userToCreate = new User (
                    user.id(),
                    userToUpdate.name(),
                    userToUpdate.email(),
                    userToUpdate.age(),
                    new ArrayList<>()
            );
        } else {
            List<Pet> pets = petService.create(userToUpdate.pets(), user.id());
             User tmpUser = new User (
                    user.id(),
                    userToUpdate.name(),
                    userToUpdate.email(),
                    userToUpdate.age(),
                    pets
            );
            userToCreate = tmpUser.addPets(user.pets());
        }
        userMap.put(id, userToCreate);
        return userToCreate;
    }

    public User deleteById(Long id) {
        LOGGER.info("execute method deleteById in UserService, id: id={}", id);
       return Optional.ofNullable(userMap.remove(id))
                .orElseThrow(() -> new NoSuchElementException("No such user with id=%s"
                        .formatted(id)));
    }

    public List<User> findAll(String name, String email) {
        LOGGER.info("execute method findAll in UserService, for user with name: name={}, email: email={}"
                ,name ,email);
        List<User> users = userMap.values()
                .stream()
                .filter(user -> Objects.isNull(name) || user.name().equals(name))
                .filter(user -> Objects.isNull(email) || user.email().equals(email))
                .toList();
        if (users.isEmpty()) {
            throw new NoSuchElementException("No such users = %s parameter: name = %s, email = %s"
                    .formatted(users, name, email));
        }
        return users;
    }

    public User findById(Long id) {
        LOGGER.info("execute method findById in UserService, id: id={}", id);
        return Optional.ofNullable(userMap.get(id))
                .orElseThrow(() -> new NoSuchElementException("No such user with id=%s"
                        .formatted(id)));
    }
}
