package com.example.eia.app.app;

import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DynamicDestinationResolver;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class Config {

    // Configuration bean for RestTemplate
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // Configuration bean for ActiveMQ ConnectionFactory
    @Bean
    public ConnectionFactory connectionFactory(){
        // Set up ActiveMQ ConnectionFactory with credentials and broker URL
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin","admin", "tcp://activemq:61616");
        connectionFactory.setTrustedPackages(Collections.singletonList("*"));
        return connectionFactory;
    }

    // Configuration bean for JmsTemplate
    @Bean
    public JmsTemplate jmsTemplate(){
        // Set up JmsTemplate with the configured ConnectionFactory and other properties
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory());
        jmsTemplate.setDestinationResolver(destinationResolver());
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.setDeliveryPersistent(true);
        return jmsTemplate;
    }

    // Configuration bean for DynamicDestinationResolver based on what a string ends
    @Bean
    DynamicDestinationResolver destinationResolver() {
        return new DynamicDestinationResolver() {
            @Override
            public Destination resolveDestinationName(Session session, String destinationName, boolean pubSubDomain) throws JMSException {
                // Determine if the destination is a queue or topic based on its name
                if(destinationName.endsWith("queue")) {
                    pubSubDomain = false;
                }
                else {
                    pubSubDomain = true;
                }
                return super.resolveDestinationName(session,destinationName,pubSubDomain);
            }
        };
    }

    // Configuration bean for DefaultJmsListenerContainerFactory
    @Bean
    public DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        // Set up DefaultJmsListenerContainerFactory with the configured ConnectionFactory and other properties
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setSubscriptionDurable(true);
        return factory;
    }

    // Configuration bean for JmsListenerContainerFactory for Recipe consumer
    @Bean(name = "jmsTCFrecipe")
	public JmsListenerContainerFactory<?> jmsTCFrecipe(ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer ) {
		// Set up JmsListenerContainerFactory with the configured ConnectionFactory and other properties for Recipe consumer
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		configurer.configure( factory, connectionFactory );
		factory.setConnectionFactory(connectionFactory);
        factory.setSubscriptionDurable(true);
        factory.setClientId("consR");
		return factory;
	}

    // Configuration bean for JmsListenerContainerFactory for Control Bus
    @Bean(name = "jmsControlBus")
    public JmsListenerContainerFactory<?> jmsControlBus(ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer ) {
        // Set up JmsListenerContainerFactory with the configured ConnectionFactory and other properties for Control Bus
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure( factory, connectionFactory );
        factory.setConnectionFactory(connectionFactory);
        factory.setSubscriptionDurable(true);
        factory.setClientId("controlBus");
        return factory;
    }
}
