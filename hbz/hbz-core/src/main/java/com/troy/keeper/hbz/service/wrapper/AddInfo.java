package com.troy.keeper.hbz.service.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by leecheng on 2018/3/29.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@lombok.extern.apachecommons.CommonsLog
public class AddInfo {

    private Double lng;
    private Double lat;
    private String formattedAddress;
    private String cityCode;
}
