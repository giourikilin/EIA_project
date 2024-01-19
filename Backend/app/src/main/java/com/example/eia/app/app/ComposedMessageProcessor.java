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


    // Buffer to temporarily store messages based on their message IDs
    private final Map<String, ResponseMessage> messageBuffer = new HashMap<>();

    // JMS template for sending messages to queues
    @Autowired
    private JmsTemplate jmsTemplate;

    // Component name for logging purposes
    private final String COMPONENT_NAME = "aggregator";

    // Producer to send a message to a specified queue
    public void sendMessageToQueue(Message message, String queueName) {
        jmsTemplate.convertAndSend(queueName, message);
    }


    // Consumer for processing messages from the YouTube consumer queue
    @JmsListener(destination = "from-yt-consumer-queue")
    public void processMessageFromYTconsumer(VideoID message) {
        try {
            System.out.println("Agg got from YT cons "+ message.getVideo_id());

            // Update message history with the aggregator component
            List<String> history = message.getHistory();
            history.add(COMPONENT_NAME);
            message.setHistory(history);

            // Check if the message ID is present in the buffer
            if (messageBuffer.containsKey(message.getMsg_id())){
                 // Update the video URL in the existing message
                messageBuffer.get(message.getMsg_id()).setVid_url(message.getVideo_id());
                // Send the updated message to the controller queue
                sendMessageToQueue(messageBuffer.get(message.getMsg_id()), "from-agg-to-controller-queue");
                // Update message type, test flag, and log the message
                messageBuffer.get(message.getMsg_id()).setType("Video ID message");
                messageBuffer.get(message.getMsg_id()).setTestMessage(message.isTestMessage());
                LogMessage logMessage = new LogMessage(messageBuffer.get(message.getMsg_id()));
                sendMessageToQueue(logMessage,"topic.control-bus");
                // Remove the message from the buffer
                messageBuffer.remove(message.getMsg_id());
            }
            
        } catch (Exception e) {
            System.out.println("Error agg from youtube reading queue");
        }
    }


    // Consumer for processing messages from the aggregator queue
    @JmsListener(destination = "to-aggregator-queue")
    public void processMessageFromQueue(ResponseMessage message) {
        try {
            System.out.println("Agg got from Recipe consumer title"+message.getTitle());
            // Put the message into the buffer with its message ID
            messageBuffer.put(message.getMsg_id(), message);
            // Update message history with the aggregator component
            List<String> history = message.getHistory();
            history.add(COMPONENT_NAME);
            // Create a VideoID object based on the received message
            VideoID videoObj = new VideoID(message.getMsg_id(), message.getTitle(), history);
            videoObj.setType("Video Object message");
            videoObj.setTestMessage(message.isTestMessage());
            // Log the VideoID message
            LogMessage logMessage = new LogMessage(videoObj);
            sendMessageToQueue(logMessage,"topic.control-bus");
            // Send the VideoID message to the YouTube consumer queue
            sendMessageToQueue(videoObj, "to-yt-consumer-queue");
        } catch (Exception e) {
            System.out.println("Error agg reading from recipe queue");
        }
    }
    
}
