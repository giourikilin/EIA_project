package com.example.eia.app.app.Monitor;

import com.example.eia.app.app.Controllers.Controller;
import com.example.eia.app.app.CustomObjects.Message;
import com.example.eia.app.app.CustomObjects.RequestMessage;
import com.example.eia.app.app.CustomObjects.ResponseMessage;
import com.example.eia.app.app.CustomObjects.VideoID;
import com.example.eia.app.app.UserEntity.UserIdRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TestMessageService {
    @Autowired
    private JmsTemplate jmsTemplate;

    public TestDTO sendMessage(TestDTO testDTO){
        String destination = testDTO.getComponent();
        switch (destination){
            case "producer":
                testProducer();
                break;
            case "monitor adapter":
                testMonitorAdapter();
                break;
            case "recipe adapter":
                testRecipeAdapter();
                break;
            case "youtube adapter":
                testYoutubeAdapter();
                break;
            case "aggregator":
                testAggregator();
                break;
        }
        return testDTO;
    }

    public void sendMessageToQueue(Message message, String queueName) {
        jmsTemplate.convertAndSend(queueName, message);
    }

    public void testProducer(){
//        // create test message
//        UserIdRequest request = new UserIdRequest(0L, "chicken");
//        // send message
//        controller.searchForRecipes(request);
    }

    public void testMonitorAdapter(){
        // create test message
        LogMessage request = new LogMessage("monitor test message",0L, new ArrayList<>() );
        request.setTestMessage(true);
        // send message
        System.out.println("Testing Monitor Adapter");
        try{
            sendMessageToQueue(request, "topic.control-bus");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void testRecipeAdapter(){
        RequestMessage message = new RequestMessage(UUID.randomUUID().toString(), "chicken" ,new ArrayList<>());
        message.setTestMessage(true);

        try{
            sendMessageToQueue(message, "topic.compositeMsg2");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void testYoutubeAdapter(){
        VideoID message = new VideoID(UUID.randomUUID().toString(), "Sk0dgp63YoY" ,new ArrayList<>());
        message.setTestMessage(true);

        try{
            sendMessageToQueue(message, "to-yt-consumer-queue");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void testAggregator(){
//        ResponseMessage message = new ResponseMessage(UUID.randomUUID().toString(), "Chicken Salad" );
//        message.setTestMessage(true);
//
//        try{
//            sendMessageToQueue(message, "to-yt-consumer-queue");
//        } catch (Exception e){
//            System.out.println(e.getMessage());
//        }
    }
}
