package com.fkt.networkmaster.configs;

//import com.fkt.networkmaster.services.NATQueueClientService;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    @Value("${fkt.rabbitmq.receive.queue}")
    String masterQueueName;
    String masterExchangeName="master";
    String masterRoutingKey ="master";
    String beatExchangeName="beat";
    String beatRoutingKey ="beat";
    @Value("${fkt.rabbitmq.beat.queue}")
    String beatQueueName;

    @Value("${spring.rabbitmq.username}")
    String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Bean
    Queue masterQueue() {
        return new Queue(masterQueueName, false);
    }
    @Bean
    public TopicExchange masterExchange(){
        return new TopicExchange(masterExchangeName);
    }
    @Bean
    public Binding masterJsonBinding(){
        return BindingBuilder
                .bind(masterQueue())
                .to(masterExchange())
                .with(masterRoutingKey);
    }
    @Bean
    Queue beatQueue() {
        return new Queue(beatQueueName, false);
    }
    @Bean
    public TopicExchange beatExchange(){
        return new TopicExchange(beatExchangeName);
    }
    @Bean
    public Binding beatJsonBinding(){
        return BindingBuilder
                .bind(beatQueue())
                .to(beatExchange())
                .with(beatRoutingKey);
    }
}