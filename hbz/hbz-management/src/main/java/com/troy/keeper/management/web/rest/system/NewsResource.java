package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.management.dto.NewsDTO;
import com.troy.keeper.management.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Autohor: hecj
 * @Description: 后台平台信息Resource
 * @Date: Created in 9:55  2018/1/10.
 * @Midified By:
 */
@RestController
@RequestMapping("/api/manager")
public class NewsResource {

    @Autowired
    private NewsService newsService;

    @PostMapping("/getNewsListByPage")
    public ResponseDTO getNewsListByPage(@RequestBody NewsDTO newsDTO, Pageable pageable) {
        return new ResponseDTO("200", "查询成功",newsService.getNewsListByPage(newsDTO, pageable));
    }

    @PostMapping("/getNewsDetail")
    public ResponseDTO getNewsDetail(@RequestBody NewsDTO newsDTO) {
        if (newsDTO.getId() == null || "".equals(newsDTO.getId())) {
            return new ResponseDTO("401", "id不能为空！", null);
        }
        ResponseDTO responseDTO = null;
        NewsDTO newsDetailDTO = newsService.getNewsDetail(newsDTO);
        if (newsDetailDTO == null) {
            responseDTO = new ResponseDTO("500", "查询失败！", null);
        } else {
            responseDTO = new ResponseDTO("200", "查询成功！", newsDetailDTO);
        }
        return responseDTO;
    }

    @PostMapping("/addNews")
    public ResponseDTO addNews(@RequestBody NewsDTO newsDTO) {
        if(null==newsDTO.getContent() || "".equals(newsDTO.getContent())){
            return new ResponseDTO("401", "信息内容不能为空！", null);
        }
        if(null==newsDTO.getTitle() || "".equals(newsDTO.getTitle())){
            return new ResponseDTO("401", "信息标题不能为空！", null);
        }
        if(null==newsDTO.getPublishDate() || "".equals(newsDTO.getPublishDate())){
            return new ResponseDTO("401", "发布时间不能为空！", null);
        }
        if(null==newsDTO.getDisplayPublishDate() || "".equals(newsDTO.getDisplayPublishDate())){
            return new ResponseDTO("401", "是否显示时间不能为空！", null);
        }
        if(null==newsDTO.getNewsType() || "".equals(newsDTO.getNewsType())){
            return new ResponseDTO("401", "信息类型不能为空！", null);
        }
        if(null==newsDTO.getTitleImageList() || "".equals(newsDTO.getTitleImageList())){
            return new ResponseDTO("401", "信息标题图片不能为空！", null);
        }
        if(null==newsDTO.getSummary() || "".equals(newsDTO.getSummary())){
            return new ResponseDTO("401", "信息简介不能为空！", null);
        }
        ResponseDTO responseDTO = null;
        if (!newsService.addNews(newsDTO)) {
            responseDTO = new ResponseDTO("500", "添加失败！", null);
        } else {
            responseDTO = new ResponseDTO("200", "添加成功！", null);
        }
        return responseDTO;
    }

    @PostMapping("/updateNews")
    public ResponseDTO updateNews(@RequestBody NewsDTO newsDTO) {
        if (newsDTO.getId() == null || "".equals(newsDTO.getId())) {
            return new ResponseDTO("401", "id不能为空！", null);
        }
        if(null==newsDTO.getContent() || "".equals(newsDTO.getContent())){
            return new ResponseDTO("401", "信息内容不能为空！", null);
        }
        if(null==newsDTO.getTitle() || "".equals(newsDTO.getTitle())){
            return new ResponseDTO("401", "信息标题不能为空！", null);
        }
        if(null==newsDTO.getPublishDate() || "".equals(newsDTO.getPublishDate())){
            return new ResponseDTO("401", "发布时间不能为空！", null);
        }
        if(null==newsDTO.getDisplayPublishDate() || "".equals(newsDTO.getDisplayPublishDate())){
            return new ResponseDTO("401", "是否显示时间不能为空！", null);
        }
        if(null==newsDTO.getNewsType() || "".equals(newsDTO.getNewsType())){
            return new ResponseDTO("401", "信息类型不能为空！", null);
        }
        if(null==newsDTO.getTitleImageList() || "".equals(newsDTO.getTitleImageList())){
            return new ResponseDTO("401", "信息标题图片不能为空！", null);
        }
        if(null==newsDTO.getSummary() || "".equals(newsDTO.getSummary())){
            return new ResponseDTO("401", "信息简介不能为空！", null);
        }
        if(null==newsDTO.getNewsStatus() || "".equals(newsDTO.getNewsStatus())){
            return new ResponseDTO("401", "信息状态不能为空！", null);
        }
        if(null==newsDTO.getNewsNo() || "".equals(newsDTO.getNewsNo())){
            return new ResponseDTO("401", "信息编号不能为空！", null);
        }
        ResponseDTO responseDTO = null;
        if (!newsService.updateNews(newsDTO)) {
            responseDTO = new ResponseDTO("500", "修改失败！", null);
        } else {
            responseDTO = new ResponseDTO("200", "修改成功！", null);
        }
        return responseDTO;
    }


    @PostMapping("/updateNewsStatus")
    public ResponseDTO updateNewsStatus(@RequestBody NewsDTO newsDTO) {
        if (newsDTO.getId() == null || "".equals(newsDTO.getId())) {
            return new ResponseDTO("401", "id不能为空！", null);
        }
        if(null==newsDTO.getNewsStatus() || "".equals(newsDTO.getNewsStatus())){
            return new ResponseDTO("401", "信息状态不能为空！", null);
        }
        ResponseDTO responseDTO = null;
        if (!newsService.updateNewsStatus(newsDTO)) {
            responseDTO = new ResponseDTO("500", "修改失败！", null);
        } else {
            responseDTO = new ResponseDTO("200", "修改成功！", null);
        }
        return responseDTO;
    }

    /**
     * 删除图文信息
     * @param
     * @return
     */
    @PostMapping("/deleteNews")
    public ResponseDTO deleteNews(@RequestBody NewsDTO newsDTO) {
        if (newsDTO.getId() == null) {
            return new ResponseDTO("500", "请传入id", null);
        }
        if (newsService.deleteNews(newsDTO)) {
            return new ResponseDTO("200", "删除成功", null);
        } else {
            return new ResponseDTO("500", "请传入id", null);
        }
    }
}
