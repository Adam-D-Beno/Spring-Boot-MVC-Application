package org.das.springmvc.service;

import jakarta.annotation.PostConstruct;
import org.das.springmvc.model.Pet;
import org.das.springmvc.model.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PetService {

    private final Map<Long, Pet> petMap;
    private Long idPetCounter;
    public PetService() {
        this.petMap = new HashMap<>();
        this.idPetCounter = 0L;
    }

    public Pet create(Pet petToCreate) {
        var newPetId = ++idPetCounter;
        var newPet = new Pet(
                newPetId, petToCreate.name(),petToCreate.userId()
        );
        this.petMap.put(newPetId, newPet);
        return newPet;
    }
    public List<Pet> create(List<Pet> pets) {
       return pets.stream()
                .map(pet -> new Pet(++idPetCounter, pet.name(), pet.userId()))
                .toList();

    }

    public Pet updateById(Long id, Pet petToUpdate) {
        return null;
    }

    public void deleteById(Long id) {
    }

    public List<Pet> findAll(String name, Long userId) {
        return null;
    }

    public Pet findById(Long id) {
        return null;
    }
}
