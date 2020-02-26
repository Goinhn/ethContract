package com.goinhn.eth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goinhn.eth.domain.Contract;
import com.goinhn.eth.domain.ResultInfo;
import com.goinhn.eth.domain.User;
import com.goinhn.eth.service.ContractService;
import com.goinhn.eth.util.SecUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping(value = "/eth/views/newContract")
@SessionAttributes("userId")
public class newContract {

    @Autowired
    private ContractService contractService;


    /**
     * 合约文件上传
     * @param params
     * @param model
     * @param file
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(@RequestBody Map<String, Object> params, ModelMap model, @RequestParam("file") MultipartFile file,
                         HttpServletRequest request) throws Exception {
        String function = params.get("function").toString();
        System.out.println("function: " + function);
        ResultInfo resultInfo = new ResultInfo();
        try {
            int userId = Integer.parseInt(model.get("userId").toString());

            boolean isEmpty = file.isEmpty();
            System.out.println("\tisEmpty=" + isEmpty);
            if (isEmpty) {
                resultInfo.setFlag(false);
                throw new RuntimeException("上传失败！上传的文件为空！");
            }

            long fileSize = file.getSize();
            System.out.println("\tsize=" + fileSize);
            if (fileSize > 1 * 1024 * 1024) {
                resultInfo.setFlag(false);
                throw new RuntimeException("上传失败！上传的文件大小超出了限制！");
            }

            String contentType = file.getContentType();
            System.out.println("\tcontentType=" + contentType);
            List<String> types = new ArrayList<String>();
            types.add("image/jpeg");
            types.add("image/png");
            types.add("image/gif");
            types.add("pdf");
            if (!types.contains(contentType)) {
                resultInfo.setFlag(false);
                throw new RuntimeException("上传失败！不允许上传此类型的文件！");
            }


            String parentDir = request.getContextPath();
            parentDir += "\\upload";
            System.out.println("\tpath=" + parentDir);
            File parent = new File(parentDir);
            if (!parent.exists()) {
                parent.mkdirs();
            }

            String originalFilename = file.getOriginalFilename();
            System.out.println("\toriginalFilename=" + originalFilename);

            String filename = UUID.randomUUID().toString();
            String suffix = "";
            int beginIndex = originalFilename.lastIndexOf(".");
            if (beginIndex != -1) {
                suffix = originalFilename.substring(beginIndex);
                String userInfo = SecUtil.encodeByMd5(String.valueOf(userId));
                suffix += userInfo;
            }

            File dest = new File(parent, filename + suffix);
            file.transferTo(dest);

            resultInfo.setFlag(true);

            return writeValueAsString(resultInfo);
        } catch (NullPointerException | NumberFormatException e) {
            e.printStackTrace();
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("该账号不存在");
            return writeValueAsString(resultInfo);
        }
    }


    /**
     * 返回用户的公钥验证用户的私钥签名是否正确
     * @param params
     * @param model
     * @return
     */
    @RequestMapping(value = "/publicKey", method = RequestMethod.POST)
    public String publicKey(@RequestBody Map<String, Object> params, ModelMap model) {
        String function = params.get("function").toString();
        System.out.println("function: " + function);

        ResultInfo resultInfo = new ResultInfo();
        try {
            int userId = Integer.parseInt(model.get("userId").toString().trim());
            User user = new User();
            user.setUserId(userId);
            user = contractService.findPublicKey(user);

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

    /**
     * 检查该合同是否已经签署
     * @param params
     * @param model
     * @return
     */

    @RequestMapping(value = "/checkSigned", method = RequestMethod.POST)
    public String checkSigned(@RequestBody Map<String, Object> params, ModelMap model) {
        String function = params.get("function").toString();
        System.out.println("function: " + function);
        String fileHash = params.get("fileHash").toString();
        System.out.println("fileHash; " + fileHash);

        ResultInfo resultInfo = new ResultInfo();
        try {
            int userId = Integer.parseInt(model.get("userId").toString().trim());
            Contract contract = new Contract();
            contract.setUserId(userId);
            contract.setContractHash(fileHash);

            if (contractService.checkSignedByContractHash(contract)) {
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


    /**
     * 提交签署合同的信息，后台保存
     * @param params
     * @param model
     * @return
     */
    @RequestMapping(value = "/submitInfo", method = RequestMethod.POST)
    public String submitInfo(@RequestBody Map<String, Object> params, ModelMap model) {
        String function = params.get("function").toString();
        System.out.println("function: " + function);

        String partyA = params.get("partyA").toString();
        String partyB = params.get("partyB").toString();
        String fileName = params.get("fileName").toString();
        String fileHash = params.get("fileHash").toString();
        System.out.println("fileHash; " + fileHash);

        ResultInfo resultInfo = new ResultInfo();
        try {
            int userId = Integer.parseInt(model.get("userId").toString().trim());
            Contract contract = new Contract();
            contract.setUserId(userId);
            contract.setContractName(fileName);
            contract.setContractHash(fileHash);
            contract.setPartyA(partyA);
            contract.setPartyB(partyB);

            if (contractService.submitContractInfo(contract)) {
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
