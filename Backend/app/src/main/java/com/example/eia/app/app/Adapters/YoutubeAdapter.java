package com.example.eia.app.app.Adapters;
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

    // YouTube API key for making API requests
    String API_KEY = "AIzaSyB5OZ6TXEppa026Ght1JcwOlnX7ZGvdgOQ";

    // Class provided by Spring for making HTTP requests and handling responses
    @Autowired
    private RestTemplate restTemplate;

    // Hellper class provided by Spring for sending messages to a queue or topic
    @Autowired
    private JmsTemplate jmsTemplate;

     // ObjectMapper for JSON processing
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Component name
    private final String COMPONENT_NAME = "youtube adapter";

    // Makes a YouTube API call to search for videos based on a query
    public ResponseEntity<?> makeYouTubeCall(String query) {
        try {
            // Construct the YouTube API URL with the provided API key and search query
            String apiUrl = "https://www.googleapis.com/youtube/v3/search?key="+API_KEY+"&part=snippet&type=video&q="+query;
            // Make an HTTP GET request to the YouTube API and get result
            String response = restTemplate.getForObject(apiUrl, String.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to make YouTube call", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Message Producer: Sends a message to a JMS queue
    public void sendMessageToQueue(Message message, String queueName) {
        jmsTemplate.convertAndSend(queueName, message);
    }


    // Message Consumer: Listens to a JMS queue ("to-yt-consumer-queue") and processes incoming messages
    @JmsListener(destination = "to-yt-consumer-queue")
    public void processMessageFromQueue(VideoID message) {
        System.out.println("Youtube Adapter received message");
        try {
            // Process the received message title and make a YouTube API call
            String processed_title = message.getVideo_id().replaceAll("[\\s?.,@$&]+", "");
            ResponseEntity<?> videoResponse = makeYouTubeCall(processed_title);
            
            if (videoResponse.getStatusCode() == HttpStatus.OK) {
                // Parse the response from the YouTube API
                String respent = (String) videoResponse.getBody();
                JsonNode jsonNode = objectMapper.readTree(respent);
                
                // Extract the video ID from the API response
                String videoID = jsonNode.get("items").get(0).get("id").get("videoId").asText();
                
                // Update the message history and create a new VideoID message
                List<String> history = message.getHistory();
                history.add(COMPONENT_NAME);
                VideoID videoObj = new VideoID(message.getMsg_id(),videoID, history);
                videoObj.setTestMessage(message.isTestMessage());
                videoObj.setType("VideoID Message");

                // Log the processed information and send messages to JMS queues
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
