package com.troy.keeper.hbz.dto;

import lombok.Data;

import java.util.List;

/**
 * Created by leecheng on 2017/11/30.
 */
@Data
public class MapRouteMatrixDTO {

    private String operator;
    private List<MapAddressDTO> origins;
    private List<MapAddressDTO> destinations;
    private Integer tactics;

}
