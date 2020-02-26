package com.goinhn.eth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goinhn.eth.domain.Contract;
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


    /**
     * 用户信息注册
     * @param params
     * @return
     */
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


    /**
     * 用户账号激活
     * @param params
     * @return
     */
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


    /**
     * 用户账号登录
     * @param params
     * @param model
     * @return
     */
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


    /**
     * 返回合同的详细信息
     * @param params
     * @return
     */
    @RequestMapping(value = "/contractDetails", method = RequestMethod.POST)
    public String contractDetails(@RequestBody Map<String, Object> params) {
        String function = params.get("function").toString();
        String contractId = params.get("contractId").toString();
        System.out.println(function);
        System.out.println(contractId);

        ResultInfo resultInfo = new ResultInfo();
        try {
            Contract contract = userService.contractDetails(Integer.parseInt(contractId));
            if (contract != null) {
                resultInfo.setFlag(true);
            } else {
                resultInfo.setFlag(false);
                resultInfo.setErrorMsg("合约不存在");
            }
            return writeValueAsString(resultInfo);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("合约不存在");
            return writeValueAsString(resultInfo);
        }
    }


    /**
     * 检查用户是否添加了签名
     * @param params
     * @return
     */
    @RequestMapping(value = "/checkSign", method = RequestMethod.POST)
    public String checkSign(@RequestBody Map<String, Object> params) {
        String function = params.get("function").toString();
        String contractId = params.get("contractId").toString();
        System.out.println(function);
        System.out.println(contractId);

        ResultInfo resultInfo = new ResultInfo();
        try {
            String publicKey = userService.findPublicKey(Integer.parseInt(contractId));
            if (publicKey != null) {
                resultInfo.setFlag(true);
            } else {
                resultInfo.setFlag(false);
                resultInfo.setErrorMsg("用户不存在");
            }
            return writeValueAsString(resultInfo);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("合约不存在");
            return writeValueAsString(resultInfo);
        }
    }


    /**
     * 用户添加签名
     * @param params
     * @return
     */
    @RequestMapping(value = "/userSigned", method = RequestMethod.POST)
    public String userSigned(@RequestBody Map<String, Object> params) {
        String function = params.get("function").toString();
        String contractId = params.get("contractId").toString();
        String contractAddress = params.get("contractAddress").toString();
        System.out.println(function);
        System.out.println(contractId);

        ResultInfo resultInfo = new ResultInfo();
        try {
            Contract contract = new Contract();
            contract.setContractId(Integer.parseInt(contractId));
            contract.setContractAddress(contractAddress);

            if (userService.userSigned(contract)) {
                resultInfo.setFlag(true);
            } else {
                resultInfo.setFlag(false);
                resultInfo.setErrorMsg("用户不存在");
            }
            return writeValueAsString(resultInfo);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("合约不存在");
            return writeValueAsString(resultInfo);
        }
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
