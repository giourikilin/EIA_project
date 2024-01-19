package com.example.eia.app.app.Adapters;

import java.util.ArrayList;
import java.util.List;

import com.example.eia.app.app.CustomObjects.Message;
import com.example.eia.app.app.Monitor.LogMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.eia.app.app.CustomObjects.RequestMessage;
import com.example.eia.app.app.CustomObjects.ResponseMessage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



@Component
public class RecipeAdapter {

    // Edamam API-KEY
    String API_KEY = "33c1f00f5bb8d915ca8b18bbaa2f7a18";

    // Class provided by Spring for making HTTP requests and handling responses
    @Autowired
    private RestTemplate restTemplate;


    // Hellper class provided by Spring for sending messages to a queue or topic
    @Autowired
    private JmsTemplate jmsTemplate;

    // Response message object to be populated
    private ResponseMessage responseMessage;

    // ObjectMapper for JSON processing
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Component name
    private final String COMPONENT_NAME = "recipe adapter";


    // Message Producer: Sends a message to a JMS queue
    public void sendMessageToQueue(Message message, String queueName) {
        jmsTemplate.convertAndSend(queueName, message);
    }


    // API call to Edamam based on a query
    public ResponseEntity<?> makeRecipeCall(String query){
        String apiUrl = "https://api.edamam.com/api/recipes/v2?type=public&beta=false&q="+ query +"&app_id=6f997404&app_key="+API_KEY;
        String response = restTemplate.getForObject(apiUrl, String.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    

    // Message Consumer: Listens to a JMS topic ("topic.compositeMsg2") and processes incoming messages
    @JmsListener(destination = "topic.compositeMsg2", containerFactory = "jmsTCFrecipe")
    public void processRecipeMessage(RequestMessage message) {
        System.out.println("Recipe Adapter received message");
        try {
            ResponseEntity<?> recipeResponse = makeRecipeCall(message.getTerm());
            if (recipeResponse.getStatusCode() == HttpStatus.OK) {
                // Parse the response from Edamam API
                String respent = (String) recipeResponse.getBody();
                JsonNode jsonNode = objectMapper.readTree(respent);

                // Extract relevant information from the Edamam API response
                String title = jsonNode.get("hits").get(0).get("recipe").get("label").asText();
                String picture = jsonNode.get("hits").get(0).get("recipe").get("image").asText();
                List<String> ingredients = new ArrayList<>();

                if (jsonNode.get("hits").get(0).get("recipe").get("ingredientLines").isArray()){
                    for(JsonNode item : jsonNode.get("hits").get(0).get("recipe").get("ingredientLines")){
                        ingredients.add(item.asText()+" ,");
                    }
                }

                // Log the processed information and create a response message
                System.out.println("Food title Recipe consumer from edam"+ title);
                List<String> history = message.getHistory();
                history.add(COMPONENT_NAME);
                responseMessage = new ResponseMessage(message.getId(), title, picture, ingredients, null, history);
                responseMessage.setTestMessage(message.isTestMessage());
                responseMessage.setType("Response message");

                // Send the response message to a JMS queue ("to-aggregator-queue")
                sendMessageToQueue(responseMessage, "to-aggregator-queue");
                // Send a log message to a JMS topic ("topic.control-bus")
                sendMessageToQueue(new LogMessage(responseMessage), "topic.control-bus");
            }
        } catch (Exception e) {
            // Handle exceptions
            System.out.println(e.getMessage());
            System.out.println("Error recipe adapter");
        }
    }
}


