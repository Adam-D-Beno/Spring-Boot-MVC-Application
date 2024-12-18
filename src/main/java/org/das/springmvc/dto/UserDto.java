package org.das.springmvc.dto;

import java.util.List;

public record UserDto(
        Long id,
        String name,
        String email,
        Integer age,
        List<PetDto> pets) {}
