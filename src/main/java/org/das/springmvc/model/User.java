package org.das.springmvc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class User {
    @Null(message = "Filed user id must be null")
    private Long id;

    @Size(max = 50, message = "Filed size user name must be max=50")
    @NotBlank(message = "Filed user name must be not null or blank")
    private String name;

    @Size(max = 50, message = "Filed user email must be max=50")
    @Email(message = "Filed user email must be email format")
    @NotBlank(message = "Filed user email must be not null or blank")
    private String email;

    @Min(value = 1, message = "Filed value user age must be min=1 and max=100")
    @Max(value = 100, message = "Filed value user age must be min=1 and max=100")
    @NotNull(message = "Filed user age must be not null")
    @Digits(integer = 3, fraction = 0, message = "Filed value user age  is an integer with a maximum of 3 digits")
    private Integer age;

    private List<Pet> pets;

    public User(Long id, String name, String email, Integer age, List<Pet> pets) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.pets = pets;
    }

    public User() {
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

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @JsonIgnore
    public boolean isPetsEmpty() {
        return pets == null || pets.isEmpty();
    }

    public void addPet(Pet pet) {
        List<Pet> mutList = new ArrayList<>(this.getPets());
        mutList.add(pet);
        this.pets = List.copyOf(mutList);
    }

    public void addPets(List<Pet> pets) {
        List<Pet> mutList = new ArrayList<>(this.getPets());
        mutList.addAll(pets);
        this.pets = List.copyOf(mutList);
    }


    public void removePet(Pet pet) {
        List<Pet> mutList = new ArrayList<>(this.getPets());
        mutList.remove(pet);
        this.pets = List.copyOf(mutList);
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
        return Objects.equals(name, user.name) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(email);
        return result;
    }
}
