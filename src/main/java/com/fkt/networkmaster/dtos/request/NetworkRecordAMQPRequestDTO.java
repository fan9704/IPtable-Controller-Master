package com.fkt.networkmaster.dtos.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NetworkRecordAMQPRequestDTO {
    private String outputIp;
    private String outputPort;

    private String inputIp;
    private String inputPort;
    private String protocol = "TCP";


    private String note;


    private String fullNetworkRecord;
    private String operation;
}