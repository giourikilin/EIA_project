package com.example.eia.app.app.UserEntity;

public class UserMapper {
    public static UserDTO mapToUserDTO(User user){
        return new UserDTO(
            user.getId(),
            user.getEmail(),
            user.getPassword(),
            user.getUsername()
        );
    }

    public static User mapToUser(UserDTO userDTO){
        return new User(
            userDTO.getId(),
            userDTO.getEmail(),
            userDTO.getPassword(),
            userDTO.getUsername()
        );
    }
}
