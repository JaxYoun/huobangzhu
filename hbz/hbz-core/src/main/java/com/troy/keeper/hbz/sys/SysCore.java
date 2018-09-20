package com.troy.keeper.hbz.sys;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by leecheng on 2017/10/16.
 */
@Component
public class SysCore implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public Object getBean(String id) {
        return applicationContext.getBean(id);
    }

    public Object getBean(Class<?> type) {
        return applicationContext.getBean(type);
    }
}