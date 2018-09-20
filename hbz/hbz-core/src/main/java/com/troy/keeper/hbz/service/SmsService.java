package com.troy.keeper.hbz.service;

/**
 * Created by leecheng on 2017/10/25.
 */
public interface SmsService {

    boolean send(String phoneNo, String msg);

}
