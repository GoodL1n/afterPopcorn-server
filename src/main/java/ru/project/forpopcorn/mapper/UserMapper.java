package ru.project.forpopcorn.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.project.forpopcorn.dto.UserDTO;
import ru.project.forpopcorn.entity.User;

@Component
public class UserMapper {
    private final ModelMapper mapper;

    public UserMapper() {
        this.mapper = new ModelMapper();
    }

    public User convertToUser(UserDTO userDTO){
        return mapper.map(userDTO, User.class);
    }

    public UserDTO convertToUserDTO(User user){
        return mapper.map(user, UserDTO.class);
    }
}
