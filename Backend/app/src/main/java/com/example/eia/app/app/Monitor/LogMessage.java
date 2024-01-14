package com.example.eia.app.app.Monitor;

import com.example.eia.app.app.CustomObjects.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LogMessage extends Message implements Serializable {
    private String message;
    private Long id;
    private List<String> history = new ArrayList<>();

    public LogMessage(DataResponse message){
        this.type = "Data Response";
        this.message = message.getMessage();
        this.id = message.getUid();
        this.history = message.getHistory();
        this.isTestMessage = message.isTestMessage();
    }

    public LogMessage(RequestMessage message){
        this.type = "Request Message";
        this.message = message.getTerm();
        this.id = 1L;
        this.history = message.getHistory();
        this.isTestMessage = message.isTestMessage();
    }

    public LogMessage(ResponseMessage message){
        this.type = "Response Message";
        this.message = message.getTitle() + " " + message.getVid_url();
        this.history = message.getHistory();
        this.isTestMessage = message.isTestMessage();
    }

    public LogMessage(VideoID message){
        this.type = "Video ID Message";
        this.message = message.getVideo_id();
        this.history = message.getHistory();
        this.isTestMessage = message.isTestMessage();
    }
}



