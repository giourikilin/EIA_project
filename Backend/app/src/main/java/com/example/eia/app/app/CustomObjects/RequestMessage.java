package com.example.eia.app.app.CustomObjects;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestMessage implements Serializable{
    
    private String id;
    private String term;
    // private double longitude;
    // private double latitude;
}
