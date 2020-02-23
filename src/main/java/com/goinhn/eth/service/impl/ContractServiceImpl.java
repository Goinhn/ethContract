package com.goinhn.eth.service.impl;


import com.goinhn.eth.dao.ContractDao;
import com.goinhn.eth.dao.UserDao;
import com.goinhn.eth.domain.Contract;
import com.goinhn.eth.domain.User;
import com.goinhn.eth.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("contactService")
public class ContractServiceImpl implements ContractService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ContractDao contractDao;

    @Override
    public User userInfo(int userId) {
        User user = userDao.findByUserId(userId);
        if (user != null) {
            return user;
        } else {
            return null;
        }
    }

    @Override
    public void updateContract(User user) {
        int changeCount = userDao.updateAccountAddress(user);
        System.out.println("updateContract-changeCount: " + changeCount);
    }

    @Override
    public Map ContractCount(int userId) {
        Map<String, Integer> map = new HashMap<>();
        Contract contract = new Contract();

        contract.setUserId(userId);
        contract.setContractType("y");
        int forceContract = contractDao.findCountByUserIdAndContractType(contract);
        contract.setContractType("n");
        int noSignedContract = contractDao.findCountByUserIdAndContractType(contract);

        map.put("forceCount", forceContract);
        map.put("notSignedContract", noSignedContract);

        return map;
    }

    @Override
    public List<Contract> findForceContract(int userId) {
        Contract contract = new Contract();
        contract.setUserId(userId);
        contract.setContractType("y");
        return contractDao.findByUserIdAndContractType(contract);
    }

    @Override
    public List<Contract> findNotSignedContract(int userId) {
        Contract contract = new Contract();
        contract.setUserId(userId);
        contract.setContractType("n");
        return contractDao.findByUserIdAndContractType(contract);
    }
}
