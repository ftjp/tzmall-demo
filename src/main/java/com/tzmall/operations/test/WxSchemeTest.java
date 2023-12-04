package com.tzmall.operations.test;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class WxSchemeTest {

    String successCode= "0";

    String appId = "wxc3cf5d135f445f8e";

    String secret = "1c6a83a7aec1706244ae214993525fea";
    /**
     * 凭证地址
     */
    String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={}&secret={}";
    /**
     * scheme地址
     */
    String schemeUrl = "https://api.weixin.qq.com/wxa/generatescheme?access_token={}";

    public static void main(String[] args) {
        WxSchemeTest wxSchemeTest = new WxSchemeTest();
        String accessToken = wxSchemeTest.getAccessToken();
//        String accessToken = "70_XhXWx9_3HV88jq8cm2yWkav_txE7e5rJlaHzax6-0MB1rD2uVU_HUJuM2atYVmb3UrWJpZrjnNiuYGBYTwWsTZfw5SENE0X1QBhfK71WzBJizKIJHUZSTHjIcCQVACbADAEJL";
        String scheme = wxSchemeTest.getScheme(accessToken);
        System.out.println("结果："+scheme);
    }

    /**
     * 获取AccessToken
     * <p>
     * 返回值示例：{"access_token":"70_HMDpV91wWbL4ixnCePwym9qmrvbdC0LXQR-_P640Vnbyx_SopB3DmRuGqgOAFiPInfOpanAVvxXaS05GLhJ2zSyM57rWPFa6LTe7nF2Ero4LCNVOEDGWyLtpEaIVUQdACAQPL","expires_in":7200}
     */
    public String getAccessToken() {
        String json = HttpUtil.get(StrUtil.format(accessTokenUrl, appId, secret), 5 * 1000);
        log.info("微信getAccessToken接口返回:{}", json);
        Map<String, Object> resMap = JSONUtil.toBean(json, new TypeReference<HashMap<String, Object>>() {}, false);
        String accessToken = MapUtil.get(resMap, "access_token", String.class);
        if (StrUtil.isBlank(accessToken)) {
            System.out.println("请求异常，请稍后再试");
        }
        return accessToken;
    }

    /**
     * 获取scheme码，适用于短信、邮件、外部网页、微信内等拉起小程序的业务场景。
     */
    public String getScheme(String accessToken) {
        WxSchemeDTO wxSchemeDTO = new WxSchemeDTO();
        WxSchemeDTO.JumpWxa jumpWxa = new WxSchemeDTO.JumpWxa();
        jumpWxa.setPath("pages/home/index");
        jumpWxa.setEnvVersion("develop");
        wxSchemeDTO.setJumpWxa(jumpWxa);
        wxSchemeDTO.setExpireType(1);
        wxSchemeDTO.setExpireInterval(1);


        System.out.println("格式:"+JSONUtil.toJsonStr(wxSchemeDTO));

        String json = HttpUtil.post(StrUtil.format(schemeUrl, accessToken),JSONUtil.toJsonStr(wxSchemeDTO), 5 * 1000);
        log.info("微信获取scheme码接口返回:{}", json);
        Map<String, Object> resMap = JSONUtil.toBean(json, new TypeReference<HashMap<String, Object>>() {}, false);
        String errCode = MapUtil.get(resMap, "errcode", String.class);
        if (StrUtil.isBlank(errCode) || !errCode.equals(successCode)) {
            System.out.println("异常："+MapUtil.get(resMap, "errmsg", String.class));
        }
        return MapUtil.get(resMap, "openlink", String.class);
    }
}
