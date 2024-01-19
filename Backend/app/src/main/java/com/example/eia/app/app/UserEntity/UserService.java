package com.example.eia.app.app.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.eia.app.app.CustomObjects.DataResponse;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    // UserRepo for database interaction
    private UserRepo userRepo;

    // Component name for logging purposes
    private final String COMPONENT_NAME = "producer";


    // Service method to save a new user
    public UserDTO saveUser(UserDTO userDTO){
        // Map UserDTO to User entity and save it to the database
        User user = UserMapper.mapToUser(userDTO);
        User saved_user = userRepo.save(user);
        // Map the saved User entity back to UserDTO for response
        return UserMapper.mapToUserDTO(saved_user);
    }

    // Service method to handle user login
    public DataResponse loginUser(UserLoginDTO userLoginDTO){
        // Retrieve user from the database based on the provided username
        User user = userRepo.findByUsername(userLoginDTO.getUsername());
        // Initialize history list for logging
        List<String> history = new ArrayList<String>();
        history.add(COMPONENT_NAME);
         // Check if a user with the provided username exists
        if (user != null){
            String pwd = userLoginDTO.getPassword();
            String usr_pwd = user.getPassword();
             // Compare the provided password with the stored password
            Boolean pwd_match = usr_pwd.matches(pwd);
            if (pwd_match){
                // If password matches, attempt to find the user by both username and password
                Optional<User> userOptional = userRepo.findByUsernameAndPassword(userLoginDTO.getUsername(), usr_pwd);
                if(userOptional.isPresent()){
                    // If user is found, return a success response
                    return new DataResponse("Login Success", userOptional.get().getId(), history);
                } else {
                     // If user is not found, return a response indicating mismatch
                    return new DataResponse("Username or Password do not match", null, history);
                }
            } else {
                // If password does not match, return a response indicating mismatch
                return new DataResponse("Username or Password do not match", null, history);
            }
        } else {
            // If user with the provided username does not exist, return a response indicating the issue
            return new DataResponse("Not a real email", null, history);
        }
    }

}
