package com.tzmall.operations.im.api.API;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.server.HttpServerRequest;
import cn.hutool.http.server.HttpServerResponse;
import cn.hutool.json.JSONUtil;
import com.sun.net.httpserver.Headers;
import com.tzmall.operations.im.api.bean.CallbackRequest;
import com.tzmall.operations.im.api.bean.CallbackResponse;
import com.tzmall.operations.im.api.bean.YunXinResponse;
import com.tzmall.operations.im.api.bean.YunXinUser;
import com.tzmall.operations.im.support.ImUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static cn.hutool.crypto.SecureUtil.sha1;
import static com.tzmall.operations.im.support.UrlConst.*;

/**
 * 网易云通信ID
 */
public class CallbackApi {
    private static Logger logger = LoggerFactory.getLogger(CallbackApi.class);

    /**
     * 回调处理接口
     *
     * @return
     * @throws IOException
     */
    public static void callback(HttpServerRequest request, HttpServerResponse response) throws IOException {
        String MD5 = ImUtil.getMD5(request.getBody());
        String curTime = String.valueOf(System.currentTimeMillis() / 1000);
        String CheckSum = ImUtil.getCheckSum(AppSecret, MD5, curTime);
        request.getHeaders().add("AppKey", AppKey);
        request.getHeaders().add("CurTime", curTime);
        request.getHeaders().add("MD5", MD5);
        request.getHeaders().add("CheckSum", CheckSum);
        request.getHeaders().add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        CallbackRequest callbackRequest = BeanUtil.copyProperties(JSONUtil.parseObj(request.getBody()), CallbackRequest.class);


        CallbackResponse callbackResponse = new CallbackResponse();
        callbackResponse.setErrCode("0");
        response.setContentType("application/json; charset=utf-8");
        response.write(JSONUtil.toJsonStr(callbackResponse));

    }


}
