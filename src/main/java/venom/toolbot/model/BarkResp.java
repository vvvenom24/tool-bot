package venom.toolbot.model;

import lombok.Data;

@Data
public class BarkResp {

    private Integer code;

    private String message;

    private Long timestamp;
}
