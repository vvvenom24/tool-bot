package venom.toolbot.entity;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * 签到日志
 */
@Data
public class QdLog {
    /**
    * 主键
    */
    private Long id;

    /**
    * 签到账号
    */
    private String loginAccount;

    /**
    * 签到信息
    */
    private String message;

    /**
    * 创建时间
    */
    private LocalDateTime createTime;
}