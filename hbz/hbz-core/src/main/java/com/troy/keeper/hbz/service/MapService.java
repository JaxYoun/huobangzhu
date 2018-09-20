package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.HbzAreaDTO;
import com.troy.keeper.hbz.dto.MapRouteMatrixDTO;
import com.troy.keeper.hbz.service.wrapper.AddInfo;

import java.util.Map;

/**
 * 地图服务
 * Created by leecheng on 2017/10/18.
 */
public interface MapService {

    Double[] getLocationByAddress(String address);

    String getLocationAddress(Double lng, Double lat);

    Map<String, Object> getLocation(String address);

    Map<String, Object> getLocation(Double lng, Double lat);

    Map<String, Object> route(MapRouteMatrixDTO routeMatrix);

    HbzAreaDTO getAreaByLocation(Double lng, Double lat);

    AddInfo getLocationX(Double lng, Double lat);
}
