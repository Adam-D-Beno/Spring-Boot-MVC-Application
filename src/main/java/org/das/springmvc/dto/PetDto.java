package org.das.springmvc.dto;

public record PetDto(
        Long id,
        String name,
        Long userId
) {}
