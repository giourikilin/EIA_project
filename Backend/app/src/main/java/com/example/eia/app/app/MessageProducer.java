package com.example.eia.app.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.example.eia.app.app.CustomObjects.RequestMessage;
import com.example.eia.app.app.UserEntity.UserIdRequest;

import java.util.UUID;


@Component
public class MessageProducer {

    
    @Autowired
    private JmsTemplate jmsTemplate;

    private RequestMessage requestMessage;

    public void sendMessageToTopic(UserIdRequest content) {
        
        String messageId = UUID.randomUUID().toString();
        // requestMessage = new RequestMessage(messageId, content.getSearchTerm(), content.getLatitude(), content.getLongitude());
        requestMessage = new RequestMessage(messageId, content.getSearchTerm());

        jmsTemplate.convertAndSend("topic.compositeMsg2", requestMessage);
    }

}
