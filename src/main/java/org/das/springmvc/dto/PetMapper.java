package org.das.springmvc.dto;

import org.das.springmvc.model.Pet;

public class PetMapper implements Mapper<Pet, PetDto> {

    @Override
    public PetDto toDto(Pet pet) {
        return null;
    }

    @Override
    public Pet toEntity(PetDto petDto) {
        return null;
    }
}
