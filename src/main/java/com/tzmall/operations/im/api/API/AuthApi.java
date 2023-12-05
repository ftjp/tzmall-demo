package com.tzmall.operations.im.api.API;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.tzmall.operations.im.api.bean.*;
import com.tzmall.operations.im.support.ImUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static com.tzmall.operations.im.support.UrlConst.*;

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
    public static YunXinUser createUser() throws IOException {
        YunXinUser user = new YunXinUser();
        user.setAccid("10000");
        user.setName("在线客服");
        YunXinResponse yunXinResponse = ImUtil.postUtil(user, USER_CREATE_ID_URL);
        return BeanUtil.copyProperties(JSONUtil.parseObj(yunXinResponse.getInfo()), YunXinUser.class);
    }

    /**
     * 更新网易云通信token 此方法只会返回状态值
     *
     * @return
     * @throws IOException
     */
    public static void updateToken() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        YunXinUser user = new YunXinUser();
        user.setAccid("9");
        user.setToken("123456789");
        ImUtil.postUtil(user, UPDATE_TOKEN_URL);
    }

    /**
     * 重置网易云通信token
     *
     * @return
     * @throws IOException
     */
    public static String refreshToken() throws IOException {
        YunXinUser user = new YunXinUser();
        user.setAccid("9");
        YunXinResponse yunXinResponse = ImUtil.postUtil(user, REFRESH_TOKEN_URL);
        return BeanUtil.copyProperties(JSONUtil.parseObj(yunXinResponse.getInfo()), YunXinUser.class).getToken();
    }

    /**
     * 封禁网易云通信ID
     *
     * @return
     * @throws IOException
     */
    public static void block() throws IOException {
        YunXinUser user = new YunXinUser();
        user.setAccid("10");
        ImUtil.postUtil(user, BLOCK_USER_ID_URL);
    }

    /**
     * 解禁网易云通信ID
     *
     * @return
     * @throws IOException
     */
    public static void unblock() throws IOException {
        YunXinUser user = new YunXinUser();
        user.setAccid("10");
        ImUtil.postUtil(user, UNBLOCK_USER_ID_URL);
    }


    public static void main(String[] args) {

        try {
            createUser();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
