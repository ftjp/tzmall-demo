package com.tzmall.operations.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 配置
 */
@Getter
@Configuration
public class OperationsProperties {

    /**
     * 企业微信平台组长key
     */
    @Value("${tzmall.qywx.leaderKey}")
    private String leaderKey;

    /**
     * 企业微信平台运维key
     */
    @Value("${tzmall.qywx.guardKey}")
    private String guardKey;

    /**
     * 企业微信平台内部群key
     */
    @Value("${tzmall.qywx.platformKey}")
    private String platformKey;

    /**
     * 是否监听服务启动
     */
    @Value("${tzmall.serviceStartup.enable:true}")
    boolean serviceStartupEnable;

    /**
     * 是否监听预发布服务启动
     */
    @Value("${tzmall.preServiceStartup.enable:true}")
    boolean preServiceStartupEnable;

}
