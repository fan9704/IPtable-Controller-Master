package com.fkt.networkmaster.services;

import com.fkt.networkmaster.dtos.request.MachineCreateDTO;
import com.fkt.networkmaster.dtos.request.MachinePatchDTO;
import com.fkt.networkmaster.models.Machine;
import com.fkt.networkmaster.repositories.MachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MachineService {
    private MachineRepository repository;

    @Autowired
    public MachineService(MachineRepository repository){
        this.repository = repository;
    }

    public ResponseEntity<Iterable<Machine>> findAllMachine(){
        return new ResponseEntity<>(this.repository.findAll(), HttpStatus.OK);
    }
    public Machine createMachine(MachineCreateDTO dto){
        Boolean isRepeat = this.checkRepeatMachine(dto.getHostIp());
        if(isRepeat){
            return null;
        }else{
            Machine machine = dto.toMachine();
            return this.repository.save(machine);
        }
    }
    public  Machine getMachineById(String id){
        Optional<Machine> machineOptional = this.repository.findById(id);
        if(machineOptional.isPresent()){
            return machineOptional.get();
        }else{
            return null;
        }
    }
    public Machine patchMachineById(String id, MachinePatchDTO dto){
        Optional<Machine> machineOptional = this.repository.findById(id);
        if(machineOptional.isPresent()){
            Machine machine = machineOptional.get();
            if(!Objects.equals(dto.getHostIp(), "")){
                machine.setHostIp(dto.getHostIp());
            }
            if(!Objects.equals(dto.getBackendUrl(), "")){
                machine.setBackendUrl(dto.getBackendUrl());
            }
            if(!Objects.equals(dto.getFrontendUrl(), "")){
                machine.setFrontendUrl(dto.getFrontendUrl());
            }
            if(!Objects.equals(dto.getNote(), "")){
                machine.setHostIp(dto.getNote());
            }
            return this.repository.save(machine);
        }else{
            return null;
        }
    }
    public void deleteMachineById(String id){
        this.repository.deleteById(id);
    }

    public Boolean checkRepeatMachine(String hostIp){
        List<Machine> machineList=this.repository.findByHostIpIs(hostIp);
        return !machineList.isEmpty();
    }
}
