package com.troy.keeper.hbz.sys;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/11/23.
 */
@Component
public class ApplicationContextHolder implements ApplicationContextAware, EnvironmentAware {

    private static ApplicationContext applicationContext;
    private static Environment environment;

    @Override
    public void setEnvironment(Environment env) {
        ApplicationContextHolder.environment = env;
    }

    public static String getProp(String name) {
        return environment.getProperty(name);
    }


    @Override

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHolder.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return ApplicationContextHolder.applicationContext;
    }

    public static <T> T getService(Class<T> service) {
        return ApplicationContextHolder.getApplicationContext().getBean(service);
    }
}
