package com.troy.keeper.hbz.sys;

import com.troy.keeper.hbz.po.HbzConfig;
import com.troy.keeper.hbz.po.HbzSource;
import com.troy.keeper.hbz.repository.HbzConfigRepository;
import com.troy.keeper.hbz.repository.HbzSourceRepository;
import com.troy.keeper.hbz.sys.annotation.Boot;
import com.troy.keeper.hbz.sys.annotation.Config;
import com.troy.keeper.hbz.sys.annotation.Label;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by leecheng on 2017/9/27.
 */
@Component
public class BootstrapProcessor implements BeanPostProcessor {

    @Autowired
    private HbzConfigRepository hbzConfigRepository;

    @Autowired
    HbzSourceRepository hbzSourceRepository;

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        Class<?> clz = o.getClass();
        while (clz != Object.class) {

            if (clz.getAnnotation(RestController.class) != null || clz.getAnnotation(Controller.class) != null) {
                List<String> path = new ArrayList<>();
                RequestMapping requestMapping = clz.getAnnotation(RequestMapping.class);
                if (requestMapping != null) path.addAll(Arrays.asList(requestMapping.value()));
                Method[] methods = clz.getMethods();
                for (Method method : methods) {
                    RequestMapping rm = method.getAnnotation(RequestMapping.class);
                    PostMapping pm = method.getAnnotation(PostMapping.class);
                    GetMapping gm = method.getAnnotation(GetMapping.class);
                    if (rm != null || pm!=null || gm!=null) {
                        List<String> subs = new ArrayList<>();
                        subs.addAll(Arrays.asList(rm!=null?rm.value():pm!=null?pm.value():gm!=null?gm.value():new String[]{}));
                        if (path.size() == 0) {
                            for (String p : subs) {
                                if (hbzSourceRepository.countBySrc(p) < 1L) {
                                    Label label = method.getAnnotation(Label.class);
                                    String urlLabel;
                                    if (label != null) urlLabel = label.value();
                                    else urlLabel = clz.getName() + "." + method.getName();
                                    HbzSource src = new HbzSource();
                                    src.setPack(clz.getName() + "." + method.getName());
                                    src.setStatus("1");
                                    src.setLabel(urlLabel);
                                    src.setSrc(p);
                                    hbzSourceRepository.save(src);
                                }
                            }
                        } else {
                            for (String prefix : path) {
                                for (String p : subs) {
                                    if (hbzSourceRepository.countBySrc(prefix + p) < 1L) {
                                        Label label = method.getAnnotation(Label.class);
                                        String urlLabel;
                                        if (label != null) urlLabel = label.value();
                                        else urlLabel = clz.getName() + "." + method.getName();
                                        HbzSource src = new HbzSource();
                                        src.setStatus("1");
                                        src.setPack(clz.getName() + "." + method.getName());
                                        src.setLabel(urlLabel);
                                        src.setSrc(prefix + p);
                                        hbzSourceRepository.save(src);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            List<Field> fields = new ArrayList<>();
            fields.addAll(Arrays.asList(clz.getDeclaredFields()));
            fields.addAll(Arrays.asList(clz.getFields()));
            for (Field f : fields) {
                Config config = f.getAnnotation(Config.class);
                if (config != null) {
                    if (!f.isAccessible()) {
                        f.setAccessible(true);
                    }
                    try {
                        String val;
                        val = System.getProperty(config.value());
                        if (val == null) {
                            HbzConfig conf = hbzConfigRepository.findByKey(config.value());
                            if (conf != null) {
                                val = conf.getValue();
                            }
                        }

                        if (f.getType() == String.class) {
                            f.set(o, val);
                        } else if (f.getType() == Integer.class) {
                            f.set(o, Integer.valueOf(val));
                        } else if (f.getType() == int.class) {
                            f.set(o, Integer.parseInt(val));
                        } else if (f.getType() == Long.class) {
                            f.set(o, Long.valueOf(val));
                        } else if (f.getType() == long.class) {
                            f.set(o, Long.parseLong(val));
                        } else if (f.getType() == Double.class) {
                            f.set(o, Double.valueOf(val));
                        } else if (f.getType() == double.class) {
                            f.set(o, Double.parseDouble(val));
                        } else if (f.getType() == Float.class) {
                            f.set(o, Float.valueOf(val));
                        } else {
                            f.set(o, val);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            clz = clz.getSuperclass();
        }
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        Class<?> clz = o.getClass();
        while (clz != Object.class) {
            List<Method> ms = Arrays.asList(clz.getMethods());
            List<Method> mm = new ArrayList<>();
            mm.addAll(ms);
            for (Method m : mm) {
                Boot boot = m.getAnnotation(Boot.class);
                if (boot != null) {
                    int parameterCount = m.getParameterCount();
                    if (parameterCount == 0) {
                        try {
                            m.invoke(o);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            clz = clz.getSuperclass();
        }
        return o;
    }
}
