package com.goinhn.eth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goinhn.eth.domain.ResultInfo;
import com.goinhn.eth.domain.User;
import com.goinhn.eth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.java2d.pipe.SpanShapeRenderer;

import java.util.Map;


@RestController
@RequestMapping(value = "/eth/views/user")
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@ModelAttribute() User user) {
        String info = userService.register(user);
        String json = "";
        if (info.equals("")) {
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setFlag(true);
            try {
                ObjectMapper mapper = new ObjectMapper();
                json = mapper.writeValueAsString(resultInfo);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return json;
        } else {
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg(info);
            try {
                ObjectMapper mapper = new ObjectMapper();
                json = mapper.writeValueAsString(resultInfo);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return json;
        }
    }


    @RequestMapping(value = "/active", method = RequestMethod.POST)
    public String active(@RequestBody Map<String,Object> params) {
        String activeNumber = params.get("activeNumber").toString();
        ResultInfo resultInfo = new ResultInfo();
        if(userService.active(activeNumber)){
            resultInfo.setFlag(true);
        }else{
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("验证码验证失败");
        }
        String json = "";
        try{
            ObjectMapper mapper = new ObjectMapper();
            json = mapper.writeValueAsString(resultInfo);
        } catch( JsonProcessingException e){
            e.printStackTrace();
        }

        return json;
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestBody Map<String,Object> params){
        User user = new User();
        user.setUsername(params.get("nameMail").toString());
        user.setMail(params.get("nameMail").toString());
        user.setPassword(params.get("password").toString());

        User userBack = userService.login(user);
        String json = "";
        if (userBack != null) {
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setFlag(true);
            resultInfo.setData(userBack);
            try {
                ObjectMapper mapper = new ObjectMapper();
                json = mapper.writeValueAsString(resultInfo);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return json;
        } else {
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("登录失败");
            try {
                ObjectMapper mapper = new ObjectMapper();
                json = mapper.writeValueAsString(resultInfo);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return json;
        }
    }
}
