package com.example.eia.app.app.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//User Data Transfer Object (DTO) 
public class UserDTO {
    private Long id;
    private String email;
    private String password;
    private String username;
}
