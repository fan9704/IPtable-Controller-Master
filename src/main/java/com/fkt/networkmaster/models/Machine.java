package com.fkt.networkmaster.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "machine")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Machine {
    @Id
    private String id;
    @NonNull
    private String hostIp;
    private String backendUrl;
    private String frontendUrl;
    private String note;
}
