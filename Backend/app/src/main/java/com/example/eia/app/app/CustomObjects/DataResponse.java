package com.example.eia.app.app.CustomObjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataResponse extends Message{
    private String message;
    private Long uid;
    private List<String> history = new ArrayList<>();
}
