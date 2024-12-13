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

    public PetService() {
        this.petMap = new HashMap<>();;
    }

    public Pet create(Pet petToCreate) {
        return null;
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
