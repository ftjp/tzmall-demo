package com.tzmall.operations.im.api.bean;

import lombok.Data;

@Data
public class CallbackResponse {

    /**
     * 0：表示回调通过，允许执行
     * 1：表示回调不通过，取消执行。如果设置了合法的自定义错误码（responseCode），则发送端会收到自定义错误码，否则发送端会收到 403 错误码。
     */
    private String errCode;
    /**
     * 当 errCode 为 1 时有效
     * 取值范围： 20,000 至 20,099
     * 对于消息类型的第三方回调（eventType=1、2、6、22、41、72、73、74），支持设置为 200 的错误码，客户端表现为消息发送成功，其实消息发送失败
     */
    private String responseCode;
    /**
     * 看接口文档
     */
    private String modifyResponse;
    /**
     * 看接口文档
     */
    private String callbackExt;

}
