package com.example.eia.app.app.CustomObjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {
    protected String type;
    protected boolean isTestMessage;
}
