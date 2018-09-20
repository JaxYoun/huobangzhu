package com.troy.keeper.hbz.app.rest.web;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.NewsDTO;
import com.troy.keeper.hbz.service.NewsService;
import com.troy.keeper.hbz.vo.NewsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author：YangJx
 * @Description：资讯业务相关
 * @DateTime：2017/12/26 9:15
 */
@Slf4j
@RestController
@RequestMapping("/api/web/news")
public class WebNewsResource {

    @Autowired
    private NewsService newsService;

    /**
     * 根据资讯id获取资讯详情
     *
     * @param newsDTO
     * @return
     */
    @PostMapping("/getNewsDetail")
    public ResponseDTO getNewsDetail(@RequestBody NewsDTO newsDTO) {
        if (newsDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "id不能为空！", null);
        }

        ResponseDTO responseDTO;
        newsDTO.setStatus(Const.STATUS_ENABLED);
        NewsVO newsVOFromDB = newsService.getNewsDetail(newsDTO);
        if (newsVOFromDB == null) {
            responseDTO = new ResponseDTO(Const.STATUS_ERROR, "查询失败！", null);
        } else {
            responseDTO = new ResponseDTO(Const.STATUS_OK, "查询成功！", newsVOFromDB);
        }
        return responseDTO;
    }

    /**
     * @param newsDTO
     * @return
     */
    @PostMapping("/getNewsDetailViaMapper")
    public ResponseDTO getNewsDetailViaMapper(@RequestBody NewsDTO newsDTO) {
        if (newsDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "id不能为空！", null);
        }

        ResponseDTO responseDTO;
        NewsVO newsVOFromDB = newsService.getNewsDetailViaMapper(newsDTO);
        if (newsVOFromDB == null) {
            responseDTO = new ResponseDTO(Const.STATUS_ERROR, "查询失败！", null);
        } else {
            responseDTO = new ResponseDTO(Const.STATUS_OK, "查询成功！", newsVOFromDB);
        }
        return responseDTO;
    }

    /**
     * 分页条件查询资讯列表
     *
     * @param newsDTO
     * @return
     */
    @PostMapping("/getPublishedNewsListByPage")
    public ResponseDTO getPublishedNewsListByPage(@RequestBody NewsDTO newsDTO) {
        newsDTO.setStatus(Const.STATUS_ENABLED);
        //封装排序及分页
        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(new Sort.Order(Sort.Direction.DESC, "lastUpdatedDate"));
        orderList.add(new Sort.Order(Sort.Direction.DESC, "id"));
        Pageable pageable = new PageRequest(newsDTO.getPage(), newsDTO.getSize(), new Sort(orderList));
        return new ResponseDTO(Const.STATUS_OK, "成功！", this.newsService.getNewsListByPage(newsDTO, pageable));
    }

}
