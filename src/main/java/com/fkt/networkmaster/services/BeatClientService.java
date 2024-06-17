package com.fkt.networkmaster.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fkt.networkmaster.dtos.request.MachineCreateDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BeatClientService {
    @Autowired
    private MachineService service;


    @RabbitListener(queues = {"beat"})
    public void listenBeat(String message) throws JsonProcessingException {
        System.out.println("Received message from queue1: " + message);
        MachineCreateDTO dto =this.messageToRequestDTO(new String(message.getBytes()));
    }

    public MachineCreateDTO messageToRequestDTO(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        MachineCreateDTO dto = objectMapper.readValue(message, MachineCreateDTO.class);

        System.out.println("Network "  + dto.toString() +
                " [x] Received");
        return dto;
    }
}