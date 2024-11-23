package venom.toolbot.enums;

import lombok.Getter;

@Getter
public enum TaskStatusEnum {
    V2EX_SUCCESS("签到成功"),
    V2EX_FAILURE_1("无法找到签到按钮"),
    V2EX_FAILURE_2("今天已经签到过了"),
    V2EX_FAILURE_3("无法获取余额列表"),
    V2EX_UNKNOWN_FAILURE("未知异常"),
    HIFINI_SUCCESS("签到成功"),
    HIFINI_FAILURE_1("cookie 失效"),
    HIFINI_FAILURE_2("无法获取 sign 值"),
    HIFINI_FAILURE_3("签到响应异常"),
    HIFINI_UNKNOWN_FAILURE("未知异常"),
    NODESEEK_SUCCESS("签到成功"),
    NODESEEK_FAILURE_1("签到响应异常"),
    NODESEEK_UNKNOWN_FAILURE("未知异常"),
    ;

    final String message;

    TaskStatusEnum(String message) {
        this.message = message;
    }
}
