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
            value = @Queue(value = "beat"),
            exchange = @Exchange(value = "beat"),
            key = "beat"
    ))
    public void listenBeat(String message) throws JsonProcessingException {
        System.out.println("[Beat] Received: " + message);
        MachineCreateDTO dto =this.messageToRequestDTO(new String(message.getBytes()));
        Machine machine =this.service.createMachine(dto);
    }

    public MachineCreateDTO messageToRequestDTO(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        MachineCreateDTO dto = objectMapper.readValue(message, MachineCreateDTO.class);
        return dto;
    }
}