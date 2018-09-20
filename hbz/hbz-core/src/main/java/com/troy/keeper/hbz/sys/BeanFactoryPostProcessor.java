package com.troy.keeper.hbz.sys;

import com.troy.keeper.core.utils.JsonUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

import java.util.Iterator;

/**
 * Created by leecheng on 2017/10/25.
 */
@Component
public class BeanFactoryPostProcessor implements org.springframework.beans.factory.config.BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
    }
}
