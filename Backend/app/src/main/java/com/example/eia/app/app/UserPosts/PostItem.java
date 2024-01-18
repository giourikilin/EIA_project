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
@Document(collection = "posts")
public class PostItem implements Serializable{

    @Id
    private Long post_ID;
    private Long userID;
    private String title;
    private String pic;
    private List<String> ing;
    private String vid_url;
}
