package com.example.eia.app.app.Controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.eia.app.app.MessageProducer;
import com.example.eia.app.app.CustomObjects.DataResponse;
import com.example.eia.app.app.CustomObjects.ResponseMessage;
import com.example.eia.app.app.UserEntity.UserDTO;
import com.example.eia.app.app.UserEntity.UserIdRequest;
import com.example.eia.app.app.UserEntity.UserLoginDTO;
import com.example.eia.app.app.UserEntity.UserService;
import com.example.eia.app.app.UserPosts.PostItem;
import com.example.eia.app.app.UserPosts.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


@RestController
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;

    @Autowired
    private MessageProducer messageProducer;

    private ResponseMessage rM;

    private final CountDownLatch latch = new CountDownLatch(1);


    @JmsListener(destination = "from-agg-to-controller-queue")
    public void processMessageFromAgg(ResponseMessage message) {
        try {
            this.rM = message;
            latch.countDown();
        } catch (Exception e) {
            System.out.println("Error");
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO){
        UserDTO savedUser = userService.saveUser(userDTO);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> loginUserEntity(@RequestBody UserLoginDTO userloginDTO){
        DataResponse dataResponse = userService.loginUser(userloginDTO);
        return ResponseEntity.ok(dataResponse);
    }

    @PostMapping(path = "/loadposts")
    public ResponseEntity<?> loadPostsUser(@RequestBody UserIdRequest userIdRequest) {
        Long user_id = userIdRequest.getUser_id();
        List<PostItem> postItems = postService.getPostsByUid(user_id);
        return ResponseEntity.ok(postItems);
    }

    @PostMapping(path = "/searchPosts")
    public ResponseEntity<?> searchForRecipes(@RequestBody UserIdRequest userIdRequest) {
        String searchString = userIdRequest.getSearchTerm();
        System.out.println(searchString);
        if (searchString != null){
            messageProducer.sendMessageToTopic(userIdRequest);
            List<ResponseMessage> res = new ArrayList<>();
            try {
                latch.await(5, TimeUnit.SECONDS);
                ResponseMessage rp = this.rM;
                res.add(rp);
                rM = null;
                return ResponseEntity.ok(res);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return ResponseEntity.ok(null);
    }

}