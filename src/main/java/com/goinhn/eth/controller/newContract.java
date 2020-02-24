package com.goinhn.eth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goinhn.eth.domain.ResultInfo;
import com.goinhn.eth.domain.User;
import com.goinhn.eth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping(value = "/eth/views/signature")
@SessionAttributes("userId")
public class signatureController {

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/saveSign", method = RequestMethod.POST)
    public String userInfo(@RequestBody Map<String, Object> params, ModelMap model) {
        String function = params.get("function").toString();
        String accountAddress = params.get("accountAddress").toString();
        String publicKey = params.get("publicKey").toString();
        System.out.println("function: " + function);
        System.out.println("publicKey: " + publicKey);

        ResultInfo resultInfo = new ResultInfo();
        try {
            User user = new User();
            int userId = Integer.parseInt(model.get("userId").toString().trim());
            user.setUserId(userId);
            user.setAccountAddress(accountAddress);
            user.setPublicKey(publicKey);

            if (userService.userSavePublicKey(user)) {
                resultInfo.setFlag(true);
            } else {
                resultInfo.setFlag(false);
                resultInfo.setErrorMsg("该账号未激活");
            }
            return writeValueAsString(resultInfo);
        } catch (NullPointerException | NumberFormatException e) {
            e.printStackTrace();
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("该账号不存在");
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
