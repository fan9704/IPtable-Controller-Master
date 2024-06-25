package com.fkt.networkmaster.dtos.request;
import com.fkt.networkmaster.models.Machine;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MachinePatchDTO {
    private String hostIp;
    private String backendUrl;
    private String frontendUrl;
    private String note;

    public Machine toMachine(){
        Machine machine = new Machine();
        machine.setHostIp(this.hostIp);
        machine.setBackendUrl(this.backendUrl);
        machine.setFrontendUrl(this.frontendUrl);
        machine.setNote(this.note);
        return machine;
    }
}
