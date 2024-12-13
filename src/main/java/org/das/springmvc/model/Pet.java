package org.das.springmvc.model;

import jakarta.validation.constraints.*;

import java.util.Objects;

public class Pet {
        @Null(message = "Filed user id must be null")
       private final Long id;

        @Size(max = 50, message = "Filed size user name must be max=50")
        @NotBlank(message = "Filed user name must be not null or blank")
        private final String name;

        @NotNull(message = "Filed user age must be not null")
        @Digits(integer = Integer.MAX_VALUE, fraction = 0,
                message = "Filed value user age  is an integer with a maximum of 3 digits")
        private final Long userId;

        public Pet(Long id, String name, Long userId) {
                this.id = id;
                this.name = name;
                this.userId = userId;
        }

        public Long getId() {
                return id;
        }

        public String getName() {
                return name;
        }

        public Long getUserId() {
                return userId;
        }

        @Override
        public String toString() {
                return "Pet{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                        ", userId=" + userId +
                        '}';
        }

        @Override
        public boolean equals(Object o) {
                if (o == null || getClass() != o.getClass()) return false;

                Pet pet = (Pet) o;
                return Objects.equals(id, pet.id) && Objects.equals(name, pet.name) && Objects.equals(userId, pet.userId);
        }

        @Override
        public int hashCode() {
                int result = Objects.hashCode(id);
                result = 31 * result + Objects.hashCode(name);
                result = 31 * result + Objects.hashCode(userId);
                return result;
        }
}
