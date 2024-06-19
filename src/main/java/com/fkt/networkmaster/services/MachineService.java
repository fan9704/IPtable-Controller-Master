package com.fkt.networkmaster.services;

import com.fkt.networkmaster.dtos.request.MachineCreateDTO;
import com.fkt.networkmaster.models.Machine;
import com.fkt.networkmaster.repositories.MachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Boolean checkRepeatMachine(String hostIp){
        List<Machine> machineList=this.repository.findByHostIpIs(hostIp);
        return !machineList.isEmpty();
    }
}
