package com.tzmall.operations.im.api.API;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.tzmall.operations.im.api.bean.UpdateToken;
import com.tzmall.operations.im.api.bean.YunXinConfig;
import com.tzmall.operations.im.api.bean.YunXinUser;
import com.tzmall.operations.im.support.UUIDUtil;
import com.tzmall.operations.im.support.UrlConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tzmall.operations.im.support.CheckSumBuilder.getCheckSum;
import static com.tzmall.operations.im.support.UrlConst.AppKey;
import static com.tzmall.operations.im.support.UrlConst.USER_CREATE_ID_URL;

/**
 * 网易云通信ID
 */
public class AuthApi {
    private static Logger logger = LoggerFactory.getLogger(AuthApi.class);

    /**
     * 创建网易云通信ID111
     *
     * @return
     * @throws IOException
     */
    public static String createUser(YunXinUser yunXinUser, YunXinConfig yunXinConfig) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        // 后台
        String nonce = UUIDUtil.getUUID();
        String curTime = String.valueOf(System.currentTimeMillis() / 1000);
        String checkSum = getCheckSum(yunXinConfig.getAppSecret(), nonce, curTime);

        // addHeader
//        HttpClientUtil.addHeader(post, "AppKey", appKey);
//        String nonce = UUIDUtil.getUUID();
//        String curTime = String.valueOf(System.currentTimeMillis() / 1000);
//        HttpClientUtil.addHeader(post, "Nonce", nonce);
//        HttpClientUtil.addHeader(post, "CurTime", curTime);
//        String checksum = getCheckSum(nonce, curTime, appSecret);
//        HttpClientUtil.addHeader(post, "CheckSum", checksum);


1

        //链式构建请求
        String result2 = HttpRequest.post(USER_CREATE_ID_URL)
                .header(Header.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")//头信息，多个头信息多次调用此方法即可
                .header("AppKey", AppKey)
                .header("Nonce", nonce)
                .header("CurTime", curTime)
                .header("CheckSum", checkSum)
                .body(JSONUtil.toJsonStr())//表单内容
                .timeout(20000)//超时，毫秒
                .execute().body();


    }




//    /**
//     * 创建网易云通信ID111
//     * @return
//     * @throws IOException
//     */
//    public static String createUser(YunXinUser yunXinUser, YunXinConfig yunXinConfig) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
//        List<NameValuePair> params = Reflect.reflectTest(yunXinUser);
//        //UTF-8编码,解决中文问题
//        HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
//        String res = NIMPost.postNIMServer(UrlConst.USER_CREATE_ID_URL, entity, yunXinConfig.getAppKey(), yunXinConfig.getAppSecret());
//        logger.info("createUser httpRes: {}", res);
//        return res;
//    }
//
//    /**
//     * 更新网易云通信token 此方法只会返回状态值
//     * @return
//     * @throws IOException
//     *
//     */
//    public static String updateToken(UpdateToken updateToken, YunXinConfig yunXinConfig) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
//        List<NameValuePair> params = Reflect.reflectTest(updateToken);
//        //UTF-8编码,解决中文问题
//        HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
//        String res = NIMPost.postNIMServer(UrlConst.UPDATE_TOKEN_URL, entity, yunXinConfig.getAppKey(), yunXinConfig.getAppSecret());
//        logger.info("updateToken httpRes: {}", res);
//        return res;
//    }
//
//    /**
//     * 重置网易云通信token
//     * @return
//     * @throws IOException
//     *
//     */
//    public static String refreshToken(String accid, YunXinConfig yunXinConfig) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("accid", accid));
//        //UTF-8编码,解决中文问题
//        HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
//        String res = NIMPost.postNIMServer(UrlConst.REFRESH_TOKEN_URL, entity, yunXinConfig.getAppKey(), yunXinConfig.getAppSecret());
//        logger.info("refreshToken httpRes: {}", res);
//        return res;
//    }

}
