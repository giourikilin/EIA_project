package com.example.eia.app.app.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserIdRequest{
    private Long user_id;
    private String searchTerm;
    // private double longitude;
    // private double latitude;

}