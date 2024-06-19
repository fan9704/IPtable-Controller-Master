package com.fkt.networkmaster.controllers;

import com.fkt.networkmaster.dtos.request.MachineCreateDTO;
import com.fkt.networkmaster.models.Machine;
import com.fkt.networkmaster.services.MachineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        MachineCreateDTO dto = new MachineCreateDTO("140.96.83.14","140.96.83.14:9990","140.96.83.14","");
        rabbitTemplate.convertAndSend("beat","beat",dto);
        return this.service.findAllMachine();
    }
}
