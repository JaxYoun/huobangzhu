package com.troy.keeper.hbz.app.apps.view;

import com.troy.keeper.core.base.entity.Account;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by leecheng on 2017/10/11.
 */
@Controller
public class View {

    @RequestMapping("/")
    public void root(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendRedirect("/index.html");
    }

    @RequestMapping("/login")
    public String loginUrl() {
        return "/login";
    }

    @RequestMapping("/home")
    public String authed(Model model) {
        model.addAttribute("username", ((Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        return "/home";
    }

    @RequestMapping("/api/index")
    public String api_index(Model model) {
        model.addAttribute("username", ((Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        return "/api_index";
    }

    @RequestMapping("/api/test/demo")
    public String api_test_demo(Model model) {
        model.addAttribute("username", ((Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        return "/api_test_demo";
    }
}
