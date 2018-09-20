package com.troy.keeper.hbz.service;

import com.troy.keeper.hbz.dto.SmLoginLogDTO;
import com.troy.keeper.hbz.vo.SmLoginLogVO;
import com.troy.keeper.system.domain.SmLoginLog;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/1/22 12:51
 */
public interface SmLoginLogServeic {

    /**
     * 获取日志详情
     *
     * @param smLoginLogDTO
     * @return
     */
    SmLoginLogVO getSmLogDetail(SmLoginLogDTO smLoginLogDTO);

    /**
     * 分页条件查询后台登录日志
     *
     * @param smLoginLogDTO
     * @param pageable
     * @return
     */
    Page<SmLoginLogVO> getSmLogListByPage(SmLoginLogDTO smLoginLogDTO, Pageable pageable);

    static SmLoginLogVO entityToVo(SmLoginLog smLoginLog) {
        SmLoginLogVO smLoginLogVO = new SmLoginLogVO();
        BeanUtils.copyProperties(smLoginLog, smLoginLogVO);
        return smLoginLogVO;
    }
}
