package com.fkt.networkmaster.controllers;

import com.fkt.networkmaster.models.NetworkRecord;
import com.fkt.networkmaster.services.NetworkRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Network Record")
@RestController
@RequestMapping("/api")
public class NetworkRecordController {
    private NetworkRecordService service;
    @Autowired
    public NetworkRecordController(NetworkRecordService service){
        this.service =service;
    }
    @Operation(summary = "List All Network Record")
    @GetMapping("/nat/record")
    public ResponseEntity<Iterable<NetworkRecord>> list(){
        return this.service.findAllNetworkRecord();
    }
}
