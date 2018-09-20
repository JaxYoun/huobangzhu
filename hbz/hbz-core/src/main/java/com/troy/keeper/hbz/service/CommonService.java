package com.troy.keeper.hbz.service;

import org.apache.commons.lang3.NotImplementedException;

/**
 * Created by leecheng on 2017/10/17.
 */
public interface CommonService {

    default Object txDo(Exe ex, Object in) {
        throw new NotImplementedException("不提供");
    }

    public static interface Exe {
        public Object txDo(Object in);
    }
}
