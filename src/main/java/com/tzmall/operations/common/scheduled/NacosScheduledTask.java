package com.tzmall.operations.common.scheduled;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.json.JSONUtil;
import com.tzmall.operations.config.OperationsProperties;
import com.tzmall.operations.util.NacosUtil;
import com.tzmall.operations.util.QyWxUtil;
import com.tzmall.operations.util.ServiceStartupUtil;
import com.tzmall.operations.util.db.PreRecordDBUtil;
import com.tzmall.operations.util.db.ProRecordDBUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;

/**
 * Nacos检查定时任务
 *
 * @Author: wrx
 * @Date: 2023/05/15 14:11
 */
@Configuration
@EnableScheduling
@Slf4j
public class NacosScheduledTask {

    /**
     * 上次邮件预警时间
     * 邮件预警成功后更新此时间
     */
    static DateTime lastWarningTime = DateUtil.yesterday();

    /**
     * 预警间隔时间，单位分钟
     * 此时间段内预警过，就不再预警
     */
    @Value("${tzmall.nacos.warningIntervalMinute:60}")
    int warningIntervalMinute;

    /**
     * 预警邮件
     */
    @Value("${tzmall.nacos.warningEmail}")
    String warningEmail;

    @Value("${tzmall.nacos.host}")
    String host;

    /**
     * 公共配置
     */
    @Autowired
    OperationsProperties operationsProperties;


    /**
     * 定时任务
     */
    @Scheduled(cron = "0 0/5 8-23 * * ?")
    private void configureTasks() {
        log.info("商城Nacos实例健康检查定时任务开始");
        List<NacosUtil.InstanceVO> instanceVOS = NacosUtil.checkService(host);
        if (CollUtil.isEmpty(instanceVOS)) {
            log.info("商城Nacos检查结果正常");
        } else {
            log.info("商城Nacos检查存在异常实例数{}", instanceVOS.size());
            // 发送邮件预警
            if (DateUtil.compare(new Date(), DateUtil.offsetMinute(lastWarningTime, warningIntervalMinute)) > 0) {
                instanceVOS.forEach(instanceVO -> {
                    log.info("商城实例{}邮件预警", instanceVO.getInstanceId());
                    List<String> warningEmails = StrUtil.split(warningEmail, StrUtil.COMMA);
                    for (String email : warningEmails) {
                        if (StrUtil.isNotBlank(email)) {
                            log.info("向{}推送邮件预警消息", email);
                            try {
                                MailUtil.send(email, String.format("商城nacos服务%s健康预警", instanceVO.getInstanceId()), String.format("%s,服务%s不健康,请排查！！实例详情:%s", DateUtil.now(), instanceVO.getInstanceId(), JSONUtil.toJsonStr(instanceVO)), false);
                            } catch (Exception e) {
                                log.info(ExceptionUtil.stacktraceToString(e));
                            }
                        }
                    }
                });
                // 更新最新预警时间
                lastWarningTime = new DateTime();
            } else {
                log.info("已发送邮件预警，{}分钟内不再预警", warningIntervalMinute);
            }
        }
    }


    // 定义上一次查询的结束时间变量
    private volatile Date lastCheckEndTime = null;

    /**
     * 定时任务
     */
    @Scheduled(cron = "0 0/1 8-23 * * ?")
    private void checkService() {
        // 服务启停检查
        try {
            this.checkServiceStartup();
        } catch (Exception e) {
            log.error(ExceptionUtil.stacktraceToString(e));
        }
    }

    /**
     * 监测服务启动、销毁信息
     */
    public void checkServiceStartup() throws Exception {
        log.info("服务启停检查定时任务开始");
        // 计算查询时间范围
        Date now = DateUtil.date();
        String beginDate = lastCheckEndTime == null ? DateUtil.formatDateTime(DateUtil.offsetMinute(now, -2)) : DateUtil.formatDateTime(lastCheckEndTime);
        String endDate = DateUtil.formatDateTime(now);
        // 更新上一次查询的结束时间
        lastCheckEndTime = now;
        log.info("查询时间段：{}={}", beginDate, endDate);

        // 正式
        ProRecordDBUtil proDBUtil = ProRecordDBUtil.getInstance(ProRecordDBUtil.DB_PRO_FILENAME);
        String msg = ServiceStartupUtil.checkServiceStartup(proDBUtil.getConnection(), beginDate, endDate);
        if (operationsProperties.isServiceStartupEnable() && StrUtil.isNotBlank(msg)) {
            // 推送给组长、运维群
            log.info("推送给组长、运维群");
            QyWxUtil.sendMdMsg(operationsProperties.getLeaderKey(), msg);
            QyWxUtil.sendMdMsg(operationsProperties.getGuardKey(), msg);
        }

        // 预发布
        PreRecordDBUtil preDBUtil = PreRecordDBUtil.getInstance(PreRecordDBUtil.DB_PRE_FILENAME);
        msg = ServiceStartupUtil.checkServiceStartup(preDBUtil.getConnection(), beginDate, endDate);
        if (operationsProperties.isPreServiceStartupEnable() && StrUtil.isNotBlank(msg)) {
            // 推送给平台测试/预发布群
            log.info("推送给平台测试/预发布群");
            QyWxUtil.sendMdMsg(operationsProperties.getPlatformKey(), msg);
        }
        log.info("服务启停检查定时任务结束");
    }

}
