package org.das.springmvc.model;

import java.util.ArrayList;
import java.util.List;

public record User(
        Long id,
        String name,
        String email,
        Integer age,
        List<Pet> pets
) {
    public boolean isPetsEmpty() {
        return pets == null || pets.isEmpty();
    }

    public User addPet(Pet pet) {
        if (pet == null) {
            return this;
        }
        List<Pet> newPets = new ArrayList<>(pets);
        newPets.add(pet);
        return new User(id, name, email, age, List.copyOf(newPets));
    }

    public User addPets(List<Pet> petsToAdd) {
        if (petsToAdd == null || petsToAdd.isEmpty()) {
            return this;
        }
        List<Pet> newPets = new ArrayList<>(pets());
        newPets.addAll(petsToAdd);
        return new User(id, name, email, age, List.copyOf(newPets));
    }

    public User removePet(Pet pet) {
        List<Pet> newPets = new ArrayList<>(pets);
        newPets.remove(pet);

        return new User(id, name, email, age, List.copyOf(newPets));
    }

    public User removePets(List<Pet> petsToRemove) {
        List<Pet> updatedPets = new ArrayList<>(pets);
        petsToRemove.forEach(petToRemove ->
                updatedPets.removeIf(currentPet -> currentPet.id().equals(petToRemove.id()))
        );
        return new User(id, name, email, age, List.copyOf(updatedPets));
    }
}
