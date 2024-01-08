package com.example.eia.app.app.CustomObjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class ResponseMessage implements Serializable{
    private String msg_id;
    private String title;
    private String pic;
    private List<String> ing;
    private String vid_url;
}
