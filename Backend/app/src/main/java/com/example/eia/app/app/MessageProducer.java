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

    // JmsTemplate for sending messages to topics
    @Autowired
    private JmsTemplate jmsTemplate;

    // RequestMessage object to be created and sent
    private RequestMessage requestMessage;

    // Component name for logging purposes
    private final String COMPONENT_NAME = "producer";

    // Producer to send a message to the specified topic and control bus
    public void sendMessageToTopic(UserIdRequest content) {
        // Generate a unique message ID using UUID
        String messageId = UUID.randomUUID().toString();

        // Create a list to store message history
        List<String> history = new ArrayList<>();
        history.add(COMPONENT_NAME);
        // Create a RequestMessage with generated ID, content, and history
        requestMessage = new RequestMessage(messageId, content.getSearchTerm(), history);
        // Send the RequestMessage to the composite message topic
        jmsTemplate.convertAndSend("topic.compositeMsg2", requestMessage);
        // Send a LogMessage to the control bus topic for logging purposes
        jmsTemplate.convertAndSend("topic.control-bus", new LogMessage(requestMessage));
    }

}
