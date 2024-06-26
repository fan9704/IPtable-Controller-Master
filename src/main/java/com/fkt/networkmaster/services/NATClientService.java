package com.fkt.networkmaster.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fkt.networkmaster.dtos.request.NetworkRecordRequestDTO;
import com.fkt.networkmaster.models.NetworkRecord;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class NATClientService {
    private NetworkRecordService service;
    @Autowired
    public NATClientService(NetworkRecordService service){
        this.service = service;
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "nat.client"),
            exchange = @Exchange(value = "client"),
            key = "client"
    ))
    public void listenBeat(String message)throws JsonProcessingException {
        System.out.println("[Network] Received: " + new String(message.getBytes()));
        try {
            NetworkRecordRequestDTO dto =this.messageToRequestDTO(new String(message.getBytes()));
            System.out.println("Operation"+dto.getOperation());
            if(Objects.equals(dto.getOperation(), "create")){
                NetworkRecord networkRecord=  this.service.createNetworkRecord(dto.toNetworkRecord());
                System.out.println("Create Success"+ networkRecord.toString());
            }else if(Objects.equals(dto.getOperation(), "update")){
                NetworkRecord networkRecord = this.service.updateNetworkRecord(dto.toNetworkRecord());
                System.out.println("Update Success"+networkRecord.toString());
            }else if(Objects.equals(dto.getOperation(),"delete")){
                this.service.deleteNetworkRecord(dto.toNetworkRecord().getFullNetworkRecord());
                System.out.println("Delete Success"+dto.toNetworkRecord().toString());
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    public NetworkRecordRequestDTO messageToRequestDTO(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        NetworkRecordRequestDTO dto = objectMapper.readValue(message, NetworkRecordRequestDTO.class);
        return dto;
    }
}