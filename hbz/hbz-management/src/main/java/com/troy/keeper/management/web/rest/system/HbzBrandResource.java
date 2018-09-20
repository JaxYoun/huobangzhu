package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzAreaDTO;
import com.troy.keeper.hbz.dto.HbzBrandDTO;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.po.HbzBrand;
import com.troy.keeper.hbz.service.EntityService;
import com.troy.keeper.hbz.service.HbzAreaService;
import com.troy.keeper.hbz.service.HbzBrandService;
import com.troy.keeper.hbz.service.mapper.HbzBrandMapper;
import com.troy.keeper.hbz.sys.Bean2Bean;
import com.troy.keeper.management.dto.HbzBrandMapDTO;
import com.troy.keeper.management.utils.MapSpec;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
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
    HbzBrandService hbzBrandService;

    @Autowired
    EntityService entityService;

    @Autowired
    HbzBrandMapper brandMapper;

    @Autowired
    HbzAreaService hbzAreaService;

    /**
     * 得到品牌
     *
     * @param brandMapDTO
     * @return
     */
    @RequestMapping(value = {"/get"}, method = RequestMethod.POST)
    public ResponseDTO getBrand(@RequestBody HbzBrandMapDTO brandMapDTO) {
        HbzBrandDTO query = new HbzBrandDTO();
        query.setId(brandMapDTO.getId());
        HbzBrandDTO brand = entityService.get(HbzBrand.class, brandMapper, query.getId());
        return new ResponseDTO(Const.STATUS_OK, "成功", Optional.of(brand).map(MapSpec::mapBrand).get());
    }

    /**
     * 新增品牌
     *
     * @param brandMapDTO
     * @return
     */
    @RequestMapping(value = {"/create"}, method = RequestMethod.POST)
    public ResponseDTO createBrand(@RequestBody HbzBrandMapDTO brandMapDTO) {
        String[] err = ValidationHelper.valid(brandMapDTO, "h_brand_n");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "验证失败", err);
        }
        String brandNo = hbzBrandService.createBrandNo();
        HbzBrandDTO brand = new HbzBrandDTO();
        new Bean2Bean().copyProperties(brandMapDTO, brand);
        brand.setBrandNo(brandNo);
        if (brand.getAreaCode() != null) {
            HbzAreaDTO area = hbzAreaService.findByOutCode(brand.getAreaCode());
            if (area != null) {
                brand.setArea(area);
                brand.setAreaId(area.getId());
            } else {
                return new ResponseDTO(Const.STATUS_ERROR, "", "地区编号无效");
            }
        }
        brand.setStatus("1");
        if (brand.getIndex() == null) {
            brand.setIndex(0);
        }
        if (entityService.save(brand, brandMapper) != null) {
            return new ResponseDTO(Const.STATUS_OK, "保存成功");
        }
        return new ResponseDTO(Const.STATUS_ERROR, "错误");
    }

    /**
     * 更新品牌
     *
     * @param brandMapDTO
     * @return
     */
    @RequestMapping(value = {"/update"}, method = RequestMethod.POST)
    public ResponseDTO updateBrand(@RequestBody HbzBrandMapDTO brandMapDTO) {
        String[] err = ValidationHelper.valid(brandMapDTO, "h_brand_u");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "验证失败", err);
        }
        HbzBrandDTO brand = entityService.get(HbzBrand.class, brandMapper, brandMapDTO.getId());
        String brandNo = brand.getBrandNo();
        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(brandMapDTO, brand);
        brand.setBrandNo(brandNo);
        if (brand.getAreaCode() != null) {
            HbzAreaDTO area = hbzAreaService.findByOutCode(brand.getAreaCode());
            if (area != null) {
                brand.setArea(area);
                brand.setAreaId(area.getId());
            } else {
                return new ResponseDTO(Const.STATUS_ERROR, "", "地区编号无效");
            }
        }
        brand.setStatus("1");
        if (brand.getIndex() == null) {
            brand.setIndex(0);
        }
        if (entityService.save(brand, brandMapper) != null) {
            return new ResponseDTO(Const.STATUS_OK, "保存成功");
        }
        return new ResponseDTO(Const.STATUS_ERROR, "错误");
    }


    /**
     * 删除品牌
     *
     * @param brandMapDTO
     * @return
     */
    @RequestMapping(value = {"/delete"}, method = RequestMethod.POST)
    public ResponseDTO deleteBrand(@RequestBody HbzBrandMapDTO brandMapDTO) {
        String[] err = ValidationHelper.valid(brandMapDTO, "h_brand_d");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "验证失败", err);
        }
        HbzBrandDTO brand = entityService.get(HbzBrand.class, brandMapper, brandMapDTO.getId());
        brand.setStatus("0");
        if (entityService.save(brand, brandMapper) != null) {
            return new ResponseDTO(Const.STATUS_OK, "ok");
        }
        return new ResponseDTO(Const.STATUS_ERROR, "错误");
    }

    /**
     * 查询品牌
     *
     * @param brandMapDTO
     * @return
     */
    @RequestMapping(value = {"/query"}, method = RequestMethod.POST)
    public ResponseDTO queryBrand(@RequestBody HbzBrandMapDTO brandMapDTO) {
        HbzBrandDTO query = new HbzBrandDTO();
        BeanUtils.copyProperties(brandMapDTO, query);
        query.setStatus(Const.STATUS_ENABLED);
        List<HbzBrandDTO> list = entityService.query(HbzBrand.class, brandMapper, query, new Sort.Order(Sort.Direction.DESC, "createdDate"));
        return new ResponseDTO(Const.STATUS_OK, "成功", list.stream().map(MapSpec::mapBrand).collect(Collectors.toList()));
    }


    /**
     * 分页品牌
     *
     * @param brandMapDTO
     * @return
     */
    @RequestMapping(value = {"/queryPage"}, method = RequestMethod.POST)
    public ResponseDTO queryPageBrand(@RequestBody HbzBrandMapDTO brandMapDTO) {
        HbzBrandDTO query = new HbzBrandDTO();
        BeanUtils.copyProperties(brandMapDTO, query);
        query.setStatus(Const.STATUS_ENABLED);
        Page<HbzBrandDTO> page = entityService.queryPage(HbzBrand.class, brandMapper, query.getPageRequest(), query, new Sort.Order(Sort.Direction.DESC, "createdDate"));
        return new ResponseDTO(Const.STATUS_OK, "成功", page.map(MapSpec::mapBrand));
    }
}
