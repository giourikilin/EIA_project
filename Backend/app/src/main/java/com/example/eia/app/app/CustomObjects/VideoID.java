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
public class VideoID implements Serializable{
    private String msg_id;
    private String video_id;   
}
