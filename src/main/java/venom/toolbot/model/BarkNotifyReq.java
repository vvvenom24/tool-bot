package venom.toolbot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BarkNotifyReq {

    private String title;

    private String body;

    private String icon;
}
