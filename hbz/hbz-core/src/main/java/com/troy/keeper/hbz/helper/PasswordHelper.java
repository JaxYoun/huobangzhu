package com.troy.keeper.hbz.helper;

/**
 * Created by leecheng on 2017/12/15.
 */
public class PasswordHelper {

    public static boolean isShort(char c) {
        if ((int) c > 255) return false;
        return true;
    }

}
