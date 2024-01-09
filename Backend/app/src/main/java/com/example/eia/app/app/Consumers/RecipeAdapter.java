package com.example.eia.app.app.Consumers;

import java.util.ArrayList;
import java.util.List;

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

    String API_KEY = "27bbe8a5f41e9b888483a5bba7887871";

    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    private JmsTemplate jmsTemplate;

    private ResponseMessage responseMessage;

    private final ObjectMapper objectMapper = new ObjectMapper();


    public void sendMessageToQueue(ResponseMessage message, String queueName) {
        jmsTemplate.convertAndSend(queueName, message);
    }

    public ResponseEntity<?> makeRecipeCall(String query){
        String apiUrl = "https://api.edamam.com/api/recipes/v2?type=public&beta=false&q="+ query +"&app_id=d1d245d5&app_key="+API_KEY;
        String response = restTemplate.getForObject(apiUrl, String.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @JmsListener(destination = "topic.compositeMsg2", containerFactory = "jmsTCFrecipe")
    public void processRecipeMessage(RequestMessage message) {
        try {
            ResponseEntity<?> recipeResponse = makeRecipeCall(message.getTerm());
            if (recipeResponse.getStatusCode() == HttpStatus.OK) {
                String respent = (String) recipeResponse.getBody();
                JsonNode jsonNode = objectMapper.readTree(respent);

            
                String title = jsonNode.get("hits").get(0).get("recipe").get("label").asText();
                String picture = jsonNode.get("hits").get(0).get("recipe").get("image").asText();
                List<String> ingridients = new ArrayList<>();

                if (jsonNode.get("hits").get(0).get("recipe").get("ingredientLines").isArray()){
                    for(JsonNode item : jsonNode.get("hits").get(0).get("recipe").get("ingredientLines")){
                        ingridients.add(item.asText());
                    }
                }
                System.out.println("Food title Recipe consumer from edam"+ title);

                responseMessage = new ResponseMessage(message.getId(), title, picture, ingridients, null);
                sendMessageToQueue(responseMessage, "to-aggregator-queue");
            }
        } catch (Exception e) {
            System.out.println("Error");
        }
    }
}


