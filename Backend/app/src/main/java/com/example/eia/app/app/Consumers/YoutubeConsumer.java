package com.example.eia.app.app.Consumers;
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

@Component
public class YoutubeConsumer {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JmsTemplate jmsTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ResponseEntity<?> makeYouTubeCall(String query){
        String apiUrl = "https://www.googleapis.com/youtube/v3/search?key=AIzaSyAEIWIXKu0QWUP83yWI5FoDJ2xtsCmRYm0&part=snippet&type=video&q="+query;
        String response = restTemplate.getForObject(apiUrl, String.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    public void sendMessageToAggregatorQ2(VideoID message, String queueName) {
        jmsTemplate.convertAndSend(queueName, message);
    }


    
    @JmsListener(destination = "to-yt-consumer-queue")
    public void processMessageFromQueue(VideoID message) {
        try {
            ResponseEntity<?> videoResponse = makeYouTubeCall(message.getVideo_id().replaceAll("[\\s?.,@$&]+", ""));
            
            if (videoResponse.getStatusCode() == HttpStatus.OK) {
                String respent = (String) videoResponse.getBody();
                JsonNode jsonNode = objectMapper.readTree(respent);
                String videoID = jsonNode.get("items").get(0).get("id").get("videoId").asText();
                VideoID videoObj = new VideoID(message.getMsg_id(),videoID);
                System.out.println("Youtube consumer made call and got "+videoID);
                sendMessageToAggregatorQ2(videoObj, "from-yt-consumer-queue");
            }
        } catch (Exception e) {
            System.out.println("Error");
        }
        
    }
}
