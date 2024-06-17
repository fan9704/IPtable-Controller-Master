package com.fkt.networkmaster.services;


import com.fkt.networkmaster.models.NetworkRecord;
import com.fkt.networkmaster.repositories.NetworkRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class NetworkRecordService {
    private NetworkRecordRepository repository;
    @Autowired
    public NetworkRecordService(NetworkRecordRepository repository){
        this.repository = repository;
    }

    public ResponseEntity<Iterable<NetworkRecord>> findAllNetworkRecord(){
        return new ResponseEntity<>(this.repository.findAll(), HttpStatus.OK);
    }
    public NetworkRecord createNetworkRecord(NetworkRecord networkRecord){
        if(!check_repeat_record(networkRecord.getFullNetworkRecord())){
            return this.repository.save(networkRecord);
        }else{
            return this.repository.findOneByFullNetworkRecord(networkRecord.getFullNetworkRecord()).get();
        }

    }
    public NetworkRecord retrieveNetworkRecord(String id){
        Optional<NetworkRecord> networkRecordOptional = this.repository.findById(id);
        return networkRecordOptional.orElse(null);
    }

    public NetworkRecord updateNetworkRecord(NetworkRecord networkRecord){
//        Optional<NetworkRecord> networkRecordOptional=this.repository.findById(id);
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

    public ResponseEntity<NetworkRecord> find_network_record_by_id(String id){
        Optional<NetworkRecord> optionalNetworkRecord =this.repository.findById(id);
        return optionalNetworkRecord.map(networkRecord -> new ResponseEntity<>(networkRecord, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    public boolean check_repeat_record(String fullNetworkRecord){
        List<NetworkRecord> networkRecordList=this.repository.findByFullNetworkRecordIs(fullNetworkRecord);
        return !networkRecordList.isEmpty();
    }


}