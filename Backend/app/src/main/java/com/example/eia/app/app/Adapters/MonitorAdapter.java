package com.example.eia.app.app.Adapters;

import com.example.eia.app.app.Monitor.Log;
import com.example.eia.app.app.Monitor.LogMessage;
import com.example.eia.app.app.Monitor.LogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MonitorAdapter {

    // Autowired LogRepo for interacting with logs in the database
    @Autowired
    private LogRepo logRepo;

    // Component name for tracking in the history
    private final String COMPONENT_NAME = "monitor adapter";

    // Consumer for processing log messages from the control bus topic
    @JmsListener(destination = "topic.control-bus", containerFactory = "jmsControlBus")
    public void processLogMessage(LogMessage message) {
        System.out.println("Monitor Adapter received Request");
        // Update history with the current component name
        List<String> history = message.getHistory();
        history.add(COMPONENT_NAME);
        // Create a Log entity and save it to the database
        Log log = new Log(0L,message.getType(), history.toString(), message.getMessage(), message.isTestMessage());
        try{
            logRepo.save(log);
        } catch(Exception e){
            // Handle exceptions related to saving logs
            System.out.println(e.getMessage());
            System.out.println("Monitor adapter");
        }
    }
}
