package com.example.eia.app.app.CustomObjects;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestMessage extends Message implements Serializable{
    
    private String id;
    private String term;
    private List<String> history;
    // private double longitude;
    // private double latitude;
}
