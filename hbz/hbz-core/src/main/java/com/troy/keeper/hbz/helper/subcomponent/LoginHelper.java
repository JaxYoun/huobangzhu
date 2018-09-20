package com.troy.keeper.hbz.helper.subcomponent;

/**
 * Created by leecheng on 2017/11/9.
 */
public class LoginHelper {

    private static final ThreadLocal<String> loginFor = new ThreadLocal<>();
    private static final ThreadLocal<String> language = new ThreadLocal<>();

    public static void setLanguage(String language) {
        LoginHelper.language.set(language);
    }

    public static String getLanguage() {
        return LoginHelper.language.get();
    }

    public static void setLoginFor(String loginFor) {
        LoginHelper.loginFor.set(loginFor);
    }

    public static String getLoginFor() {
        return LoginHelper.loginFor.get();
    }

}
