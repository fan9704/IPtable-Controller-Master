package com.fkt.networkmaster.controllers;

import com.fkt.networkmaster.dtos.request.NetworkRecordCreateDTO;
import com.fkt.networkmaster.models.NetworkRecord;
import com.fkt.networkmaster.services.NetworkRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
    @Operation(summary = "Find By Id Network Record")
    @GetMapping("/nat/record/{id}")
    public ResponseEntity<NetworkRecord> findById(@PathVariable("id") String id){
        return this.service.findNetworkRecordById(id);
    }
    @Operation(summary = "Patch By Id Network Record")
    @PatchMapping("/nat/record/{id}")
    public ResponseEntity<NetworkRecord> patchById(@PathVariable("id") String id ,@RequestBody NetworkRecordCreateDTO dto) {
        return this.service.patchNetworkRecordByIdWithResponseEntity(id,dto);
    }
    @Operation(summary = "Create NAT Iptables Service")
    @PostMapping("/nat/record")
    public ResponseEntity<NetworkRecord> createNat(@RequestBody NetworkRecordCreateDTO dto) {
        return this.service.createWithResponseEntity(dto);
    }
    @Operation(summary = "Delete NAT Iptables Service")
    @DeleteMapping("/nat/record/{id}")
    public ResponseEntity<?> deleteNat(@PathVariable("id") String id) {
        return this.service.deleteNetworkRecordByIdWithResponseEntity(id);
    }
}
