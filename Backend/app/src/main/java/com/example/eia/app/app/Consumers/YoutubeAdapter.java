package com.example.eia.app.app.Consumers;
import com.example.eia.app.app.CustomObjects.Message;
import com.example.eia.app.app.Monitor.LogMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;

import com.example.eia.app.app.CustomObjects.VideoID;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@Component
public class YoutubeAdapter {

    String API_KEY = "AIzaSyB5OZ6TXEppa026Ght1JcwOlnX7ZGvdgOQ";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JmsTemplate jmsTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String COMPONENT_NAME = "youtube adapter";

    public ResponseEntity<?> makeYouTubeCall(String query) {
        try {
            String apiUrl = "https://www.googleapis.com/youtube/v3/search?key="+API_KEY+"&part=snippet&type=video&q="+query;
            String response = restTemplate.getForObject(apiUrl, String.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to make YouTube call", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public void sendMessageToQueue(Message message, String queueName) {
        jmsTemplate.convertAndSend(queueName, message);
    }


    
    @JmsListener(destination = "to-yt-consumer-queue")
    public void processMessageFromQueue(VideoID message) {
        try {
            String processed_title = message.getVideo_id().replaceAll("[\\s?.,@$&]+", "");
            ResponseEntity<?> videoResponse = makeYouTubeCall(processed_title);
            System.out.println(processed_title);
            
            
            if (videoResponse.getStatusCode() == HttpStatus.OK) {
                System.out.println(videoResponse.getStatusCode());
                String respent = (String) videoResponse.getBody();
                System.out.println(respent);
                JsonNode jsonNode = objectMapper.readTree(respent);
                String videoID = jsonNode.get("items").get(0).get("id").get("videoId").asText();
                List<String> history = message.getHistory();
                history.add(COMPONENT_NAME);
                VideoID videoObj = new VideoID(message.getMsg_id(),videoID, history);
                videoObj.setTestMessage(message.isTestMessage());
                videoObj.setType("VideoID Message");
                System.out.println("Youtube consumer made call and got "+videoID);
                sendMessageToQueue(videoObj, "from-yt-consumer-queue");
                sendMessageToQueue(new LogMessage(videoObj), "topic.control-bus");

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Error youtube adapter");
        }
        
    }
}
