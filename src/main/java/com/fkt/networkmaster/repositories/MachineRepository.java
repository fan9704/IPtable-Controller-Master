package com.fkt.networkmaster.repositories;

import com.fkt.networkmaster.models.Machine;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface MachineRepository extends ElasticsearchRepository<Machine,String> {
    List<Machine> findByHostIpIs(String hostIp);
}
