package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.security.SecurityUtils;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.management.dto.HbzVersionManagementDTO;
import com.troy.keeper.management.service.HbzVersionManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

/**
 * @Autohor: hecj
 * @Description: 移动端版本管理Resource
 * @Date: Created in 14:41  2018/2/1.
 * @Midified By:
 */
@RestController
@RequestMapping("/api/manager")
public class HbzVersionManagementResource {

    @Autowired
    private HbzVersionManagementService hbzVersionManagementService;


    /**
     * 分页列表查询
     * @param hbzVersionManagementDTO
     * @param pageable
     * @return
     */
    @PostMapping("/queryVersionManagementPage")
    public ResponseDTO queryVersionManagementPage(@RequestBody HbzVersionManagementDTO hbzVersionManagementDTO, Pageable pageable) {
        return new ResponseDTO("200", "分页查询成功", hbzVersionManagementService.queryVersionManagement(hbzVersionManagementDTO, pageable));
    }

    /**
     * 添加版本
     * @param hbzVersionManagementDTO
     * @return
     */
    @PostMapping("/addVersionManagement")
    public ResponseDTO addVersionManagement(@RequestBody HbzVersionManagementDTO hbzVersionManagementDTO) {
        if (null == hbzVersionManagementDTO.getVersionNo() || "".equals(hbzVersionManagementDTO.getVersionNo())) {
            return new ResponseDTO("401", "版本号不能为空！", null);
        }
        if (null == hbzVersionManagementDTO.getVersionName() || "".equals(hbzVersionManagementDTO.getVersionName())) {
            return new ResponseDTO("401", "版本名称不能为空！", null);
        }
        if (null == hbzVersionManagementDTO.getFileUrl() || "".equals(hbzVersionManagementDTO.getFileUrl())) {
            return new ResponseDTO("401", "版本路径不能为空！", null);
        }
        if (null == hbzVersionManagementDTO.getRecordComment() || "".equals(hbzVersionManagementDTO.getRecordComment())) {
            return new ResponseDTO("401", "版本描述不能为空！", null);
        }
        if (null == hbzVersionManagementDTO.getType() || "".equals(hbzVersionManagementDTO.getType())) {
            return new ResponseDTO("401", "版本类别不能为空！", null);
        }
        ResponseDTO responseDTO = null;
        if (!hbzVersionManagementService.addVersionManagement(hbzVersionManagementDTO)) {
            responseDTO = new ResponseDTO("500", "保存失败！", null);
        } else {
            responseDTO = new ResponseDTO("200", "保存成功！", null);
        }
        return responseDTO;
    }

    /**
     * 修改版本
     * @param hbzVersionManagementDTO
     * @return
     */
    @PostMapping("/updateVersionManagement")
    public ResponseDTO updateVersionManagement(@RequestBody HbzVersionManagementDTO hbzVersionManagementDTO) {
        if (null == hbzVersionManagementDTO.getId() || "".equals(hbzVersionManagementDTO.getId())) {
            return new ResponseDTO("401", "ID不能为空！", null);
        }
        if (null == hbzVersionManagementDTO.getVersionNo() || "".equals(hbzVersionManagementDTO.getVersionNo())) {
            return new ResponseDTO("401", "版本号不能为空！", null);
        }
        if (null == hbzVersionManagementDTO.getVersionName() || "".equals(hbzVersionManagementDTO.getVersionName())) {
            return new ResponseDTO("401", "版本名称不能为空！", null);
        }
        //if (null == hbzVersionManagementDTO.getFileUrl() || "".equals(hbzVersionManagementDTO.getFileUrl())) {
        //    return new ResponseDTO("401", "版本路径不能为空！", null);
        //}
        if (null == hbzVersionManagementDTO.getRecordComment() || "".equals(hbzVersionManagementDTO.getRecordComment())) {
            return new ResponseDTO("401", "版本描述不能为空！", null);
        }
        if (null == hbzVersionManagementDTO.getType() || "".equals(hbzVersionManagementDTO.getType())) {
            return new ResponseDTO("401", "版本类别不能为空！", null);
        }
        if (null == hbzVersionManagementDTO.getIsDisable() || "".equals(hbzVersionManagementDTO.getIsDisable())) {
            return new ResponseDTO("401", "版本是否禁用不能为空！", null);
        }
        ResponseDTO responseDTO = null;
        if (!hbzVersionManagementService.updateVersionManagement(hbzVersionManagementDTO)) {
            responseDTO = new ResponseDTO("500", "编辑失败！", null);
        } else {
            responseDTO = new ResponseDTO("200", "编辑成功！", null);
        }
        return responseDTO;
    }

    /**
     * 删除app版本
     * @param hbzVersionManagementDTO
     * @return
     */
    @PostMapping("/deleteVersionManagement")
    public ResponseDTO deleteVersionManagement(@RequestBody HbzVersionManagementDTO hbzVersionManagementDTO) {
        if (hbzVersionManagementDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "请传入id", null);
        }
        hbzVersionManagementDTO.setStatus(Const.STATUS_DISABLED);
        hbzVersionManagementDTO.setLastUpdatedBy(SecurityUtils.getCurrentUserId());
        hbzVersionManagementDTO.setLastUpdatedDate(Instant.now().getEpochSecond());
        if (this.hbzVersionManagementService.deleteVersionManagement(hbzVersionManagementDTO)) {
            return new ResponseDTO(Const.STATUS_OK, "删除成功", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "请传入id", null);
        }
    }

    /**
     * 是否禁用app版本 0 是 1 否
     * @param hbzVersionManagementDTO
     * @return
     */
    @PostMapping("/isDisableVersionManagement")
    public ResponseDTO isDisableVersionManagement(@RequestBody HbzVersionManagementDTO hbzVersionManagementDTO) {
        if (hbzVersionManagementDTO.getId() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "请传入id", null);
        }
        if (hbzVersionManagementDTO.getIsDisable() == null) {
            return new ResponseDTO(Const.STATUS_ERROR, "请传入是否禁用参数", null);
        }
        if (this.hbzVersionManagementService.isDisableVersionManagement(hbzVersionManagementDTO)) {
            return new ResponseDTO(Const.STATUS_OK, "操作成功", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败", null);
        }
    }

}
