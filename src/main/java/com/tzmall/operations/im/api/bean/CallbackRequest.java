package com.tzmall.operations.im.api.bean;

import lombok.Data;

@Data
public class CallbackRequest {

    /**
     * 属性请参考具体的回调类型
     */
    private String body;
    private String eventType;
    private String callbackExt;
    private String fromAccount;
    private String fromClientType;
    private String fromDeviceId;
    private String fromNick;
    private String msgTimestamp;
    private String msgType;
    private String msgidClient;
    private String to;
    private String fromClientIp;
    private String fromClientPort;

}
