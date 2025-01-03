package org.das.springmvc.service;

import org.das.springmvc.model.Pet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PetService {

    private final Map<Long, Pet> petMap;
    private final AtomicLong idPetCounter;
    private final static Logger LOGGER = LoggerFactory.getLogger(PetService.class);

    public PetService() {
        this.petMap = new HashMap<>();
        this.idPetCounter = new AtomicLong();
    }

    public Pet create(Pet petToCreate) {
        var newPetId = idPetCounter.incrementAndGet();
        LOGGER.info("execute method Create in PetService, pet: pet={} and id: id={}", petToCreate, newPetId);
        var newPet = new Pet(
                newPetId, petToCreate.name(), petToCreate.userId()
        );
        this.petMap.put(newPetId, newPet);
        return newPet;
    }

    public List<Pet> create(List<Pet> pets, Long userId) {
        LOGGER.info("execute method Create in PetService, pets: pets={}", pets);
        return pets.stream()
                .map(pet -> new Pet(idPetCounter.incrementAndGet(), pet.name(), userId))
                .peek(pet -> this.petMap.put(pet.id(), pet))
                .toList();
    }

    public Pet updateById(Long id, Pet petToUpdate) {
        LOGGER.info("execute method updateById in PetService, pet: pet={} and id: id={}", petToUpdate, id);
        Pet pet = Optional.ofNullable(petMap.get(id))
                .orElseThrow(() -> new NoSuchElementException("No such pet with id=%s"
                        .formatted(id)));

        Pet newPet = new Pet(
                pet.id(),
                petToUpdate.name(),
                petToUpdate.userId()
        );

        petMap.put(id, newPet);
        return pet;
    }


    public List<Pet> updateById(List<Pet> petsToUpdate) {
        LOGGER.info("execute method updateById in PetService, pets: pets={}", petsToUpdate);
        List<Pet> pets = petsToUpdate.stream()
                .filter(pet -> petMap.containsKey(pet.id()))
                .filter(pet -> pet.userId() != null)
                .peek(pet -> petMap.put(pet.id(), pet))
                .toList();
        if (pets.isEmpty()) {
          throw new NoSuchElementException("No exist List pets = %s"
                  .formatted(petsToUpdate));
        }
        return pets;
    }

    public Pet deleteById(Long id) {
        LOGGER.info("execute method deleteById in PetService, PetId: id={}", id);
        return Optional.ofNullable(petMap.remove(id))
                .orElseThrow(() -> new NoSuchElementException("No such pet with id=%s"
                        .formatted(id)));
    }

    public void deleteById(List<Pet> pets) {
        LOGGER.info("execute method deleteById in PetService, Pets: pets={}", pets);
        pets.stream()
                .filter(pet -> pet.userId() != null)
                .forEach(pet -> petMap.remove(pet.id()));
    }

    public List<Pet> findAll(String name, Long userId) {
        LOGGER.info("execute method findAll in PetService, for pet with name: name={}, userId: userId={}"
                , name, userId);
        return petMap.values()
                .stream()
                .filter(pet -> Objects.isNull(name) || pet.name().equals(name))
                .filter(pet -> Objects.isNull(userId) || pet.userId().equals(userId))
                .toList();
    }

    public Pet findById(Long id) {
        LOGGER.info("execute method findById in PetService, for pet with id: id={}", id);
        return Optional.ofNullable(petMap.get(id))
                .orElseThrow(() -> new NoSuchElementException("No such pet with id=%s"
                        .formatted(id)));
    }
}
