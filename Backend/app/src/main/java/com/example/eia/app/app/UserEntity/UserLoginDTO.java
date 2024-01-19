package com.example.eia.app.app.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// User Log in Data Transfer Object (DTO) 
public class UserLoginDTO {
    private String username;
    private String password;
}
