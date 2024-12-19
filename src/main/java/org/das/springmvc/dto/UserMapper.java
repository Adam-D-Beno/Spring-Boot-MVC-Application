package org.das.springmvc.dto;

import org.das.springmvc.model.Pet;
import org.das.springmvc.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper implements Mapper <User, UserDto> {

    private final PetMapper petMapper;

    public UserMapper(PetMapper petMapper) {
        this.petMapper = petMapper;
    }

    @Override
    public UserDto toDto(User user) {
        return new UserDto(
                user.id(),
                user.name(),
                user.email(),
                user.age(),
                toDtos(user)
        );
    }

    @Override
    public List<UserDto> toDto(List<User> users) {
        return users.stream().map(this::toDto).toList();
    }

    @Override
    public User toEntity(UserDto userDto) {
        return new User(
                userDto.id(),
                userDto.name(),
                userDto.email(),
                userDto.age(),
                toPets(userDto)
        );
    }

    private List<Pet> toPets(UserDto userDto) {
        if (userDto.pets() == null || userDto.pets().isEmpty()) {
            return new ArrayList<>();
        }
        return userDto.pets().stream().map(petMapper::toEntity).toList();
    }

    private List<PetDto> toDtos(User user) {
        if (user.pets() == null || user.pets().isEmpty()) {
            return new ArrayList<>();
        }
        return user.pets().stream().map(petMapper::toDto).toList();
    }
}
