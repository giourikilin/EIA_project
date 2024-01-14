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

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ConnectionFactory connectionFactory(){
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin","admin", "tcp://activemq:61616");
        connectionFactory.setTrustedPackages(Collections.singletonList("*"));
        return connectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate(){
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory());
        jmsTemplate.setDestinationResolver(destinationResolver());
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.setDeliveryPersistent(true);
        return jmsTemplate;
    }

    @Bean
    DynamicDestinationResolver destinationResolver() {
        return new DynamicDestinationResolver() {
            @Override
            public Destination resolveDestinationName(Session session, String destinationName, boolean pubSubDomain) throws JMSException {
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

    @Bean
    public DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setSubscriptionDurable(true);
        return factory;
    }

    @Bean(name = "jmsTCFrecipe")
	public JmsListenerContainerFactory<?> jmsTCFrecipe(ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer ) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		configurer.configure( factory, connectionFactory );
		factory.setConnectionFactory(connectionFactory);
        factory.setSubscriptionDurable(true);
        factory.setClientId("consR");
		return factory;
	}

    @Bean(name = "jmsControlBus")
    public JmsListenerContainerFactory<?> jmsControlBus(ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer ) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure( factory, connectionFactory );
        factory.setConnectionFactory(connectionFactory);
        factory.setSubscriptionDurable(true);
        factory.setClientId("controlBus");
        return factory;
    }
}
