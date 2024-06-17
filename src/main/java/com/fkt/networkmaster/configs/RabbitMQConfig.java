package com.fkt.networkmaster.configs;

//import com.fkt.networkmaster.services.NATQueueClientService;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    @Value("${fkt.rabbitmq.receive.queue}")
    String queueName;

    @Value("${fkt.rabbitmq.beat.queue}")
    String beatQueueName;

    @Value("${spring.rabbitmq.username}")
    String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    Queue beatQueue() {
        return new Queue(beatQueueName, true);
    }

    //create MessageListenerContainer using default connection factory
//    @Bean
//    MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory ) {
//        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
//        simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
//        simpleMessageListenerContainer.setQueues(queue());
//        simpleMessageListenerContainer.setMessageListener(new NATQueueClientService());
//        return simpleMessageListenerContainer;
//
//    }

//    @Bean
//    MessageListenerContainer beatListenerContainer(ConnectionFactory connectionFactory ) {
//        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
//        simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
//        simpleMessageListenerContainer.setQueues(beatQueue());
//        simpleMessageListenerContainer.setMessageListener(new BeatClientService());
//        return simpleMessageListenerContainer;
//
//    }
}