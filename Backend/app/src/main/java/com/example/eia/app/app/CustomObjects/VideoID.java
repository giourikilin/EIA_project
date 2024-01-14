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
public class VideoID extends Message implements Serializable{
    private String msg_id;
    private String video_id;
    private List<String> history;
}
