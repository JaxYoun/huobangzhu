package com.troy.keeper.management.web.rest.system;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.management.dto.HelpSendDTO;
import com.troy.keeper.management.service.HelpSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 李奥   帮我送
 * @date 2017/12/18.
 */
@RestController
public class HelpSendResource {

    @Autowired
    private HelpSendService helpSendService;

    //分页查询
    @RequestMapping("/api/manager/HelpSendPage")
    public ResponseDTO pageQureyDemo(@RequestBody HelpSendDTO helpSendDTO, Pageable pageable){
        return new ResponseDTO("200", "帮我送分页查询",helpSendService.findByCondition(helpSendDTO,pageable));
    }

    //详情按钮
    @RequestMapping("/api/manager/HelpSendDetails")
    public ResponseDTO HelpSendDetails(@RequestBody HelpSendDTO helpSendDTO){
        return new ResponseDTO("200", "帮我送详情按钮查询",helpSendService.findHelpSend(helpSendDTO));
    }

    //接单人详情
    @RequestMapping("/api/manager/takeDetailsSingle")
    public ResponseDTO teakUserDetails(@RequestBody HelpSendDTO helpSendDTO){
        return new ResponseDTO("200", "帮我送接单人查询",helpSendService.findTeakUser(helpSendDTO));
    }



}
