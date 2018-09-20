package com.troy.keeper.core.config;

import com.troy.keeper.hbz.helper.freemarker.RemoteTemplateLoader;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.PostConstruct;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/3/2 11:41
 */
@Configuration
public class FreemarkerConfig {

    @Value("${staticImagePrefix}")
    private String remotePathPrefix;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    private WebApplicationContext applicationContext;

    @PostConstruct
    public void freeMarkerConfigurer() {
        freemarker.template.Configuration configuration = freeMarkerConfigurer.getConfiguration();
        RemoteTemplateLoader remoteTemplateLoader = new RemoteTemplateLoader(remotePathPrefix);
        ClassTemplateLoader classTemplateLoader = new ClassTemplateLoader(getClass(), "/WEB-INF/pages/");
        MultiTemplateLoader templateLoader = new MultiTemplateLoader(new TemplateLoader[]{classTemplateLoader, remoteTemplateLoader});
        configuration.setTemplateLoader(templateLoader);
    }
}