package org.das.springmvc.dto;


//todo export in main branch

import jakarta.validation.constraints.*;

public record PetDto(
        @Null(message = "Filed user id must be null")
        Long id,

        @Size(max = 50, message = "Filed size user name must be max=50")
        @NotBlank(message = "Filed user name must be not null or blank")
        String name,

        @NotNull(message = "Filed user age must be not null")
        @Digits(integer = Integer.MAX_VALUE, fraction = 0,
                message = "Filed value userId is an integer with a maximum")
        Long userId
) {}
