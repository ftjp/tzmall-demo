package com.tzmall.operations.test;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

/**
 * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/qrcode-link/url-scheme/generateScheme.html
 *
 */
@Data
public class WxSchemeDTO {

    /**
     * 跳转到的目标小程序信息。
     */
    @Alias("jump_wxa")
    private JumpWxa jumpWxa;

    @Alias("expire_time")
    private Integer expireTime;
    /**
     * 使用场景如下：
     * ①生成后的7天内有效。通过设置expire_type为1，并将expire_interval设置为7
     * ②指定某个时间过期。通过设置expire_type为1，并将expireTime设置为过期的时间戳
     */
    @Alias("expire_type")
    private Integer expireType;
    @Alias("expire_interval")
    private Integer expireInterval;

    /**
     * 跳转到的目标小程序信息。
     */
    @Data
    public static class JumpWxa{
        /**
         * 通过 scheme 码进入的小程序页面路径，必须是已经发布的小程序存在的页面，不可携带 query。path 为空时会跳转小程序主页。
         */
        private String path;
        /**
         * 通过 scheme 码进入小程序时的 query，最大1024个字符，只支持数字，大小写英文以及部分特殊字符：`!#$&'()*+,/:;=?@-._~%``
         */
        private String query;
        /**
         * 默认值"release"。要打开的小程序版本。正式版为"release"，体验版为"trial"，开发版为"develop"，仅在微信外打开时生效。
         */
        @Alias("env_version")
        private String envVersion;
    }
}


