package com.example.eia.app.app.Monitor;

import com.example.eia.app.app.CustomObjects.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage extends Message {
    private String error;
    private List<String> history = new ArrayList<>();
}
