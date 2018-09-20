package com.troy.keeper.hbz.helper;

/**
 * Created by leecheng on 2017/12/7.
 */
public class MathSupport {

    public int mod(int a, int b) {
        return a % b;
    }

    public int div(int a, int b) {
        return a / b;
    }

    public int minLtInt(double x) {
        int k = (int) x;
        double y = x - Double.valueOf(k).doubleValue();
        if (y > 0) return k + 1;
        return k;
    }

}
