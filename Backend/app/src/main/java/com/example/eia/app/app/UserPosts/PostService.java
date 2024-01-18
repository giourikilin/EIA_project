package com.example.eia.app.app.UserPosts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostService {

    @Autowired
    private PostsRepo postsRepo;

    public List<PostItem> getPostsByUid(Long uid){      
        try {
            return postsRepo.findByUserID(uid);   
        } catch(Exception e) {
            return null;
        }    
    }


    public void savePostItem(PostItem postItem) {
        if(postItem != null){
            postsRepo.save(postItem);
        } else {
            System.out.println("Cant save recipe is null");
        }
    }
}
