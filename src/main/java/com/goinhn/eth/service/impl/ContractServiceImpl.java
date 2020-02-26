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
    public boolean userSaveAccountAddress(User user) {
        if (userDao.updateAccountAddress(user) != 0) {
            return true;
        } else {
            return false;
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

    @Override
    public User findPublicKey(User user) {
        return userDao.findByUserId(user.getUserId());
    }

    @Override
    public boolean checkSignedByContractHash(Contract contract) {
        Contract contractBack = contractDao.findByUserIdAndContractHash(contract);
        if (contractBack != null && contractBack.getContractType().equalsIgnoreCase("n")) {
            return true;
        } else if (contractBack == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean submitContractInfo(Contract contract) {
        Contract contractBack = contractDao.findByUserIdAndContractHash(contract);
        if (contractBack != null && contractBack.getContractHash().equals(contract.getContractHash()) &&
                contractBack.getPartyA().equals(contract.getPartyA()) &&
                contractBack.getPartyB().equals(contract.getPartyB())) {
            contractBack.setContractType("y");
            return contractDao.updateContract(contractBack) == 1;

        } else if (contractBack == null) {
            User userA = userDao.findByPublicKey(contract.getPartyA());
            User userB = userDao.findByPublicKey(contract.getPartyB());
            if (userA != null && userB != null) {
                Contract contractA = contract;
                Contract contractB = contract;
                if (userA.getUserId() == contract.getUserId()) {
                    contractB.setUserId(userB.getUserId());
                    contractA.setContractType("y");
                    contractB.setContractType("n");
                } else if (userB.getUserId() == contract.getUserId()) {
                    contractA.setUserId(userA.getUserId());
                    contractA.setContractType("n");
                    contractB.setContractType("y");
                }

                int countA = contractDao.saveContract(contractA);
                int countB = contractDao.saveContract(contractB);

                return countA == 1 && countB == 1;

            } else {
                return false;
            }
        } else {
            return false;
        }
    }


}
