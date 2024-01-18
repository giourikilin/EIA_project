package com.example.eia.app.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.eia.app.app.CustomObjects.Message;
import com.example.eia.app.app.Monitor.LogMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.example.eia.app.app.CustomObjects.ResponseMessage;
import com.example.eia.app.app.CustomObjects.VideoID;

@Component
public class ComposedMessageProcessor {

    private final Map<String, ResponseMessage> messageBuffer = new HashMap<>();

    @Autowired
    private JmsTemplate jmsTemplate;

    private final String COMPONENT_NAME = "aggregator";
    public void sendMessageToQueue(Message message, String queueName) {
        jmsTemplate.convertAndSend(queueName, message);
    }


    @JmsListener(destination = "from-yt-consumer-queue")
    public void processMessageFromYTconsumer(VideoID message) {
        try {
            System.out.println("Agg got from YT cons "+ message.getVideo_id());
            List<String> history = message.getHistory();
            history.add(COMPONENT_NAME);
            message.setHistory(history);
            if (messageBuffer.containsKey(message.getMsg_id())){
                messageBuffer.get(message.getMsg_id()).setVid_url(message.getVideo_id());
                sendMessageToQueue(messageBuffer.get(message.getMsg_id()), "from-agg-to-controller-queue");
                messageBuffer.get(message.getMsg_id()).setType("Video ID message");
                messageBuffer.get(message.getMsg_id()).setTestMessage(message.isTestMessage());
                LogMessage logMessage = new LogMessage(messageBuffer.get(message.getMsg_id()));
                sendMessageToQueue(logMessage,"topic.control-bus");
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
            List<String> history = message.getHistory();
            history.add(COMPONENT_NAME);
            VideoID videoObj = new VideoID(message.getMsg_id(), message.getTitle(), history);
            videoObj.setType("Video Object message");
            videoObj.setTestMessage(message.isTestMessage());
            LogMessage logMessage = new LogMessage(videoObj);
            sendMessageToQueue(logMessage,"topic.control-bus");
            sendMessageToQueue(videoObj, "to-yt-consumer-queue");
        } catch (Exception e) {
            System.out.println("Error agg reading from recipe queue");
        }
    }
    
}
