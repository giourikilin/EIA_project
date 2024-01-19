package com.example.eia.app.app.Controllers;

import com.example.eia.app.app.Monitor.TestDTO;
import com.example.eia.app.app.Monitor.TestMessageService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@CrossOrigin(value = "*")
@RestController
public class Controller {

    // Autowired services and components
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private TestMessageService testMessageService;
    @Autowired
    private MessageProducer messageProducer;

    // Response message received from the aggregator
    private ResponseMessage rM;

    // CountDownLatch for synchronizing asynchronous operations
    private final CountDownLatch latch = new CountDownLatch(6);



    // Consumer for processing messages from the aggregator
    @JmsListener(destination = "from-agg-to-controller-queue")
    public void processMessageFromAgg(ResponseMessage message) {
        try {
            // Set the received response message and decrement the latch count
            this.rM = message;
            latch.countDown();
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    // Endpoint to produce a test message
    @PostMapping("/test")
    public ResponseEntity<?> produceTestMessage(@RequestBody TestDTO testDTO){
        TestDTO test = testMessageService.sendMessage(testDTO);
        return ResponseEntity.ok(test);
    }

    // Endpoint to create a new user
    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO){
        UserDTO savedUser = userService.saveUser(userDTO);
        return ResponseEntity.ok(savedUser);
    }


    // Endpoint to login a user
    @PostMapping(path = "/login")
    public ResponseEntity<?> loginUserEntity(@RequestBody UserLoginDTO userloginDTO){
        DataResponse dataResponse = userService.loginUser(userloginDTO);
        return ResponseEntity.ok(dataResponse);
    }


    // Endpoint to load posts for a user
    @PostMapping(path = "/loadposts")
    public ResponseEntity<?> loadPostsUser(@RequestBody UserIdRequest userIdRequest) {
        Long user_id = userIdRequest.getUser_id();
        try {
            // Retrieve posts for the specified user
            List<PostItem> postItems = postService.getPostsByUid(user_id);
            return ResponseEntity.ok(postItems);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrive recipes");
        }
    }

    // Endpoint to save a user's post
    @PostMapping(path = "/savePost")
    public ResponseEntity<?> saveUserPost(@RequestBody PostItem postItem){
        try {
            // Save the specified post for the user
            postService.savePostItem(postItem);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save PostItem");
        }
    }


    // Endpoint to search for recipes and retrieve response messages
    @PostMapping(path = "/searchPosts")
    public ResponseEntity<?> searchForRecipes(@RequestBody UserIdRequest userIdRequest) {
        String searchString = userIdRequest.getSearchTerm();
        System.out.println(searchString);
        if (searchString != null){
            messageProducer.sendMessageToTopic(userIdRequest);
            List<ResponseMessage> res = new ArrayList<>();
            try {
                latch.await(6, TimeUnit.SECONDS);
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