package venom.toolbot.model;

import lombok.Data;

@Data
public class NodeSeekResp {
    private Boolean success;
    private String message;
    private Integer gain;
    private Integer current;
}
