package org.das.springmvc.dto;
//todo export in main branch
import org.das.springmvc.model.Pet;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PetMapper implements Mapper<Pet, PetDto> {

    @Override
    public PetDto toDto(Pet pet) {
        return new PetDto(
                pet.id(),
                pet.name(),
                pet.userId()
        );
    }

    @Override
    public List<PetDto> toDto(List<Pet> pets) {
        return pets.stream().map(this::toDto).toList();
    }

    @Override
    public Pet toEntity(PetDto petDto) {
        return new Pet(
                petDto.id(),
                petDto.name(),
                petDto.userId()
        );
    }

}
