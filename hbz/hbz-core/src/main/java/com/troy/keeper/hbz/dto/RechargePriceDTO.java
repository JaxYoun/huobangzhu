package com.troy.keeper.hbz.dto;

import com.troy.keeper.hbz.type.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2017/12/21 10:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RechargePriceDTO extends BaseDTO {

    private Role role;

    private Double price;

    private String roleName;

}
