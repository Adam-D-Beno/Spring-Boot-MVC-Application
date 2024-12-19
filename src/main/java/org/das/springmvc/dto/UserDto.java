package org.das.springmvc.dto;
//todo export in main branch
import jakarta.validation.constraints.*;

import java.util.List;

public record UserDto(
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

        List<PetDto> pets) {}
