package com.goinhn.eth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goinhn.eth.domain.ResultInfo;
import com.goinhn.eth.domain.User;
import com.goinhn.eth.service.UserService;
import com.goinhn.eth.util.SecUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.reactive.server.XpathAssertions;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import sun.java2d.pipe.SpanShapeRenderer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;


@RestController
@RequestMapping(value = "/eth/views/user")
@SessionAttributes("userId")
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestBody Map<String, Object> params) {
        String function = params.get("function").toString();
        String username = params.get("uesrname").toString();
        String mail = params.get("mail").toString();
        String password = params.get("password").toString();
        String passwordMd5 = "";
        try {
            passwordMd5 = SecUtil.encodeByMd5(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("function: " + function);
        System.out.println("username: " + username);
        System.out.println("passwordMd5: " + passwordMd5);

        User user = new User();
        user.setUsername(username);
        user.setMail(mail);
        user.setPassword(passwordMd5);
        String info = userService.register(user);
        ResultInfo resultInfo = new ResultInfo();

        if (info.equals("")) {
            resultInfo.setFlag(true);
        } else {
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg(info);
        }

        return writeValueAsString(resultInfo);
    }


    @RequestMapping(value = "/active", method = RequestMethod.POST)
    public String active(@RequestBody Map<String, Object> params) {
        String function = params.get("function").toString();
        String activeNumber = params.get("code").toString();
        System.out.println(function);

        ResultInfo resultInfo = new ResultInfo();
        if (userService.active(activeNumber)) {
            resultInfo.setFlag(true);
        } else {
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("验证码验证失败");
        }

        return writeValueAsString(resultInfo);
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestBody Map<String, Object> params, ModelMap model) {
        String function = params.get("function").toString();
        String remember = params.get("remember").toString();
        String nameMail = params.get("nameMail").toString();
        String password = params.get("password").toString();
        String passwordMd5 = "";
        try {
            passwordMd5 = SecUtil.encodeByMd5(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("function" + function);
        System.out.println("nameMail" + nameMail);

        User user = new User();
        user.setUsername(nameMail);
        user.setMail(nameMail);
        user.setPassword(passwordMd5);

        User userBack = userService.login(user);
        ResultInfo resultInfo = new ResultInfo();
        if (userBack != null) {
            resultInfo.setFlag(true);
            resultInfo.setData(userBack);
            //添加userId的全局属性
            model.addAttribute("userId", userBack.getUserId());
        } else {
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("登录失败");
        }

        return writeValueAsString(resultInfo);
    }

    private String writeValueAsString(ResultInfo resultInfo) {
        String json = "";
        try {
            ObjectMapper mapper = new ObjectMapper();
            json = mapper.writeValueAsString(resultInfo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}
