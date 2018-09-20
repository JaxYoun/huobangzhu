package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.app.dto.HbzRecommendProductMapDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzProductDTO;
import com.troy.keeper.hbz.dto.HbzRecommendProductDTO;
import com.troy.keeper.hbz.dto.HbzWareTypeDTO;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.po.HbzProduct;
import com.troy.keeper.hbz.po.HbzRecommendProduct;
import com.troy.keeper.hbz.service.EntityService;
import com.troy.keeper.hbz.service.HbzWareTypeService;
import com.troy.keeper.hbz.service.mapper.HbzProductMapper;
import com.troy.keeper.hbz.service.mapper.HbzRecommendProductMapper;
import com.troy.keeper.hbz.sys.Bean2Bean;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/12/15.
 */
@RestController
@RequestMapping("/api/recommendProduct")
public class HbzRecommendProductResource {

    @Autowired
    EntityService entityService;
    @Autowired
    HbzRecommendProductMapper mapper;
    @Autowired
    HbzProductMapper hbzProductMapper;
    @Autowired
    HbzWareTypeService hbzWareTypeService;

    /**
     * 新建推荐产品DTO
     *
     * @param productMapDTO
     * @return
     */
    //@RequestMapping(value = {"/create"}, method = RequestMethod.POST)
    @SneakyThrows
    public ResponseDTO crRecommendProduct(@RequestBody HbzRecommendProductMapDTO productMapDTO) {
        String[] errs = ValidationHelper.valid(productMapDTO, "recommend_product_cr");
        if (errs.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误", errs);
        }
        HbzRecommendProductDTO productDTO = new HbzRecommendProductDTO();
        new Bean2Bean().copyProperties(productMapDTO, productDTO);

        HbzProductDTO queryPro = new HbzProductDTO();
        queryPro.setStatus("1");
        queryPro.setId(productDTO.getProductId());
        Long count = entityService.count(HbzProduct.class, queryPro);
        if (count.equals(0L)) {
            return new ResponseDTO(Const.STATUS_ERROR, "商品id错误");
        }
        HbzProductDTO product = entityService.get(HbzProduct.class, hbzProductMapper, productDTO.getProductId());
        productDTO.setProduct(product);
        if (productDTO.getIndex() == null) productDTO.setIndex(0);
        productDTO.setStatus("1");
        if ((productDTO = entityService.save(productDTO, mapper)) != null) {
            Map<String, Object> re = new LinkedHashMap<>();
            re.put("id", productDTO.getId());
            re.put("product", productDTO.getProduct().getWareId());
            return new ResponseDTO(Const.STATUS_OK, "成功");
        }
        return new ResponseDTO(Const.STATUS_ERROR, "错误");
    }

    /**
     * 更新推荐产品DTO
     *
     * @param productMapDTO
     * @return
     */
    //@RequestMapping(value = {"/update"}, method = RequestMethod.POST)
    @SneakyThrows
    public ResponseDTO updateRecommendProduct(@RequestBody HbzRecommendProductMapDTO productMapDTO) {
        String[] errs = ValidationHelper.valid(productMapDTO, "recommend_product_upd");
        if (errs.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误", errs);
        }
        HbzRecommendProductDTO productDTO = entityService.get(HbzRecommendProduct.class, mapper, productMapDTO.getId());
        if (productDTO == null) {
            return new ResponseDTO<>(Const.STATUS_OK, "非法标识");
        }
        new Bean2Bean().addExcludeProp(Const.ID_AUDIT_FIELDS).copyProperties(productMapDTO, productDTO);
        HbzProductDTO queryPro = new HbzProductDTO();
        queryPro.setStatus("1");
        queryPro.setId(productDTO.getProductId());
        Long count = entityService.count(HbzProduct.class, queryPro);
        if (count.equals(0L)) {
            return new ResponseDTO(Const.STATUS_ERROR, "商品id错误");
        }
        HbzProductDTO product = entityService.get(HbzProduct.class, hbzProductMapper, productDTO.getProductId());
        productDTO.setProduct(product);
        if (productDTO.getIndex() == null) productDTO.setIndex(0);
        productDTO.setStatus("1");
        if ((productDTO = entityService.save(productDTO, mapper)) != null) {
            Map<String, Object> re = new LinkedHashMap<>();
            re.put("id", productDTO.getId());
            re.put("product", productDTO.getProduct().getWareId());
            return new ResponseDTO(Const.STATUS_OK, "成功");
        }
        return new ResponseDTO(Const.STATUS_ERROR, "错误");
    }

    /**
     * 查
     *
     * @param productMapDTO
     * @return
     */
    //@RequestMapping(value = {"/delete"}, method = RequestMethod.POST)
    @SneakyThrows
    public ResponseDTO deleteRecommendProduct(@RequestBody HbzRecommendProductMapDTO productMapDTO) {
        String[] errs = ValidationHelper.valid(productMapDTO, "recommend_product_d");
        if (errs.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误", errs);
        }
        HbzRecommendProductDTO productDTO = entityService.get(HbzRecommendProduct.class, mapper, productMapDTO.getId());
        if (productDTO == null) {
            return new ResponseDTO<>(Const.STATUS_OK, "非法标识");
        }
        productDTO.setStatus("0");
        entityService.save(productDTO, mapper);
        return new ResponseDTO(Const.STATUS_OK, "成功", Optional.of(productDTO).map(MapSpec::mapRecommendProduc));
    }

    /**
     * 查
     *
     * @param productMapDTO
     * @return
     */
    @RequestMapping(value = {"/attach"}, method = RequestMethod.POST)
    @SneakyThrows
    public ResponseDTO attachRecommendProduct(@RequestBody HbzRecommendProductMapDTO productMapDTO) {
        String[] errs = ValidationHelper.valid(productMapDTO, "recommend_product_att");
        if (errs.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误", errs);
        }
        HbzRecommendProductDTO productDTO = entityService.get(HbzRecommendProduct.class, mapper, productMapDTO.getId());
        if (productDTO == null) {
            return new ResponseDTO<>(Const.STATUS_OK, "非法标识");
        }
        return new ResponseDTO(Const.STATUS_OK, "成功", Optional.of(productDTO).map(MapSpec::mapRecommendProduc));
    }


    /**
     * 查询推荐商品
     *
     * @param productMapDTO
     * @return
     */
    @RequestMapping(value = {"/query"}, method = RequestMethod.POST)
    public ResponseDTO query(@RequestBody HbzRecommendProductMapDTO productMapDTO) {
        HbzRecommendProductDTO productQuery = new HbzRecommendProductDTO();
        new Bean2Bean().copyProperties(productMapDTO, productQuery);
        productQuery.setState(1);
        if (productQuery.getTypeId() != null) {
            List<Long> ids = hbzWareTypeService.querySub(productQuery.getTypeId()).stream().map(HbzWareTypeDTO::getId).collect(Collectors.toList());
            if (ids.size() > 0) {
                productQuery.setTypeId(null);
                productQuery.setTypeIds(ids);
            }
        }
        productQuery.setStatus("1");
        List<Sort.Order> orders = Arrays.asList(new Sort.Order(Sort.Direction.DESC, "index"));
        List<HbzRecommendProductDTO> list = entityService.query(HbzRecommendProduct.class, mapper, productQuery, orders.toArray(new Sort.Order[orders.size()]));
        return new ResponseDTO(Const.STATUS_OK, "操作成功", list.stream().map(MapSpec::mapRecommendProduc).collect(Collectors.toList()));
    }

    @RequestMapping(value = {"/queryPage"}, method = RequestMethod.POST)
    public ResponseDTO queryPage(@RequestBody HbzRecommendProductMapDTO productMapDTO) {
        HbzRecommendProductDTO productQuery = new HbzRecommendProductDTO();
        new Bean2Bean().copyProperties(productMapDTO, productQuery);
        productQuery.setState(1);
        if (productQuery.getTypeId() != null) {
            List<Long> ids = hbzWareTypeService.querySub(productQuery.getTypeId()).stream().map(HbzWareTypeDTO::getId).collect(Collectors.toList());
            if (ids.size() > 0) {
                productQuery.setTypeId(null);
                productQuery.setTypeIds(ids);
            }
        }
        productQuery.setStatus("1");
        List<Sort.Order> orders = Arrays.asList(new Sort.Order(Sort.Direction.DESC, "index"));
        Page<HbzRecommendProductDTO> page = entityService.queryPage(HbzRecommendProduct.class, mapper, productQuery.getPageRequest(), productQuery, orders.toArray(new Sort.Order[orders.size()]));
        return new ResponseDTO(Const.STATUS_OK, "操作成功", page.map(MapSpec::mapRecommendProduc));
    }


}
