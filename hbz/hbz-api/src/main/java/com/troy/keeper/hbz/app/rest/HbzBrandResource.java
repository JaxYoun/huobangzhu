package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.app.dto.HbzBrandMapDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzBrandDTO;
import com.troy.keeper.hbz.po.HbzBrand;
import com.troy.keeper.hbz.service.EntityService;
import com.troy.keeper.hbz.service.mapper.HbzBrandMapper;
import com.troy.keeper.hbz.sys.annotation.Label;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/12/21.
 */
@RestController
@RequestMapping("/api/brand")
public class HbzBrandResource {

    @Autowired
    EntityService entityService;

    @Autowired
    HbzBrandMapper brandMapper;


    /**
     * 得到品牌
     *
     * @param brandMapDTO
     * @return
     */
    @Label("App端 - 积分商城 - 品牌 - 详细信息")
    @RequestMapping(value = {"/get"}, method = RequestMethod.POST)
    public ResponseDTO getBrand(@RequestBody HbzBrandMapDTO brandMapDTO) {
        HbzBrandDTO query = new HbzBrandDTO();
        query.setId(brandMapDTO.getId());
        HbzBrandDTO brand = entityService.get(HbzBrand.class, brandMapper, query.getId());
        return new ResponseDTO(Const.STATUS_OK, "成功", Optional.of(brand).map(MapSpec::mapBrand).get());
    }

    /**
     * 查询品牌
     *
     * @param brandMapDTO
     * @return
     */
    @Label("App端 - 积分商城 - 品牌 - 查询")
    @RequestMapping(value = {"/query"}, method = RequestMethod.POST)
    public ResponseDTO queryBrand(@RequestBody HbzBrandMapDTO brandMapDTO) {
        HbzBrandDTO query = new HbzBrandDTO();
        BeanUtils.copyProperties(brandMapDTO, query);
        query.setStatus(Const.STATUS_ENABLED);
        List<HbzBrandDTO> list = entityService.query(HbzBrand.class, brandMapper, query);
        return new ResponseDTO(Const.STATUS_OK, "成功", list.stream().map(MapSpec::mapBrand).collect(Collectors.toList()));
    }


    /**
     * 分页品牌
     *
     * @param brandMapDTO
     * @return
     */
    @Label("App端 - 积分商城 - 品牌 - 分页查询")
    @RequestMapping(value = {"/queryPage"}, method = RequestMethod.POST)
    public ResponseDTO queryPageBrand(@RequestBody HbzBrandMapDTO brandMapDTO) {
        HbzBrandDTO query = new HbzBrandDTO();
        BeanUtils.copyProperties(brandMapDTO, query);
        query.setStatus(Const.STATUS_ENABLED);
        Page<HbzBrandDTO> page = entityService.queryPage(HbzBrand.class, brandMapper, query.getPageRequest(), query);
        return new ResponseDTO(Const.STATUS_OK, "成功", page.map(MapSpec::mapBrand));
    }
}
