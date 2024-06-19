package com.fkt.networkmaster.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fkt.networkmaster.dtos.request.NetworkRecordRequestDTO;
import com.fkt.networkmaster.models.NetworkRecord;
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

    @RabbitListener(queues = {"nat.client"})
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = "nat.client",durable = "true",autoDelete = "true"),
//            exchange = @Exchange(value = "nat.client"),
//            key = "nat.client"
//    ))
    public void listenBeat(String message)throws JsonProcessingException {
        System.out.println("Consuming Message - " + new String(message.getBytes()));
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

        System.out.println("Network "  + dto.toString() +
                " [x] Received");
        return dto;
    }
}
