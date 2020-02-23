package com.goinhn.eth.service;

import com.goinhn.eth.domain.Contract;
import com.goinhn.eth.domain.User;

import java.util.List;
import java.util.Map;

public interface ContractService {

    User userInfo(int userId);

    void updateContract(User user);

    Map ContractCount(int userId);

    List<Contract> findForceContract(int userId);

    List<Contract> findNotSignedContract(int userId);
}
