package com.example.eia.app.app.UserPosts;


import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;
//import org.springframework.data.util.Pair;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collation = "posts")
public class PostItem implements Serializable{

    @Id
    private Long post_ID;

    private Long userID;
    private String title;
    private String picture;
    private List<String> ingredients;
    private String video_url;
    // private Pair<Double, Double> local_map;
}
