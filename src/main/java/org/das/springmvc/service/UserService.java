package org.das.springmvc.service;

import org.das.springmvc.model.Pet;
import org.das.springmvc.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserService {

    private final Map<Long, User> userMap;
    private Long idUserCounter;
    private final PetService petService;
    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(PetService petService) {
        this.petService = petService;
        this.userMap = new HashMap<>();
        this.idUserCounter = 0L;
    }

    public User create(User userToCreate) {
        var newUserId = ++idUserCounter;
        LOGGER.info("execute method Create in UserService, user: user={} and id: id={}", userToCreate, newUserId);

        List<Pet> pets = userToCreate.isPetsEmpty()
                ? new ArrayList<>()
                : petService.create(userToCreate.getPets(), newUserId);
        var newUser = new User(
                idUserCounter,
                userToCreate.getName(),
                userToCreate.getEmail(),
                userToCreate.getAge(),
                pets
        );
        this.userMap.put(newUserId, newUser);
        return newUser;
    }

    public User updateById(Long id, User userToUpdate) {
        LOGGER.info("execute method updateById in UserService, user: user={} and id: id={}", userToUpdate, id);
        User user = Optional.ofNullable(userMap.get(id))
                .orElseThrow(() -> new NoSuchElementException("No such user with id=%s not found"
                        .formatted(id)));

        if (userToUpdate.isPetsEmpty()) {
            user.setName(userToUpdate.getName());
            user.setEmail(userToUpdate.getEmail());
            user.setAge(userToUpdate.getAge());
        } else {
            List<Pet> pets = petService.create(userToUpdate.getPets(), user.getId());
            user.setName(userToUpdate.getName());
            user.setEmail(userToUpdate.getEmail());
            user.setAge(userToUpdate.getAge());
            user.addPets(pets);
        }
        userMap.put(id, user);
        return user;
    }

    public void deleteById(Long id) {
        LOGGER.info("execute method deleteById in UserService, id: id={}", id);
        Optional.ofNullable(userMap.remove(id))
                .orElseThrow(() -> new NoSuchElementException("No such user with id=%s not found"
                        .formatted(id)));
    }

    public List<User> findAll(String name, String email) {
        LOGGER.info("execute method findAll in UserService, for user with name: name={}, email: email={}"
                ,name ,email);
        return  userMap.values()
                .stream()
                .filter(user -> Objects.isNull(name) || user.getName().equals(name))
                .filter(user ->Objects.isNull(email) || user.getEmail().equals(email))
                .toList();
    }

    public User findById(Long id) {
        LOGGER.info("execute method findById in UserService, id: id={}", id);
        return Optional.ofNullable(userMap.get(id))
                .orElseThrow(() -> new NoSuchElementException("No such user with id=%s not found"
                        .formatted(id)));
    }
}
