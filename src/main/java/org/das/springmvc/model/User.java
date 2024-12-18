package org.das.springmvc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record User(
        @Null(message = "Filed user id must be null")
         Long id,

        @Size(max = 50, message = "Filed size user name must be max=50")
        @NotBlank(message = "Filed user name must be not null or blank")
        String name,

        @Size(max = 50, message = "Filed user email must be max=50")
        @Email(message = "Filed user email must be email format")
        @NotBlank(message = "Filed user email must be not null or blank")
        String email,

        @Min(value = 1, message = "Filed value user age must be min=1 and max=100")
        @Max(value = 100, message = "Filed value user age must be min=1 and max=100")
        @NotNull(message = "Filed user age must be not null")
        @Digits(integer = 3, fraction = 0, message = "Filed value user age  is an integer with a maximum of 3 digits")
        Integer age,

        List<Pet> pets
) {
    @JsonIgnore
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
}
