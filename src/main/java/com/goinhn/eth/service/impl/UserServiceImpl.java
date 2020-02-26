package com.goinhn.eth.service.impl;


import com.goinhn.eth.dao.ContractDao;
import com.goinhn.eth.dao.UserDao;
import com.goinhn.eth.domain.Contract;
import com.goinhn.eth.domain.User;
import com.goinhn.eth.service.UserService;
import com.goinhn.eth.util.MailUtil;
import com.goinhn.eth.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.HTMLDocument;
import java.util.Iterator;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;


    @Autowired
    private ContractDao contractDao;

    @Override
    public String register(User user) {
        if (userDao.findByUsername(user.getUsername()) != null) {
            return "该用户名已经注册";
        }
        if (userDao.findByMail(user.getMail()) != null) {
            return "该邮箱已经注册";
        }

        user.setActiveNumber(UuidUtil.getUuid());
        user.setIsActive("n");
        userDao.saveUser(user);

        String content = "<a href='http://localhost/eth/user/active.html?code=" + user.getActiveNumber() + "'>点击激活【智能合同】</a>";
        MailUtil.sendMail(user.getMail(), content, "智能合同激活邮件");

        return "";
    }

    @Override
    public boolean active(String activeNumber) {
        User user = userDao.findByActiveNumber(activeNumber);
        if (user != null) {
            user.setIsActive("y");
            userDao.updateActive(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User login(User user) {
        User userBack = userDao.findByUsernameAndPassword(user);
        if (userBack != null && userBack.getIsActive().equalsIgnoreCase("y")) {
            return userBack;
        } else {
            userBack = userDao.findByMailAndPassword(user);
            if (userBack != null && userBack.getIsActive().equalsIgnoreCase("y")) {
                return userBack;
            } else {
                return null;
            }
        }
    }

    @Override
    public boolean userSavePublicKey(User user) {
        User userBack = userDao.findByUserIdAndAccountAddress(user);
        if (userBack != null) {
            if (userDao.updatePublicKey(user) != 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public Contract contractDetails(int contractId) {
        return contractDao.findByContractId(contractId);
    }

    @Override
    public String findPublicKey(int contractId) {
        Contract contract = contractDao.findByContractId(contractId);
        if (contract != null) {
            User user = userDao.findByUserId(contract.getUserId());
            if (user != null) {
                return user.getPublicKey();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    @Override
    public boolean userSigned(Contract contract) {
        Contract contractBack = contractDao.findByContractId(contract.getContractId());
        if (contractBack != null) {
            List<Contract> contracts = contractDao.findByContractHash(contract.getContractHash());
            for (Contract contractTemp : contracts) {
                if (contractTemp.getContractType().equalsIgnoreCase("n")) {
                    contractTemp.setContractType("y");
                    contractTemp.setContractAddress(contract.getContractAddress());
                    int count = contractDao.updateContractTypeAndContractAddress(contractTemp);
                    if (count == 1) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return false;
        } else {
            return false;
        }
    }
}
