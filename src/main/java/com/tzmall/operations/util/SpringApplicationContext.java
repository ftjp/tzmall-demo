package com.tzmall.operations.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SpringApplicationContext implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringApplicationContext.context = context;
    }

    public static ApplicationContext getContext() {
        return context;
    }

    /**
     * 获取 active profile
     */
    public static String getActiveProfile() {
        String[] profiles = new String[]{"pre"};
        try {
            AutowireCapableBeanFactory factory = getContext().getAutowireCapableBeanFactory();
            if (factory instanceof DefaultListableBeanFactory) {
                ConfigurableListableBeanFactory configurableListableBeanFactory = (ConfigurableListableBeanFactory) factory;
                Environment environment = configurableListableBeanFactory.getBean(Environment.class);
                profiles = environment.getActiveProfiles();
            }
        } catch (Exception e) {
            log.warn("Could not get active profiles, use default profile 'pre'");
        }
        log.info("当前读取的active profile：{}", profiles[0]);
        return profiles[0];
    }
}
