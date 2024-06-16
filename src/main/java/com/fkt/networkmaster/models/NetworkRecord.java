package com.fkt.networkmaster.models;


import  lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.lang.Nullable;

@Document(indexName = "NetworkRecord")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}