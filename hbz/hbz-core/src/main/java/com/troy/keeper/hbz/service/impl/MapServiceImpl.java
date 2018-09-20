package com.troy.keeper.hbz.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.troy.keeper.hbz.dto.HbzAreaDTO;
import com.troy.keeper.hbz.dto.MapRouteMatrixDTO;
import com.troy.keeper.hbz.service.HbzAreaService;
import com.troy.keeper.hbz.service.MapService;
import com.troy.keeper.hbz.service.wrapper.AddInfo;
import com.troy.keeper.hbz.sys.annotation.Config;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leecheng on 2017/10/23.
 */
@CommonsLog
@Service
@Transactional
public class MapServiceImpl implements MapService {

    @Autowired
    private RestTemplate restTemplate;

    @Config("com.hbz.ak")
    private String ak;

    @Autowired
    HbzAreaService hbzAreaService;

    @PostConstruct
    private void init() {

    }

    @Override
    public Double[] getLocationByAddress(String address) {
        try {
            String url = "http://api.map.baidu.com/geocoder/v2/?address=" + address + "&output=json&ak=" + ak;
            HttpHeaders headers = new HttpHeaders();
            HttpEntity entity = new HttpEntity(headers);
            ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonObject json = new Gson().fromJson(resp.getBody(), JsonObject.class);
            return new Double[]{
                    json.getAsJsonObject("result").getAsJsonObject("location").get("lng").getAsDouble(),
                    json.getAsJsonObject("result").getAsJsonObject("location").get("lat").getAsDouble()
            };
        } catch (Exception e) {
            log.debug(e);
            return null;
        }
    }

    @Override
    public Map<String, Object> getLocation(String address) {
        try {
            Map<String, Object> ret = new LinkedHashMap<>();
            String url = "http://api.map.baidu.com/geocoder/v2/?address=" + address + "&output=json&ak=" + ak;
            HttpHeaders headers = new HttpHeaders();
            HttpEntity entity = new HttpEntity(headers);
            ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonObject json = new Gson().fromJson(resp.getBody(), JsonObject.class);
            ret.put("lng", json.getAsJsonObject("result").getAsJsonObject("location").get("lng").getAsDouble());
            ret.put("lat", json.getAsJsonObject("result").getAsJsonObject("location").get("lat").getAsDouble());
            ret.put("precise", json.getAsJsonObject("result").get("precise").getAsInt());
            ret.put("confidence", json.getAsJsonObject("result").get("confidence").getAsInt());
            ret.put("level", json.getAsJsonObject("result").get("confidence").getAsString());
            return ret;
        } catch (Exception e) {
            log.debug(e);
            return null;
        }
    }

    @Override
    public Map<String, Object> getLocation(Double lng, Double lat) {
        try {
            Map<String, Object> ret = new LinkedHashMap<>();
            String url = "http://api.map.baidu.com/geocoder/v2/?location=" + lat + "," + lng + "&output=json&pois=1&ak=" + ak;
            HttpHeaders headers = new HttpHeaders();
            HttpEntity entity = new HttpEntity(headers);
            ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonObject json = new Gson().fromJson(resp.getBody(), JsonObject.class);
            ret.put("formatted_address", json.getAsJsonObject("result").get("formatted_address").getAsString());
            ret.put("cityCode", json.getAsJsonObject("result").getAsJsonObject("addressComponent").get("adcode").getAsString());
            return ret;
        } catch (Exception e) {
            log.debug(e);
            return null;
        }
    }

    @Override
    public AddInfo getLocationX(Double lng, Double lat) {
        try {
            Map<String, Object> ret = new LinkedHashMap<>();
            String url = "http://api.map.baidu.com/geocoder/v2/?location=" + lat + "," + lng + "&output=json&pois=1&ak=" + ak;
            HttpHeaders headers = new HttpHeaders();
            HttpEntity entity = new HttpEntity(headers);
            ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonObject json = new Gson().fromJson(resp.getBody(), JsonObject.class);
            String formattedAddress = json.getAsJsonObject("result").get("formatted_address").getAsString();
            String cityCode = json.getAsJsonObject("result").getAsJsonObject("addressComponent").get("adcode").getAsString();
            return new AddInfo(lng, lat, formattedAddress, cityCode);
        } catch (Exception e) {
            log.debug(e);
            return null;
        }
    }

    @Override
    public Map<String, Object> route(MapRouteMatrixDTO routeMatrix) {
        try {
            Map<String, Object> ret = new LinkedHashMap<>();
            StringBuilder originSb = new StringBuilder();
            routeMatrix.getOrigins().forEach(add -> {
                originSb.append("|" + add.getLat() + "," + add.getLng());
            });
            String origins = originSb.toString().substring(1);
            StringBuilder destinationsSb = new StringBuilder();
            routeMatrix.getDestinations().forEach(add -> {
                destinationsSb.append("|" + add.getLat() + "," + add.getLng());
            });
            String destinations = destinationsSb.toString().substring(1);
            String url = "http://api.map.baidu.com/routematrix/v2/" + routeMatrix.getOperator() +
                    "?origins=" + origins +
                    "&destinations=" + destinations +
                    "&output=json" +
                    "&tactics=" + routeMatrix.getTactics() +
                    "&ak=" + ak;
            HttpHeaders headers = new HttpHeaders();
            HttpEntity entity = new HttpEntity(headers);
            ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonObject json = new Gson().fromJson(resp.getBody(), JsonObject.class);
            List<Map<String, Object>> result = new ArrayList<>();
            for (int i = 0; i < routeMatrix.getOrigins().size(); i++) {
                for (int j = 0; j < routeMatrix.getDestinations().size(); j++) {
                    int idx = i * routeMatrix.getDestinations().size() + j;
                    Map<String, Object> r = new LinkedHashMap<>();
                    Map<String, Object> from = new LinkedHashMap<>();
                    Map<String, Object> to = new LinkedHashMap<>();
                    Map<String, Object> fromData = getLocation(routeMatrix.getOrigins().get(i).getLng(), routeMatrix.getOrigins().get(i).getLat());
                    from.put("lng", routeMatrix.getOrigins().get(i).getLng());
                    from.put("lat", routeMatrix.getOrigins().get(i).getLat());
                    from.put("add", fromData.get("formatted_address"));
                    Map<String, Object> toD = getLocation(routeMatrix.getDestinations().get(j).getLng(), routeMatrix.getDestinations().get(j).getLat());
                    to.put("lng", routeMatrix.getDestinations().get(j).getLng());
                    to.put("lat", routeMatrix.getDestinations().get(j).getLat());
                    to.put("add", toD.get("formatted_address"));
                    r.put("origin", from);
                    r.put("destination", to);
                    String distance = json.getAsJsonArray("result").get(idx).getAsJsonObject().getAsJsonObject("distance").get("value").getAsString();
                    String duration = json.getAsJsonArray("result").get(idx).getAsJsonObject().getAsJsonObject("duration").get("value").getAsString();
                    r.put("distance", distance);
                    r.put("duration", duration);
                    r.put("distText", json.getAsJsonArray("result").get(idx).getAsJsonObject().getAsJsonObject("distance").get("text").getAsString());
                    r.put("duraText", json.getAsJsonArray("result").get(idx).getAsJsonObject().getAsJsonObject("duration").get("text").getAsString());
                    result.add(r);
                }
            }
            ret.put("result", result);
            return ret;
        } catch (Exception e) {
            log.debug(e);
            return null;
        }
    }

    @Override
    public HbzAreaDTO getAreaByLocation(Double lng, Double lat) {
        try {
            Map<String, Object> loc = getLocation(lng, lat);
            String outCode = (String) loc.get("cityCode");
            return hbzAreaService.findByOutCode(outCode);
        } catch (Exception e) {
            log.error("解析错误！", e);
            return null;
        }
    }

    @Override
    public String getLocationAddress(Double lng, Double lat) {
        Map<String, Object> address = getLocation(lng, lat);
        if (address == null) {
            return null;
        } else {
            return (String) address.get("formatted_address");
        }
    }
}
