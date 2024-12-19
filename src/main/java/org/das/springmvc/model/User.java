package org.das.springmvc.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    //todo change all equals pets // в этом фрагмент идет за дублирование pet объектов при обновлении
    public User addPet(Pet pet) {
        if (pet == null) {
            return this;
        }
        List<Pet> newPets = new ArrayList<>(pets);
        newPets.add(pet);
        return new User(id, name, email, age, List.copyOf(newPets));
    }

    //todo change all equals pets // в этом фрагмент идет за дублирование pet объектов при обновлении
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
}
