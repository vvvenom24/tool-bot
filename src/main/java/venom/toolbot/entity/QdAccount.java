package venom.toolbot.entity;

import lombok.Data;

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
}