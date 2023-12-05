package com.tzmall.operations.im.support;

/**
 * 回调类型
 * @author Firrela
 * @time 2016/3/7.
 */
public enum EventTypeEnum {

    // IM 登录
    LOGIN("36", "登录回调"),
    // 单聊
    SINGLE_CHAT("1", "单聊消息回调"),
    // 群聊
    GROUP_CHAT("2", "群组消息回调"),
    GROUP_CHAT_CREATE("7", "创建群回调"),

    WITHDRAW("35", "消息撤回回调"),
    USER_INFO_MODIFY("36", "用户资料变更回调"),
    ;

    private String code;
    private String name;

    private EventTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }


}
