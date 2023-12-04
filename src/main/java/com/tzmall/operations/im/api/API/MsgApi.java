package com.tzmall.operations.im.api.API;



import com.tzmall.operations.im.api.bean.SendMsg;
import com.tzmall.operations.im.api.bean.YunXinConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 发送消息
 */
public class MsgApi {
    private static Logger logger = LoggerFactory.getLogger(MsgApi.class);
    /**
     * 发送普通消息
     * @return
     * @throws IOException
     */
    public static String sendMsg(SendMsg sendMsg, YunXinConfig yunXinConfig) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
//        List<NameValuePair> params = Reflect.reflectTest(sendMsg);
//        //UTF-8编码,解决中文问题
//        HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
//        String res = NIMPost.postNIMServer(UrlConst.SEND_MSG_URL, entity, yunXinConfig.getAppKey(), yunXinConfig.getAppSecret());
//        logger.info("sendMsg httpRes: {}", res);
        return null;
    }

}
