package com.example.eia.app.app;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.example.eia.app.app.CustomObjects.ResponseMessage;
import com.example.eia.app.app.CustomObjects.VideoID;

@Component
public class Aggregator {

    private final Map<String, ResponseMessage> messageBuffer = new HashMap<>();

    @Autowired
    private JmsTemplate jmsTemplate;

    
    public void sendMessageToYTconsumer(VideoID message, String queueName) {
        jmsTemplate.convertAndSend(queueName, message);
    }

    public void sendMessageToController(ResponseMessage message, String queueName) {
        jmsTemplate.convertAndSend(queueName, message);
    }

    @JmsListener(destination = "from-yt-consumer-queue")
    public void processMessageFromYTconsumer(VideoID message) {
        try {
            System.out.println("Agg got from YT cons "+ message.getVideo_id());
            if (messageBuffer.containsKey(message.getMsg_id())){
                messageBuffer.get(message.getMsg_id()).setVid_url(message.getVideo_id());
                sendMessageToController(messageBuffer.get(message.getMsg_id()), "from-agg-to-controller-queue");
                messageBuffer.remove(message.getMsg_id());
            }
            
        } catch (Exception e) {
            System.out.println("Error agg from youtube reading queue");
        }
    }


    @JmsListener(destination = "to-aggregator-queue")
    public void processMessageFromQueue(ResponseMessage message) {
        try {
            System.out.println("Agg got from Recipe consumer title"+message.getTitle());
            messageBuffer.put(message.getMsg_id(), message);
            VideoID videoObj = new VideoID(message.getMsg_id(), message.getTitle());
            sendMessageToYTconsumer(videoObj, "to-yt-consumer-queue");
        } catch (Exception e) {
            System.out.println("Error agg reading from recipe queue");
        }
    }
    
}
