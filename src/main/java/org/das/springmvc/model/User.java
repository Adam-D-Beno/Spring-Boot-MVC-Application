package org.das.springmvc.model;

import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    @Null(message = "Filed user id must be null")
    private final Long id;

    @Size(max = 50, message = "Filed size user name must be max=50")
    @NotBlank(message = "Filed user name must be not null or blank")
    private final String name;

    @Size(max = 50, message = "Filed user email must be max=50")
    @Email(message = "Filed user email must be email format")
    @NotBlank(message = "Filed user email must be not null or blank")
    private final String email;

    @Min(value = 1, message = "Filed value user age must be min=1 and max=100")
    @Max(value = 100, message = "Filed value user age must be min=1 and max=100")
    @NotNull(message = "Filed user age must be not null")
    @Digits(integer = 3, fraction = 0, message = "Filed value user age  is an integer with a maximum of 3 digits")
    private Integer age;

    private final List<Pet> pets;

    public User(Long id, String name, String email, Integer age, List<Pet> pets) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.pets = pets;
    }

        public Long getId() {
                return id;
        }

        public String getName() {
                return name;
        }

        public String getEmail() {
                return email;
        }

        public Integer getAge() {
                return age;
        }

        public List<Pet> getPets() {
                return List.copyOf(pets);
        }

        public boolean addPet(Pet pet) {
        return pets.add(pet);
        }

        public boolean removePet(Pet pet) {
        return pets.remove(pet);
        }

        @Override
        public String toString() {
                return "User{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                        ", email='" + email + '\'' +
                        ", age=" + age +
                        ", pets=" + pets +
                        '}';
        }

        @Override
        public boolean equals(Object o) {
                if (o == null || getClass() != o.getClass()) return false;

                User user = (User) o;
                return Objects.equals(id, user.id) && Objects.equals(name, user.name);
        }

        @Override
        public int hashCode() {
                int result = Objects.hashCode(id);
                result = 31 * result + Objects.hashCode(name);
                return result;
        }
}
