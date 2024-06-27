package com.fkt.networkmaster.services;


import com.fkt.networkmaster.dtos.request.NetworkRecordCreateDTO;
import com.fkt.networkmaster.models.NetworkRecord;
import com.fkt.networkmaster.repositories.NetworkRecordRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class NetworkRecordService {
    private NetworkRecordRepository repository;
    private final RabbitTemplate rabbitTemplate;
    @Value("${spring.rabbitmq.enable}")
    private Boolean amqpIsEnable = false;
    @Autowired
    public NetworkRecordService(NetworkRecordRepository repository, RabbitTemplate rabbitTemplate){
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setMessageConverter( new Jackson2JsonMessageConverter());
    }

    public ResponseEntity<Iterable<NetworkRecord>> findAllNetworkRecord(){
        return new ResponseEntity<>(this.repository.findAll(), HttpStatus.OK);
    }
    public ResponseEntity<NetworkRecord> findNetworkRecordById(String id){
        Optional<NetworkRecord> optionalNetworkRecord =this.repository.findById(id);
        return optionalNetworkRecord.map(networkRecord -> new ResponseEntity<>(networkRecord, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }
    public ResponseEntity<NetworkRecord> createWithResponseEntity(NetworkRecordCreateDTO dto) {
        NetworkRecord networkRecord=this.createNatService(dto);
        if(networkRecord == null){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }else{
            // Send AMQP Create Message
            this.amqpSendJSON(networkRecord,"create");
            return new ResponseEntity<>(networkRecord, HttpStatus.CREATED);
        }
    }
    public NetworkRecord createNatService(NetworkRecordCreateDTO dto) {
        // Check Rule is repeat
        String fullNetworkRecord = dto.getFullNetworkRecord();
        boolean repeated = this.check_repeat_record(fullNetworkRecord);
        if(repeated){
            System.out.println("Network Record Repeat");
            return null;
        }
        // Create NAT Record in Host
        return this.repository.save(dto.dtoToNetworkRecord());

    }
    public ResponseEntity<NetworkRecord> patchNetworkRecordByIdWithResponseEntity(String id, NetworkRecordCreateDTO dto) {
        NetworkRecord networkRecord = this.patchNetworkRecordById(id,dto);
        if(networkRecord != null){
            // Send AMQP Update Message
            this.amqpSendJSON(networkRecord,"update");
            return new ResponseEntity<>(networkRecord,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }
    public NetworkRecord patchNetworkRecordById(String id, NetworkRecordCreateDTO dto) {
        Optional<NetworkRecord> optionalNetworkRecord =this.repository.findById(id);
        // Patch Network Record
        if(optionalNetworkRecord.isPresent()){
            NetworkRecord oldNetworkRecord = optionalNetworkRecord.get();
            NetworkRecord newNetworkRecord=this.editNetworkRecord(oldNetworkRecord,dto);
            return this.repository.save(newNetworkRecord);
        }else{
            return null;
        }
    }

    private NetworkRecord editNetworkRecord(NetworkRecord networkRecord,NetworkRecordCreateDTO dto) {
        if(!Objects.equals(dto.getInputIp(), "")){
            networkRecord.setInputIp(dto.getInputIp());
        }
        if(!Objects.equals(dto.getInputPort(), "")){
            networkRecord.setInputPort(dto.getInputPort());
        }
        if (!Objects.equals(dto.getOutputPort(), "")) {
            networkRecord.setOutputIp(dto.getOutputIp());
        }
        if (!Objects.equals(dto.getOutputPort(), "")) {
            networkRecord.setOutputPort(dto.getOutputPort());
        }
        if (!Objects.equals(dto.getNote(), "")){
            networkRecord.setNote(dto.getNote());
        }
        if (!Objects.equals(dto.getProtocol(), "")){
            networkRecord.setProtocol(dto.getProtocol());
        }

        networkRecord.setFullNetworkRecord(networkRecord.getFullNetworkRecord());
        return networkRecord;
    }
    public NetworkRecord createNetworkRecord(NetworkRecord networkRecord){
        if(!check_repeat_record(networkRecord.getFullNetworkRecord())){
            return this.repository.save(networkRecord);
        }else if(this.repository.findOneByFullNetworkRecord(networkRecord.getFullNetworkRecord()).isPresent()){
            return this.repository.findOneByFullNetworkRecord(networkRecord.getFullNetworkRecord()).get();
        }
        return null;
    }
    public ResponseEntity<?> deleteNetworkRecordByIdWithResponseEntity(String id) {
        Optional<NetworkRecord> networkRecordOptional = this.repository.findById(id);
        if(networkRecordOptional.isPresent()){
            this.repository.deleteById(id);
            this.amqpSendJSON(networkRecordOptional.get(),"delete");
        }
        return new ResponseEntity<>(null,HttpStatus.OK);
    }

    public NetworkRecord retrieveNetworkRecord(String id){
        Optional<NetworkRecord> networkRecordOptional = this.repository.findById(id);
        return networkRecordOptional.orElse(null);
    }

    public NetworkRecord updateNetworkRecord(NetworkRecord networkRecord){
        Optional<NetworkRecord> networkRecordOptional=this.repository.findOneByFullNetworkRecord(networkRecord.getFullNetworkRecord());
        if(networkRecordOptional.isPresent()){
            NetworkRecord oldNetworkRecord = networkRecordOptional.get();
            if(!Objects.equals(networkRecord.getInputIp(), "")){
                oldNetworkRecord.setInputIp(networkRecord.getInputIp());
            }
            if(!Objects.equals(networkRecord.getInputPort(), "")){
                oldNetworkRecord.setInputPort(networkRecord.getInputPort());
            }
            if (!Objects.equals(networkRecord.getOutputPort(), "")) {
                oldNetworkRecord.setOutputIp(networkRecord.getOutputIp());
            }
            if (!Objects.equals(networkRecord.getOutputPort(), "")) {
                oldNetworkRecord.setOutputPort(networkRecord.getOutputPort());
            }
            if (!Objects.equals(networkRecord.getNote(), "")){
                oldNetworkRecord.setNote(networkRecord.getNote());
            }
            if (!Objects.equals(networkRecord.getProtocol(), "")){
                oldNetworkRecord.setProtocol(networkRecord.getProtocol());
            }

            oldNetworkRecord.setFullNetworkRecord(networkRecord.getFullNetworkRecord());
            return this.repository.save(oldNetworkRecord);
        }else{
            return null;
        }
    }
    public void deleteNetworkRecord(String fullNetworkRecord){
        this.repository.deleteOneByFullNetworkRecord(fullNetworkRecord);
    }

    public boolean check_repeat_record(String fullNetworkRecord){
        List<NetworkRecord> networkRecordList=this.repository.findByFullNetworkRecordIs(fullNetworkRecord);
        return !networkRecordList.isEmpty();
    }

    public void amqpSendJSON(NetworkRecord networkRecord,String operation){
        if(this.amqpIsEnabled()){
            rabbitTemplate.convertAndSend("master","master",networkRecord.toAMQPDTO(operation));
            System.out.println("Sent AMQP Message");
        }else{
            System.out.println("AMQP Is Disabled");
        }
    }

    public Boolean amqpIsEnabled(){
        return this.amqpIsEnable;
    }
}