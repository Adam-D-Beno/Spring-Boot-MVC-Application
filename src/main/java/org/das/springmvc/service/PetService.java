package org.das.springmvc.service;


import org.das.springmvc.model.Pet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PetService {

    private final Map<Long, Pet> petMap;
    private Long idPetCounter;
    private final static Logger LOGGER = LoggerFactory.getLogger(PetService.class);

    public PetService() {
        this.petMap = new HashMap<>();
        this.idPetCounter = 0L;
    }

    public Pet create(Pet petToCreate) {
        var newPetId = ++idPetCounter;
        LOGGER.info("execute method Create in PetService, pet: pet={} and id: id={}", petToCreate, newPetId);
        var newPet = new Pet(
                newPetId, petToCreate.getName(), petToCreate.getUserId()
        );
        this.petMap.put(newPetId, newPet);
        return newPet;
    }

    //todo check exist user
    public List<Pet> create(List<Pet> pets, Long userId) {
        LOGGER.info("execute method Create in PetService, pets: pets={}", pets);
        return pets.stream()
                .map(pet -> new Pet(++idPetCounter, pet.getName(), userId))
                .toList();
    }

    public Pet updateById(Long id, Pet petToUpdate) {
        LOGGER.info("execute method updateById in PetService, pet: pet={} and id: id={}", petToUpdate, id);
        Pet pet = Optional.ofNullable(petMap.get(id))
                .orElseThrow(() -> new NoSuchElementException("No such pet with id=%s not found"
                        .formatted(id)));

        pet.setName(petToUpdate.getName());
        pet.setUserId(petToUpdate.getUserId());

        petMap.put(id, pet);
        return pet;
    }

    public Pet deleteById(Long id) {

        LOGGER.info("execute method deleteById in PetService, PetId: id={}", id);
        return Optional.ofNullable(petMap.remove(id))
                .orElseThrow(() -> new NoSuchElementException("No such pet with id=%s not found"
                        .formatted(id)));
//        if (pet.getUserId() != null) {
//            LOGGER.info("execute method findById and removePet in UserService, userId: id={}, pet: pet={}"
//                    , pet.getUserId(), pet);
//            userService.findById(pet.getUserId()).removePet(pet);
//        }
    }

    public List<Pet> findAll(String name, Long userId) {
        LOGGER.info("execute method findAll in PetService, for pet with name: name={}, userId: userId={}"
                , name, userId);
        return petMap.values()
                .stream()
                .filter(pet -> Objects.isNull(name) || pet.getName().equals(name))
                .filter(pet -> Objects.isNull(userId) || pet.getUserId().equals(userId))
                .toList();
    }

    public Pet findById(Long id) {
        LOGGER.info("execute method findById in PetService, for pet with id: id={}", id);
        return Optional.ofNullable(petMap.get(id))
                .orElseThrow(() -> new NoSuchElementException("No such pet with id=%s not found"
                        .formatted(id)));
    }
}
