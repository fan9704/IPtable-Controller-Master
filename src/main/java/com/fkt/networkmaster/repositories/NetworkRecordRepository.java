package com.fkt.networkmaster.repositories;

import com.fkt.networkmaster.models.NetworkRecord;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface NetworkRecordRepository extends ElasticsearchRepository<NetworkRecord,String> {
}
