package com.example.eia.app.app.UserPosts;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface PostsRepo extends MongoRepository<PostItem, Long> {
    List<PostItem> findByUserID(Long uid);
}
