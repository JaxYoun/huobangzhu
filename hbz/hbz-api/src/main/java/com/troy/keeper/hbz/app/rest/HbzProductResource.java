package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.app.dto.HbzProductMAPEDDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzProductDTO;
import com.troy.keeper.hbz.dto.HbzWareInfoDTO;
import com.troy.keeper.hbz.dto.HbzWareTypeDTO;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.po.HbzProduct;
import com.troy.keeper.hbz.po.HbzWareInfo;
import com.troy.keeper.hbz.service.EntityService;
import com.troy.keeper.hbz.service.HbzWareTypeService;
import com.troy.keeper.hbz.service.mapper.HbzProductMapper;
import com.troy.keeper.hbz.service.mapper.HbzWareInfoMapper;
import com.troy.keeper.hbz.service.mapper.HbzWareTypeMapper;
import com.troy.keeper.hbz.sys.Bean2Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/12/14.
 */
@RestController
@RequestMapping("/api/product")
public class HbzProductResource {

    @Autowired
    EntityService entityService;

    @Autowired
    HbzProductMapper mapper;

    @Autowired
    HbzWareInfoMapper info;

    @Autowired
    HbzWareTypeMapper wareTypeMapper;

    @Autowired
    HbzWareTypeService hbzWareTypeService;

    //上架商品
    //@RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseDTO publicProduct(@RequestBody HbzProductMAPEDDTO productMAPEDDTO) {
        String[] errs = ValidationHelper.valid(productMAPEDDTO, "product_cr");
        if (errs.length > 0)
            return new ResponseDTO(Const.STATUS_ERROR, "错误", errs);
        HbzProductDTO product = new HbzProductDTO();
        new Bean2Bean().copyProperties(productMAPEDDTO, product);
        HbzWareInfoDTO wareInfo = entityService.get(HbzWareInfo.class, info, product.getWareId());
        if (wareInfo == null)
            return new ResponseDTO(Const.STATUS_ERROR, "商品id无效");
        if (product.getIndex() == null)
            product.setIndex(0);
        if (product.getAmount() == null)
            product.setAmount(0.000);
        product.setLeave(product.getTotal());
        product.setRecommend(0);
        product.setStatus(Const.STATUS_ENABLED);
        product = entityService.save(product, mapper);
        if (product != null)
            return new ResponseDTO(Const.STATUS_OK, "成功");
        return new ResponseDTO(Const.STATUS_ERROR, "错误");
    }

    //下架商品
    //@RequestMapping(value = "/destroy", method = RequestMethod.POST)
    public ResponseDTO destroyProduct(@RequestBody HbzProductMAPEDDTO productMAPEDDTO) {
        String[] errs = ValidationHelper.valid(productMAPEDDTO, "product_de");
        if (errs.length > 0)
            return new ResponseDTO(Const.STATUS_ERROR, "错误", errs);
        HbzProductDTO product = entityService.get(HbzProduct.class, mapper, productMAPEDDTO.getId());
        if (product == null)
            return new ResponseDTO(Const.STATUS_ERROR, "商品id无效");
        product.setStatus(Const.STATUS_DISABLED);
        product = entityService.save(product, mapper);
        if (product != null)
            return new ResponseDTO(Const.STATUS_OK, "成功");
        return new ResponseDTO(Const.STATUS_ERROR, "错误");
    }

    //查
    @RequestMapping(value = "/attach", method = RequestMethod.POST)
    public ResponseDTO attachProduct(@RequestBody HbzProductMAPEDDTO productMAPEDDTO) {
        String[] errs = ValidationHelper.valid(productMAPEDDTO, "product_a");
        if (errs.length > 0)
            return new ResponseDTO(Const.STATUS_ERROR, "错误", errs);
        HbzProductDTO product = entityService.get(HbzProduct.class, mapper, productMAPEDDTO.getId());
        if (product == null)
            return new ResponseDTO(Const.STATUS_ERROR, "商品id无效");
        return new ResponseDTO(Const.STATUS_OK, "成功", Optional.of(product).map(MapSpec::mapProduct));
    }

    //查询商品
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseDTO queryProduct(@RequestBody HbzProductMAPEDDTO productMAPEDDTO) {
        HbzProductDTO productQuery = new HbzProductDTO();

        new Bean2Bean().copyProperties(productMAPEDDTO, productQuery);
        productQuery.setStatus("1");
        productQuery.setState(1);

        if (productQuery.getTypeId() != null) {
            List<Long> ids = hbzWareTypeService.querySub(productQuery.getTypeId()).stream().map(HbzWareTypeDTO::getId).collect(Collectors.toList());
            if (ids.size() > 0) {
                productQuery.setTypeId(null);
                productQuery.setTypeIds(ids);
            }
        }

        List<HbzProductDTO> list = entityService.query(HbzProduct.class, mapper, productQuery);
        return new ResponseDTO(Const.STATUS_OK, "成功", list.stream().map(MapSpec::mapProduct).collect(Collectors.toList()));
    }

    //查询商品规格
    @RequestMapping(value = "/querySpecificationses", method = RequestMethod.POST)
    public ResponseDTO queryProductSpecificationses(@RequestBody HbzProductMAPEDDTO productMAPEDDTO) {
        HbzProductDTO productQuery = new HbzProductDTO();

        new Bean2Bean().copyProperties(productMAPEDDTO, productQuery);
        productQuery.setStatus("1");
        productQuery.setState(1);
        productQuery.setProductStatus(1);

        if (productQuery.getTypeId() != null) {
            List<Long> ids = hbzWareTypeService.querySub(productQuery.getTypeId()).stream().map(HbzWareTypeDTO::getId).collect(Collectors.toList());
            if (ids.size() > 0) {
                productQuery.setTypeId(null);
                productQuery.setTypeIds(ids);
            }
        }

        List<String> list = entityService.query(HbzProduct.class, product -> product.getWare().getSpecifications(), productQuery, Arrays.asList("id", "ware"));
        return new ResponseDTO(Const.STATUS_OK, "成功", list.stream().distinct().collect(Collectors.toList()));
    }


    @RequestMapping(value = "/queryPage", method = RequestMethod.POST)
    public ResponseDTO queryPageProduct(@RequestBody HbzProductMAPEDDTO productMAPEDDTO) {
        HbzProductDTO productQuery = new HbzProductDTO();

        new Bean2Bean().copyProperties(productMAPEDDTO, productQuery);
        productQuery.setStatus("1");
        productQuery.setState(1);
        productQuery.setProductStatus(1);

        if (productQuery.getTypeId() != null) {
            List<Long> ids = hbzWareTypeService.querySub(productQuery.getTypeId()).stream().map(HbzWareTypeDTO::getId).collect(Collectors.toList());
            if (ids.size() > 0) {
                productQuery.setTypeId(null);
                productQuery.setTypeIds(ids);
            }
        }

        Page<HbzProductDTO> page = entityService.queryPage(HbzProduct.class, mapper, productQuery.getPageRequest(), productQuery);
        return new ResponseDTO(Const.STATUS_OK, "成功", page.map(MapSpec::mapProduct));
    }
}
