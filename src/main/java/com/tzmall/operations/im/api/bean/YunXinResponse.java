package com.tzmall.operations.im.api.bean;

import lombok.Data;

@Data
public class YunXinResponse {

    /**
     * 状态码
     */
    private String code;
    /**
     * 注册失败时返回的信息
     */
    private String desc;
    /**
     * 注册成功时返回的信息
     */
    private String info;

}
