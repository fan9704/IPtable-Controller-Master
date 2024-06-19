package com.fkt.networkmaster.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fkt.networkmaster.dtos.request.MachineCreateDTO;
import com.fkt.networkmaster.models.Machine;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class BeatClientService {
    @Autowired
    private MachineService service;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "beat",durable = "true",autoDelete = "true"),
            exchange = @Exchange(value = "beat"),
            key = "beat"
    ))
    public void listenBeat(String message) throws JsonProcessingException {
        System.out.println("Received message from Beat Queue: " + message);
        MachineCreateDTO dto =this.messageToRequestDTO(new String(message.getBytes()));
        Machine machine =this.service.createMachine(dto);
        if(machine != null){
            System.out.println("New Machine");
        }else {
            System.out.println("Repeated Machine");
        }
    }

    public MachineCreateDTO messageToRequestDTO(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        MachineCreateDTO dto = objectMapper.readValue(message, MachineCreateDTO.class);

        System.out.println("Network "  + dto.toString() +
                " [x] Received");
        return dto;
    }
}