package com.tzmall.operations.util;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * nacos工具类
 *
 * @author wrx
 * @date 2023/5/15 16:25
 */
@Slf4j
public class NacosUtil {

    /**
     * 校验服务健康
     */
    public static List<InstanceVO> checkService(String host) {
        String url = host + "/v1/ns/catalog/services?withInstances=false&pageNo=1&pageSize=100&keyword=&namespaceId=";
        String body = HttpUtil.get(url);
        log.info(body);
        JSONObject jsonObject = JSONUtil.parseObj(body);
        JSONArray serviceList = jsonObject.getJSONArray("serviceList");
        List<ServiceVO> serviceVOS = serviceList.toList(ServiceVO.class);
        log.info("正式商城服务总数：{}", serviceVOS.size());
        List<InstanceVO> warningInstanceList = new ArrayList<>();
        serviceVOS.forEach(serviceVO -> {
            log.info("目前检测的服务:{}",serviceVO.getName());
            String detailUrl = host + "/v1/ns/catalog/instances?serviceName=%s&groupName=tzmall&clusterName=DEFAULT&pageSize=10&pageNo=1&namespaceId=";
            detailUrl = String.format(detailUrl, serviceVO.getName());
            String result = HttpUtil.get(detailUrl);
            log.info("查询{}具体的实例信息响应(响应非json也不发预警邮件)：{}", serviceVO.getName(), result);
            if (JSONUtil.isTypeJSON(result)) {
                JSONObject object = JSONUtil.parseObj(result);
                JSONArray list = object.getJSONArray("list");
                List<InstanceVO> instanceVOS = list.toList(InstanceVO.class);
                instanceVOS.forEach(instanceVO -> {
                    if (instanceVO.getWeight() != 1 || !instanceVO.isEnabled() || !instanceVO.isHealthy()) {
                        log.info("商城服务{}不健康！！", instanceVO.getInstanceId());
                        warningInstanceList.add(instanceVO);
                    }
                });
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        return warningInstanceList;
    }

    @Data
    static class ServiceVO {
        /**
         * 服务名
         */
        String name;
        /**
         * 集群数
         */
        int clusterCount;
        /**
         * 实例数
         */
        int ipCount;
        /**
         * 健康数
         */
        int healthyInstanceCount;
    }

    @Data
    public static class InstanceVO {
        /**
         * ip
         */
        String ip;
        /**
         * 端口号
         */
        int port;
        /**
         * 权重
         */
        double weight;
        /**
         * 健康状态
         */
        boolean healthy;
        /**
         * 启用状态
         */
        boolean enabled;
        /**
         * 实例ID
         */
        String instanceId;
    }
}
