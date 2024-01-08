package com.example.eia.app.app.UserEntity;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.eia.app.app.CustomObjects.DataResponse;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepo userRepo;

    public UserDTO saveUser(UserDTO userDTO){
        User user = UserMapper.mapToUser(userDTO);
        User saved_user = userRepo.save(user);
        return UserMapper.mapToUserDTO(saved_user);
    }

    public DataResponse loginUser(UserLoginDTO userLoginDTO){
        User user = userRepo.findByUsername(userLoginDTO.getUsername());
        if (user != null){
            String pwd = userLoginDTO.getPassword();
            String usr_pwd = user.getPassword();
            Boolean pwd_match = usr_pwd.matches(pwd);
            if (pwd_match){
                Optional<User> userOptional = userRepo.findByUsernameAndPassword(userLoginDTO.getUsername(), usr_pwd);
                if(userOptional.isPresent()){
                    return new DataResponse("Login Success", userOptional.get().getId());
                } else {
                    return new DataResponse("Username or Password do not match", null);
                }
            } else {
                return new DataResponse("Username or Password do not match", null);
            }
        } else {
            return new DataResponse("Not a real email", null);
        }
    }

}
