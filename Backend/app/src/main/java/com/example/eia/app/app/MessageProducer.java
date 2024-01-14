package com.example.eia.app.app;

import com.example.eia.app.app.Monitor.LogMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.example.eia.app.app.CustomObjects.RequestMessage;
import com.example.eia.app.app.UserEntity.UserIdRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Component
public class MessageProducer {

    
    @Autowired
    private JmsTemplate jmsTemplate;

    private RequestMessage requestMessage;
    private final String COMPONENT_NAME = "producer";

    public void sendMessageToTopic(UserIdRequest content) {
        
        String messageId = UUID.randomUUID().toString();
        // requestMessage = new RequestMessage(messageId, content.getSearchTerm(), content.getLatitude(), content.getLongitude());
        List<String> history = new ArrayList<>();
        history.add(COMPONENT_NAME);
        requestMessage = new RequestMessage(messageId, content.getSearchTerm(), history);
        jmsTemplate.convertAndSend("topic.compositeMsg2", requestMessage);
        jmsTemplate.convertAndSend("topic.control-bus", new LogMessage(requestMessage));
    }

}
