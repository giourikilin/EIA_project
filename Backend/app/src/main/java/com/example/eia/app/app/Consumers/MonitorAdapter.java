package com.example.eia.app.app.Consumers;

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
    @Autowired
    private LogRepo logRepo;
    private final String COMPONENT_NAME = "monitor adapter";

    @JmsListener(destination = "topic.control-bus", containerFactory = "jmsControlBus")
    public void processLogMessage(LogMessage message) {
        List<String> history = message.getHistory();
        history.add(COMPONENT_NAME);
        Log log = new Log(0L,message.getType(), history.toString(), message.getMessage());
        try{
            logRepo.save(log);
        } catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println("Monitor adapter");
        }
    }
}
