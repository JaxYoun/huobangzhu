package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.sys.annotation.Label;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by leecheng on 2017/11/2.
 */
@CrossOrigin
@RestController
public class CommonResource {

    @Label("App端 - 会话获取")
    @RequestMapping("/api/session/token")
    public ResponseDTO createdSession(HttpServletRequest request) {
        return new ResponseDTO(Const.STATUS_OK, "完成!", request.getSession().getId());
    }

}
