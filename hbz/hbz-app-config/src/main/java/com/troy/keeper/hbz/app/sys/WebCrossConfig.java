package com.troy.keeper.hbz.app.sys;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/3/7 10:37
 */
@Configuration
public class WebCrossConfig extends WebMvcConfigurerAdapter {

    /**
     * {@inheritDoc}
     * <p>This implementation is empty.
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("**");
    }
}
