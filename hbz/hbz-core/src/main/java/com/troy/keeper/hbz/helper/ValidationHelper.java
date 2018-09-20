package com.troy.keeper.hbz.helper;

import com.troy.keeper.hbz.consts.ValidConstants;
import com.troy.keeper.hbz.sys.annotation.Validation;
import com.troy.keeper.hbz.sys.annotation.ValueFormat;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by leecheng on 2017/7/7.
 */
public class ValidationHelper {

    public static final String[] valid(Object dto, String u) {
        try {
            List<String> r = new ArrayList<>();
            if (dto == null) {
                return new String[]{"参数没有"};
            } else {
                Class<?> cla = dto.getClass();
                while (cla != Object.class) {
                    List<Field> fs = new ArrayList<>();
                    fs.addAll(Arrays.asList(cla.getFields()));
                    fs.addAll(Arrays.asList(cla.getDeclaredFields()));
                    for (Field f : fs) {
                        ValueFormat v = f.getAnnotation(ValueFormat.class);
                        if (v == null) continue;
                        Validation[] validations = v.validations();
                        for (Validation validation : validations) {
                            String use = validation.use();
                            String msg = validation.msg().replace("{fieldName}", f.getName());
                            String format = validation.format();
                            String conf = validation.conf();
                            if (use.equals(u)) {
                                if (!f.isAccessible()) {
                                    f.setAccessible(true);
                                }
                                switch (format) {
                                    case ValidConstants.NOT_NULL: {
                                        if (f.get(dto) == null) {
                                            r.add(msg);
                                        } else if (f.getType() == String.class) {
                                            String va = (String) f.get(dto);
                                            if (StringHelper.isNullOREmpty(va)) {
                                                r.add(msg);
                                            } else {
                                                if (validation.min() > -1) {
                                                    if (va.length() < validation.min()) {
                                                        r.add("参数：" + f.getName() + " 小于度小长度" + validation.min());
                                                    }
                                                }
                                                if (validation.max() > -1) {
                                                    if (va.length() > validation.max()) {
                                                        r.add("参数：" + f.getName() + " 大于最大长度" + validation.max());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    break;
                                    case ValidConstants.DEFAULT_TIME: {
                                        if (f.get(dto) == null) {
                                            r.add(msg);
                                        } else {
                                            if (f.getType() == String.class) {
                                                String val = (String) f.get(dto);
                                                if (!val.matches("\\d{4}\\-\\d{2}\\-\\d{2}\\s\\d{2}\\:\\d{2}\\:\\d{2}")) {
                                                    r.add(msg);
                                                }
                                            } else if (f.getType() == Date.class) {
                                            } else {
                                                r.add(msg);
                                            }
                                        }
                                    }
                                    break;
                                    case ValidConstants.DEFAULT_DATE: {
                                        if (f.get(dto) == null) {
                                            r.add(msg);
                                        } else {
                                            if (f.getType() == String.class) {
                                                String val = (String) f.get(dto);
                                                if (!val.matches("\\d{4}\\-\\d{2}\\-\\d{2}")) {
                                                    r.add(msg);
                                                }
                                            } else if (f.getType() != Date.class) {
                                                r.add(msg);
                                            }
                                        }
                                    }
                                    break;
                                    case ValidConstants.NUMBER: {
                                        if (f.get(dto) == null) {
                                            r.add(msg);
                                        } else {
                                            if (f.getType() == String.class) {
                                                String val = (String) f.get(dto);
                                                if (!val.matches("\\d+(\\.\\d+)?")) {
                                                    r.add(msg);
                                                }
                                            }
                                        }
                                    }
                                    break;
                                    case ValidConstants.NULL: {
                                        if (f.get(dto) != null) {
                                            r.add(msg);
                                        }
                                    }
                                    break;
                                    case ValidConstants.REGEX: {
                                        if (f.get(dto) == null) {
                                            r.add(msg);
                                        } else {
                                            if (f.getType() == String.class) {
                                                String val = (String) f.get(dto);
                                                if (!val.matches(conf)) {
                                                    r.add(msg);
                                                }
                                            } else {
                                                r.add(msg);
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    cla = cla.getSuperclass();
                }
                String[] ms = new String[r.size()];
                return r.toArray(ms);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}
