package com.fkt.networkmaster.models;


import com.fkt.networkmaster.dtos.request.NetworkRecordAMQPRequestDTO;
import  lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.lang.Nullable;

@Document(indexName = "network_record")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NetworkRecord {
    @Id
    private String id;
    @NonNull
    private String outputIp;
    @NonNull
    private String outputPort;

    @NonNull
    private String inputIp;
    @NonNull
    private String inputPort;
    @NonNull
    private String protocol = "TCP";

    @Nullable
    private String note;

    @NonNull
    private String fullNetworkRecord;

    public void generateFullNetworkRecord(){
        this.setFullNetworkRecord(String.format("%s:%s:%s:%s",
                this.getOutputPort(),
                this.getOutputIp(),
                this.getInputPort(),
                this.getInputIp()
        ));
    }
    public String getFullNetworkRecord(){
        return String.format("%s:%s:%s:%s",
                this.getOutputPort(),
                this.getOutputIp(),
                this.getInputPort(),
                this.getInputIp()
        );
    }
    public NetworkRecordAMQPRequestDTO toAMQPDTO(String operation){
        NetworkRecordAMQPRequestDTO dto = new NetworkRecordAMQPRequestDTO(
                getOutputIp(),
                getOutputPort(),
                getInputIp(),
                getInputPort(),
                getProtocol(),
                getNote(),
                getFullNetworkRecord(),
                operation
        );
        return dto;
    }
}