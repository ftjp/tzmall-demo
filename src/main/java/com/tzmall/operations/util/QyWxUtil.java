package com.tzmall.operations.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QyWxUtil {

    /**
     * 企业微信推送地址
     */
    private static String qyapiUrl = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=%s";

    /**
     * 推送md格式消息
     *
     * @param msg 消息
     */
    public static void sendMdMsg(String key, String msg) {
        sendMsg(msg, key, "markdown");
    }

    public static void sendMsg(String key, String msg) {
        sendMsg(msg, key, "text");
    }

    public static void sendMsg(String msg, String key, String type) {
        JSONObject msgObject = new JSONObject();
        msgObject.put("msgtype", type);
        msgObject.put(type, JSONUtil.createObj().put("content", StrUtil.subPre(msg, 5000)));
        log.info("消息推送:{}", msgObject.toString());
        String post = HttpUtil.post(String.format(qyapiUrl, key), msgObject.toString());
        log.info("消息推送结果:{}", post);
    }

}
