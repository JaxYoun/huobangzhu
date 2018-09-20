package com.troy.keeper.hbz.app.dto;

import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.type.PayType;
import com.troy.keeper.hbz.type.Role;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by leecheng on 2017/12/4.
 */
@Getter
@Setter
public class HbzRechargeMapDTO extends HbzBaseMapDTO {

    private Long id;
    private PayType payType;
    private String chargeNo;

    //关联用户
    private HbzUserDTO user;
    private Long userId;

    private Double money;

    private Long executeDate;

    private Integer state;

    private Role role;

}
