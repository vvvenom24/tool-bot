package venom.toolbot.model;

import lombok.Data;
import venom.toolbot.mapper.QdLogMapper;

import java.util.Map;

@Data
public class SignInTaskParam {

    private Map<String, String> cookies;

    private String baseUrl;

    private QdLogMapper qdLogMapper;

    private String taskName;
}
