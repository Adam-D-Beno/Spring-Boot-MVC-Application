package org.das.springmvc.dto;
//todo export in main branch
import org.das.springmvc.model.User;

public class UserMapper implements Mapper <User, UserDto> {

    @Override
    public UserDto toDto(User user) {
        return null;
    }

    @Override
    public User toEntity(UserDto userDto) {
        return null;
    }
}
