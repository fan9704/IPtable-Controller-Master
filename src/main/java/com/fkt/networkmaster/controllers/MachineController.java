package com.fkt.networkmaster.controllers;

import com.fkt.networkmaster.dtos.request.MachineCreateDTO;
import com.fkt.networkmaster.dtos.request.MachinePatchDTO;
import com.fkt.networkmaster.models.Machine;
import com.fkt.networkmaster.services.MachineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Machine")
@RestController
@RequestMapping("/api")
public class MachineController {
    private MachineService service;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MachineController(MachineService service, RabbitTemplate rabbitTemplate){
        this.service=service;
        this.rabbitTemplate=rabbitTemplate;
        rabbitTemplate.setMessageConverter( new Jackson2JsonMessageConverter());
    }
    @Operation(summary = "List All Machine")
    @GetMapping("/machine")
    public ResponseEntity<Iterable<Machine>> list(){
//        MachineCreateDTO dto = new MachineCreateDTO("140.96.83.14","140.96.83.14:9990","140.96.83.14","");
//        rabbitTemplate.convertAndSend("beat","beat",dto);
        return this.service.findAllMachine();
    }

    @Operation(summary = "Get Machine By Id")
    @GetMapping("/machine/{id}")
    public ResponseEntity<Machine> getById(@PathVariable("id") String id){
        Machine machine = this.service.getMachineById(id);
        if(machine != null){
            return  new ResponseEntity<>(machine,HttpStatus.OK);
        }else{
            return  new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }
    @Operation(summary = "Patch Machine By Id")
    @PatchMapping("/machine/{id}")
    public ResponseEntity<Machine> patchById(@PathVariable("id") String id, @RequestBody MachinePatchDTO dto){
        return new ResponseEntity<>(this.service.patchMachineById(id,dto), HttpStatus.OK);
    }
    @Operation(summary = "Delete Machine By Id")
    @DeleteMapping("/machine/{id}")
    public void deleteById(@PathVariable("id") String id){
        this.service.deleteMachineById(id);
    }


}
