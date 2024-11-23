package venom.toolbot.entity;

import lombok.Data;

import java.time.LocalTime;

/**
 * 签到账号
 */
@Data
public class QdAccount {
    /**
     * 账号id
     */
    private Long accountId;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 登录账号
     */
    private String loginAccount;

    /**
     * cookie
     */
    private String accountCookie;

    /**
     * 开始时间
     */
    private LocalTime startTime;

    /**
     * 结束时间
     */
    private LocalTime endTime;
}