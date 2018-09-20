package com.troy.keeper.hbz.sys;

import lombok.Data;

/**
 * Created by leecheng on 2018/2/8.
 */
@Data
public class Counter {

    private int count = 0;

    public Counter() {
    }

    public Counter(int initCount) {
        count = initCount;
    }

    public void countAdd() {
        count++;
    }

    public int getCount() {
        return count;
    }

}
