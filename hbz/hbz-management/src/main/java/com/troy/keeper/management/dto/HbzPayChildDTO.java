package com.troy.keeper.management.dto;

import com.troy.keeper.hbz.dto.HbzPayDTO;

/**
 * @author 李奥
 * @date 2017/12/14.
 */
public class HbzPayChildDTO extends HbzPayDTO {

    private String payTypeValue;

    public String getPayTypeValue() {
        return payTypeValue;
    }

    public void setPayTypeValue(String payTypeValue) {
        this.payTypeValue = payTypeValue;
    }
}
