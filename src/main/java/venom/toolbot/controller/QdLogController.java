package venom.toolbot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import venom.toolbot.entity.QdLog;
import venom.toolbot.mapper.QdLogMapper;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class QdLogController {

    private final QdLogMapper qdLogMapper;

    @GetMapping("/account/{appName}/{loginAccount}")
    public List<QdLog> getLogsByAccount(@PathVariable("appName") String appName,
                                        @PathVariable("loginAccount") String loginAccount) {
        return qdLogMapper.selectByLoginAccount(appName, loginAccount);
    }
}
