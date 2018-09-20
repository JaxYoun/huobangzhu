package com.troy.keeper.hbz.app.rest.web;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.base.entity.Account;
import com.troy.keeper.hbz.app.dto.PasswordDTO;
import com.troy.keeper.hbz.app.web.WebThreadHolder;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.HbzOrgDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.service.HbzUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 接收、处理web端企业用户相关请求
 */
@RestController
@RequestMapping("/api/web/user/")
public class WebUserResource {

    @Autowired
    HbzUserService hbzUserService;

    /**
     * 获取菜单：
     * 1.对于未登录用户：仅仅返回不需要认证的公共菜单
     * 2.对于已登录用户：同时返回公共菜单、根据角色分配的授权菜单
     *
     * @return
     */
    @PostMapping("/getUserRoleMenu")
    public ResponseDTO getMenuList() {
        Object object = WebThreadHolder.currentUserPrincipal();
        if (object instanceof String && "anonymousUser".equals(object)) {
            return new ResponseDTO(Const.STATUS_OK, "获取菜单成功！", hbzUserService.getMenuListByUserId(null));
        } else {
            Account account = (Account) object;
            if (account != null && account.getAccountId() != null) {
                return new ResponseDTO(Const.STATUS_OK, "获取菜单成功！", hbzUserService.getMenuListByUserId(account.getAccountId()));
            } else {
                return new ResponseDTO(Const.STATUS_ERROR, "获取菜单失败！", null);
            }
        }
    }

    /**
     * 获取当前用户公司信息
     *
     * @return
     */
    @PostMapping("/getCurrentUserEnterprise")
    public ResponseDTO getCurrentUserEnterprise() {
        HbzOrgDTO hbzOrgDTO = this.hbzUserService.currentUser().getEnt();
        if (hbzOrgDTO != null) {
            return new ResponseDTO(Const.STATUS_OK, "成功", hbzOrgDTO);
        }
        return new ResponseDTO(Const.STATUS_ERROR, "失败，用户没有公司信息", null);
    }

    /**
     * @param passwordDTO
     * @return
     */
    @PostMapping("/modifyPassword")
    public ResponseDTO modifyPassword(HttpServletRequest request, @RequestBody PasswordDTO passwordDTO) {
        String backAuthCode = (String) request.getSession().getAttribute(Const.AUTH_CODE_NAME);

        String frontAuthCode = passwordDTO.getAuthCode();
        String oldPassword = passwordDTO.getOldPassword();
        String newPassword = passwordDTO.getNewPassword();

        ResponseDTO responseDTO = new ResponseDTO();

        // TODO: 2017/11/10 暂时注释了验证码检查逻辑，后期需放开
//        if(StringUtils.isNotBlank(frontAuthCode) && StringUtils.isNotBlank(backAuthCode) && backAuthCode.equals(frontAuthCode)) {
        Account account = WebThreadHolder.currentUserPrincipal();
        HbzUserDTO hbzUserDTO = hbzUserService.findUser(account.getAccountId());

        // TODO: 2017/11/10 未添加密码加密处理，后期需添加
        if (StringUtils.isNotBlank(oldPassword) && oldPassword.equals(hbzUserDTO.getPassword())) {
            hbzUserDTO.setPassword(newPassword);
            hbzUserService.save(hbzUserDTO);

            responseDTO.setCode(Const.STATUS_OK);
            responseDTO.setMsg("修改成功！");
            responseDTO.setData(null);
            return responseDTO;
        } else {
            responseDTO.setCode("400");
            responseDTO.setMsg("旧密码错误！");
            responseDTO.setData(null);
            return responseDTO;
        }
    }

}