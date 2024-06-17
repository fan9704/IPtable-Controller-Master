package com.fkt.networkmaster.repositories;

import com.fkt.networkmaster.models.NetworkRecord;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;


public interface NetworkRecordRepository extends ElasticsearchRepository<NetworkRecord,String> {
    List<NetworkRecord> findByFullNetworkRecordIs(String fullNetworkRecord);

    Optional<NetworkRecord> findOneByFullNetworkRecord(String fullNetworkRecord);
    void deleteOneByFullNetworkRecord(String fullNetworkRecord);
}
