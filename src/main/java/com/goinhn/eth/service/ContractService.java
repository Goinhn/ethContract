package com.goinhn.eth.service;

import com.goinhn.eth.domain.Contract;
import com.goinhn.eth.domain.User;

import java.util.List;
import java.util.Map;

public interface ContractService {

    /**
     * 获取用户的信息
     *
     * @param userId
     * @return
     */
    User userInfo(int userId);

    /**
     * 保存用户的账户地址
     *
     * @param user
     * @return
     */
    boolean userSaveAccountAddress(User user);

    /**
     * 更新合同的信息
     *
     * @param user
     */
    void updateContract(User user);

    /**
     * 合同的数量
     *
     * @param userId
     * @return
     */
    Map ContractCount(int userId);

    /**
     * 已经签署的合同
     *
     * @param userId
     * @return
     */
    List<Contract> findForceContract(int userId);

    /**
     * 未签署的合同
     *
     * @param userId
     * @return
     */
    List<Contract> findNotSignedContract(int userId);

    /**
     * 查找公钥
     *
     * @param user
     * @return
     */
    User findPublicKey(User user);

    /**
     * 检查合同是否签署
     *
     * @param contract
     * @return
     */
    boolean checkSignedByContractHash(Contract contract);

    /**
     * 提交合同的信息
     *
     * @param contract
     * @return
     */
    boolean submitContractInfo(Contract contract);
}
