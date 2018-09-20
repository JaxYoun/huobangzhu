package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.constant.MapSpec;
import com.troy.keeper.hbz.app.dto.AuthCode;
import com.troy.keeper.hbz.app.dto.SmsVO;
import com.troy.keeper.hbz.app.helper.AuthCodeHelper;
import com.troy.keeper.hbz.app.web.WebThreadHolder;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.dto.BaseDTO;
import com.troy.keeper.hbz.dto.HbzPayAccountDTO;
import com.troy.keeper.hbz.dto.HbzRoleDTO;
import com.troy.keeper.hbz.dto.HbzUserDTO;
import com.troy.keeper.hbz.helper.Base64Encrypter;
import com.troy.keeper.hbz.helper.PasswordHelper;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.helper.ValidationHelper;
import com.troy.keeper.hbz.service.HbzPayAccountService;
import com.troy.keeper.hbz.service.HbzRoleService;
import com.troy.keeper.hbz.service.HbzUserService;
import com.troy.keeper.hbz.service.SmsService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by leecheng on 2017/10/25.
 */
@CommonsLog
@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class HbzUserResource {

    @Value("${debug}")
    private Boolean debug;

    @Value("${staticImagePrefix}")
    private String staticImagePrefix;

    @Autowired
    private SmsService smsService;

    @Autowired
    private HbzUserService hbzUserService;

    @Autowired
    private HbzPayAccountService hbzPayAccountService;

    @Autowired
    private HbzRoleService hbzRoleService;

    /**
     * 获取认证码图片
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/authCode/peek")
    public ResponseDTO peek(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String width = request.getParameter("width");
        String height = request.getParameter("height");
        String size = request.getParameter("size");

        int sizePre = 4;
        if (StringHelper.notNullAndEmpty(size)) {
            sizePre = Integer.parseInt(size);
        } else {
            sizePre = 4;
        }
        int bound = 1;
        for (int i = 0; i < sizePre; i++) {
            bound *= 10;
        }

        int picWidth = 100;
        int picHeight = 32;
        if (StringHelper.notNullAndEmpty(width)) {
            picWidth = Integer.parseInt(width);
        }
        if (StringHelper.notNullAndEmpty(height)) {
            picHeight = Integer.parseInt(height);
        }

        Random random = new Random(new Date().getTime());
        int rand = random.nextInt(bound - 1);
        String auth_code = String.format("%0" + sizePre + "d", rand);
        AuthCode authCode = new AuthCode(auth_code, System.currentTimeMillis());
        request.getSession().setAttribute(Const.AUTH_CODE_NAME, authCode);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        AuthCodeHelper.outputImage(picWidth, picHeight, baos, auth_code);
        String base64 = Base64Encrypter.base64Encode(baos.toByteArray());
        return new ResponseDTO(Const.STATUS_OK, "验证码", base64);
    }

    /**
     * 认证码短信
     *
     * @param s
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/authCode/send", method = RequestMethod.POST)
    public ResponseDTO sms(@RequestBody SmsVO s, HttpServletRequest request, HttpServletResponse response) {
        String[] err = ValidationHelper.valid(s, "sms_send");
        if (err.length > 0) {
            return new ResponseDTO(Const.STATUS_ERROR, "错误", err);
        }
        String size = request.getParameter("size");
        int sizePre = 4;
        if (StringHelper.notNullAndEmpty(size)) {
            sizePre = Integer.parseInt(size);
        } else {
            sizePre = 4;
        }
        int bound = 1;
        for (int i = 0; i < sizePre; i++) {
            bound *= 10;
        }
        Random random = new Random(new Date().getTime());
        int rand = random.nextInt(bound - 1);
        String auth_code = String.format("%0" + sizePre + "d", rand);
        AuthCode authCode = new AuthCode(auth_code, System.currentTimeMillis());
        request.getSession().setAttribute(Const.AUTH_CODE_NAME, authCode);
        //进行业务验证
        if (s.getSource() != null) {
            switch (s.getSource()) {
                case REGISTER:
                    break;
                case FORGET_PASSWORD:
                    HbzUserDTO query = new HbzUserDTO();
                    query.setStatus("1");
                    query.setTelephone(s.getPhone());
                    Long count = hbzUserService.count(query);
                    if (count.longValue() < 1L) {
                        return new ResponseDTO(Const.STATUS_ERROR, "未注册，或用户被禁止！");
                    }
                    break;
            }
        }
        if (smsService.send(s.getPhone(), "你的验证码：[" + auth_code + "]")) {
            log.debug("验证码：" + auth_code + "");
            return new ResponseDTO(Const.STATUS_OK, "验证码发送成功", true);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "发送失败", false);
        }
    }

    /**
     * 验证码检查
     *
     * @param dto
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/app/authCode/check", method = RequestMethod.POST)
    public ResponseDTO sms(@RequestBody BaseDTO dto, HttpServletRequest request) {
        AuthCode authCode = (AuthCode) request.getSession().getAttribute(Const.AUTH_CODE_NAME);
        if (authCode != null && authCode instanceof AuthCode && authCode.getAuthCode().equals(dto.getAuthCode())) {
            if (authCode.getCreateTime() - System.currentTimeMillis() > -10L * 60L * 1000L) {
                return new ResponseDTO(Const.STATUS_OK, "验证成功", null);
            } else {
                return new ResponseDTO(Const.STATUS_ERROR, "验证超时", null);
            }
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "验证失败", null);
        }
    }

    /**
     * 验证码检查
     *
     * @param dto
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/app/telephone/check", method = RequestMethod.POST)
    public ResponseDTO checkPhonenumber(@RequestBody HbzUserDTO dto, HttpServletRequest request) {
        HbzUserDTO hbzUser = hbzUserService.findByLogin(dto.getTelephone());
        if (hbzUser != null) {
            return new ResponseDTO(Const.STATUS_ERROR, "手机已注册", null);
        } else {
            return new ResponseDTO(Const.STATUS_OK, "可以注册", null);
        }
    }


    /**
     * 重置密码
     *
     * @param hbzUserDTO
     * @param request
     * @return
     */
    @RequestMapping(value = "/app/reset", method = RequestMethod.POST)
    public ResponseDTO reset(@RequestBody HbzUserDTO hbzUserDTO, HttpServletRequest request) {
        if (debug) {
            if (request.getSession().getAttribute(Const.AUTH_CODE_NAME) == null) {
                request.getSession().setAttribute(Const.AUTH_CODE_NAME, new AuthCode("1001", System.currentTimeMillis()));
            }
        }
        AuthCode authCode = (AuthCode) request.getSession().getAttribute(Const.AUTH_CODE_NAME);
        request.getSession().removeAttribute(Const.AUTH_CODE_NAME);
        if (authCode == null || !authCode.getAuthCode().equals(hbzUserDTO.getAuthCode())) {
            return new ResponseDTO(Const.STATUS_ERROR, "认证码有误");
        }
        if (authCode.getCreateTime() - System.currentTimeMillis() > -10L * 60L * 1000L) {
            String telephone = hbzUserDTO.getTelephone();
            String password = hbzUserDTO.getPassword();
            HbzUserDTO user = hbzUserService.findByLogin(telephone);
            if (user != null) {
                if (password != null && password.length() > 0) {
                    for (int i = 0; i < password.length(); i++) {
                        char c = password.charAt(i);
                        if (!PasswordHelper.isShort(c)) {
                            return new ResponseDTO(Const.STATUS_ERROR, "密码有非法字符");
                        }
                    }
                    user.setPassword(password);
                    hbzUserService.save(user);
                    return new ResponseDTO(Const.STATUS_OK, "密码重置成功", null);
                }
                return new ResponseDTO(Const.STATUS_ERROR, "出现错误", null);
            } else {
                return new ResponseDTO(Const.STATUS_ERROR, "出现错误", null);
            }
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "认证码错误", null);
        }
    }

    /**
     * 是否已经登录的接口
     *
     * @param hbzUserDTO
     * @param request
     * @return
     */
    @RequestMapping(value = "/isAuthencated", method = RequestMethod.POST)
    public ResponseDTO isAuthencated(@RequestBody HbzUserDTO hbzUserDTO, HttpServletRequest request) {
        if (WebThreadHolder.isAuthenticated()) {
            return new ResponseDTO(Const.STATUS_OK, "认证", true);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "未认证", false);
        }
    }

    //注册用户接口
    @RequestMapping(value = "/app/registry", method = RequestMethod.POST)
    public ResponseDTO userRegistry(@RequestBody HbzUserDTO hbzUserDTO, HttpSession session) {
        if (hbzUserDTO.getId() != null) {
            return new ResponseDTO(Const.STATUS_ERROR, "不允许设置ID");
        }
        if (debug) {
            if (session.getAttribute(Const.AUTH_CODE_NAME) == null) {
                session.setAttribute(Const.AUTH_CODE_NAME, new AuthCode("1001", System.currentTimeMillis()));
            }
        }
        AuthCode authCode = (AuthCode) session.getAttribute(Const.AUTH_CODE_NAME);
        if (authCode == null || !authCode.getAuthCode().equals(hbzUserDTO.getAuthCode())) {
            return new ResponseDTO(Const.STATUS_ERROR, "认证码有误");
        }
        session.removeAttribute(Const.AUTH_CODE_NAME);
        if (authCode.getCreateTime() - System.currentTimeMillis() > -10L * 60L * 1000L) {
            String[] err = ValidationHelper.valid(hbzUserDTO, "app_user_registry");
            if (err != null && err.length > 0) {
                return new ResponseDTO(Const.STATUS_ERROR, "验证失败", err);
            } else {
                HbzUserDTO query;
                List list;

                query = new HbzUserDTO();
                query.setLogin(hbzUserDTO.getTelephone());
                list = hbzUserService.query(query);
                if (list != null && list.size() > 0) {
                    return new ResponseDTO(Const.STATUS_ERROR, "账号已经存在", err);
                }

                query = new HbzUserDTO();
                query.setTelephone(hbzUserDTO.getTelephone());
                list = hbzUserService.query(query);
                if (list != null && list.size() > 0) {
                    return new ResponseDTO(Const.STATUS_ERROR, "手机号已经存在", err);
                }

                HbzUserDTO hbzUser = new HbzUserDTO();
                BeanUtils.copyProperties(hbzUserDTO, hbzUser);
                hbzUser.setScore(300);
                hbzUser.setStarLevel(3);
                hbzUser.setUserStarLevel(3);
                hbzUser.setLogin(hbzUserDTO.getTelephone());
                hbzUser.setActivated(true);
                hbzUser.setStatus(Const.STATUS_ENABLED);
                hbzUser.setUserScore(300);

                hbzUser = hbzUserService.save(hbzUser);
                if (hbzUser != null) {
                    return new ResponseDTO(Const.STATUS_OK, "注册成功", null);
                }
            }
        }
        return new ResponseDTO(Const.STATUS_ERROR, "认证码超时", null);
    }

    //用户常用支付账户设置
    @RequestMapping(value = {"/app/pay/account/set"}, method = RequestMethod.POST)
    public ResponseDTO userPayAccountSet(@RequestBody HbzPayAccountDTO hbzPayAccountDTO) {
        if (hbzPayAccountDTO.getId() != null)
            return new ResponseDTO(Const.STATUS_ERROR, "不能指定ID");
        String[] err = ValidationHelper.valid(hbzPayAccountDTO, "account_create");
        if (err != null && err.length > 0)
            return new ResponseDTO(Const.STATUS_ERROR, "验证失败", err);
        HbzUserDTO user = hbzUserService.findByLogin(WebThreadHolder.currentUser());

        HbzPayAccountDTO query = new HbzPayAccountDTO();
        query.setStatus(Const.STATUS_ENABLED);
        query.setUserId(user.getId());
        query.setIsDefault(true);
        List<HbzPayAccountDTO> list = hbzPayAccountService.query(query);

        if (hbzPayAccountDTO.getIsDefault() != null && hbzPayAccountDTO.getIsDefault()) {
            if (list != null && list.size() > 0)
                for (HbzPayAccountDTO hbzPayAccount : list)
                    hbzPayAccount.setIsDefault(false);
            hbzPayAccountService.save(list);
        } else {
            if (list != null && list.size() > 0)
                hbzPayAccountDTO.setIsDefault(false);
            else
                hbzPayAccountDTO.setIsDefault(true);
        }
        hbzPayAccountDTO.setUserId(user.getId());
        hbzPayAccountDTO.setStatus(Const.STATUS_ENABLED);
        HbzPayAccountDTO hbzPayAccount = hbzPayAccountService.save(hbzPayAccountDTO);
        if (hbzPayAccount != null) {
            hbzPayAccount.getUser().setPassword("");
            return new ResponseDTO(Const.STATUS_OK, "操作成功", hbzPayAccount);
        }
        return new ResponseDTO(Const.STATUS_ERROR, "错误");
    }

    //常用地址创建
    @RequestMapping(value = {"/get"}, method = RequestMethod.POST)
    public ResponseDTO user() {
        return new ResponseDTO(Const.STATUS_OK, "信息", Optional.of(hbzUserService.currentUser()).map(u -> {
            Map<String, Object> user = MapSpec.mapUserWithRegistry(u);
            //if(null != user.get("imageUrl")){
            //    user.put("imageUrl",staticImagePrefix + user.get("imageUrl"));
            //}else{
            //    user.put("imageUrl",user.get("imageUrl"));
            //}
            return user;
        }).get());
    }

    //查询用户
    @RequestMapping(value = {"/query"}, method = RequestMethod.POST)
    public ResponseDTO userQuery(@RequestBody HbzUserDTO queryDTO) {
        return new ResponseDTO(Const.STATUS_OK, "查询成功", hbzUserService.query(queryDTO).stream().map(u -> {
            Map<String, Object> user = MapSpec.mapUserWithRegistry(u);
            //if(null != user.get("imageUrl")){
            //    user.put("imageUrl",staticImagePrefix + user.get("imageUrl"));
            //}else{
            //    user.put("imageUrl",user.get("imageUrl"));
            //}
            return user;
        }).collect(Collectors.toList()));
    }

    //查询用户
    @RequestMapping(value = {"/queryPage"}, method = RequestMethod.POST)
    public ResponseDTO pageQuery(@RequestBody HbzUserDTO queryDTO) {
        return new ResponseDTO(Const.STATUS_OK, "查询成功", hbzUserService.queryPage(queryDTO, queryDTO.getPageRequest()).map(u -> {
            Map<String, Object> user = MapSpec.mapUserWithRegistry(u);
            //if(null != user.get("imageUrl")){
            //    user.put("imageUrl",staticImagePrefix + user.get("imageUrl"));
            //}else{
            //    user.put("imageUrl",user.get("imageUrl"));
            //}
            return user;
        }));
    }

    //查询用户
    @RequestMapping(value = {"/roles"}, method = RequestMethod.POST)
    public ResponseDTO userRole() {
        HbzRoleDTO rolequery = new HbzRoleDTO();
        rolequery.setStatus(Const.STATUS_ENABLED);
        rolequery.setUserId(hbzUserService.currentUser().getId());
        List<HbzRoleDTO> role = hbzRoleService.query(rolequery);
        return new ResponseDTO(Const.STATUS_OK, "查询成功", role.stream().map(MapSpec::mapRole).collect(Collectors.toList()));
    }

    //修改用户信息
    @RequestMapping(value = "/updateHbzUser", method = RequestMethod.POST)
    public ResponseDTO updateHbzUser(@RequestBody HbzUserDTO hbzUserDTO) {
        if ("".equals(hbzUserDTO.getImageUrl()) || null == hbzUserDTO.getImageUrl()) {
            return new ResponseDTO(Const.STATUS_UN_AUTHENCATIIONED, "头像不能为空", null);
        }
        boolean flag = hbzUserService.updateHbzUser(hbzUserService.currentUser().getId(), hbzUserDTO);
        if (flag) {
            return new ResponseDTO(Const.STATUS_OK, "操作成功", null);
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "操作失败", null);
        }
    }


}
