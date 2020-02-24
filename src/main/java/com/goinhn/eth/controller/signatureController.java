package com.goinhn.eth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goinhn.eth.domain.Contract;
import com.goinhn.eth.domain.ResultInfo;
import com.goinhn.eth.domain.User;
import com.goinhn.eth.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/eth/views/index")
@SessionAttributes("userId")
public class IndexController {

    @Autowired
    private ContractService contractService;


    @RequestMapping(value = "/userInfo", method = RequestMethod.POST)
    public String userInfo(@RequestBody Map<String, Object> params, ModelMap model) {
        String function = params.get("function").toString();
        String accountAddress = params.get("accountAddress").toString();
        System.out.println("function: " + function);
        System.out.println("accountAddress: " + accountAddress);

        ResultInfo resultInfo = new ResultInfo();
        try {
            int userId = Integer.parseInt(model.get("userId").toString().trim());
            User user = contractService.userInfo(userId);
            user.setAccountAddress(accountAddress);
            contractService.updateContract(user);
            if (user != null) {
                resultInfo.setFlag(true);
                resultInfo.setData(user);
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


    @RequestMapping(value = "/userSaveAccountAddress", method = RequestMethod.POST)
    public String userSaveAccountAddress(@RequestBody Map<String, Object> params, ModelMap model) {
        String function = params.get("function").toString();
        String accountAddress = params.get("accountAddress").toString();
        System.out.println("function: " + function);

        ResultInfo resultInfo = new ResultInfo();
        try {
            User user = new User();
            int userId = Integer.parseInt(model.get("userId").toString().trim());
            user.setUserId(userId);
            user.setAccountAddress(accountAddress);

            if (contractService.userSaveAccountAddress(user)) {
                resultInfo.setFlag(true);

            } else {
                resultInfo.setFlag(false);
                resultInfo.setErrorMsg("更新签名失败！");
            }
            return writeValueAsString(resultInfo);
        } catch (NullPointerException | NumberFormatException e) {
            e.printStackTrace();
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("该账号不存在");
            return writeValueAsString(resultInfo);
        }
    }


    @RequestMapping(value = "/contractCount", method = RequestMethod.POST)
    public String contractCount(@RequestBody Map<String, Object> params, ModelMap model) {
        String function = params.get("function").toString();
        System.out.println("function: " + function);

        ResultInfo resultInfo = new ResultInfo();
        try {
            int userId = Integer.parseInt(model.get("userId").toString().trim());

            Map map = contractService.ContractCount(userId);
            if (map != null && map.size() != 0) {
                resultInfo.setFlag(true);
                resultInfo.setData(map);
            } else {
                resultInfo.setFlag(false);
                resultInfo.setErrorMsg("该账号未登录");
            }
            return writeValueAsString(resultInfo);
        } catch (NullPointerException | NumberFormatException e) {
            e.printStackTrace();
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("该账号不存在");
            return writeValueAsString(resultInfo);
        }
    }


    @RequestMapping(value = "/forceContract", method = RequestMethod.POST)
    public String forceContract(@RequestBody Map<String, Object> params, ModelMap model) {
        String function = params.get("function").toString();
        System.out.println("function: " + function);

        ResultInfo resultInfo = new ResultInfo();
        try {
            int userId = Integer.parseInt(model.get("userId").toString().trim());

            List<Contract> contracts = contractService.findForceContract(userId);
            if (contracts != null) {
                resultInfo.setFlag(true);
                resultInfo.setData(contracts);
            } else {
                resultInfo.setFlag(false);
                resultInfo.setErrorMsg("该账号未登录");
            }
            return writeValueAsString(resultInfo);
        } catch (NullPointerException | NumberFormatException e) {
            e.printStackTrace();
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("该账号不存在");
            return writeValueAsString(resultInfo);
        }
    }


    @RequestMapping(value = "/notSignedContract", method = RequestMethod.POST)
    public String notSignedContract(@RequestBody Map<String, Object> params, ModelMap model) {
        String function = params.get("function").toString();
        System.out.println("function: " + function);

        ResultInfo resultInfo = new ResultInfo();
        try {
            int userId = Integer.parseInt(model.get("userId").toString().trim());

            List<Contract> contracts = contractService.findNotSignedContract(userId);
            if (contracts != null) {
                resultInfo.setFlag(true);
                resultInfo.setData(contracts);
            } else {
                resultInfo.setFlag(false);
                resultInfo.setErrorMsg("该账号未登录");
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
