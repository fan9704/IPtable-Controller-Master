//package com.fkt.networkmaster.services;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fkt.networkmaster.dtos.request.NetworkRecordRequestDTO;
//import com.fkt.networkmaster.models.NetworkRecord;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.core.MessageListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Objects;
//
//@Service
//public class NATQueueClientService implements MessageListener{
//
//    private NetworkRecordService service;
////    @Autowired
////    public NATQueueClientService(NetworkRecordService service) {
////        this.service = service;
////    }
//
//    public void onMessage(Message message) {
//        System.out.println("Consuming Message - " + new String(message.getBody()));
//        try {
//            NetworkRecordRequestDTO dto =this.messageToRequestDTO(new String(message.getBody()));
//            System.out.println("Operation"+dto.getOperation());
//            if(Objects.equals(dto.getOperation(), "create")){
////                NetworkRecord networkRecord=  this.service.createNetworkRecord(dto.toNetworkRecord());
////                System.out.println("Create Success"+ networkRecord.toString());
//            }
//
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//    public NetworkRecordRequestDTO messageToRequestDTO(String message) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        NetworkRecordRequestDTO dto = objectMapper.readValue(message, NetworkRecordRequestDTO.class);
//
//        System.out.println("Network "  + dto.toString() +
//                " [x] Received");
//        return dto;
//    }
//}
