package com.goinhn.eth.service;

import com.goinhn.eth.domain.Contract;
import com.goinhn.eth.domain.User;

public interface UserService {

    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    String register(User user);

    /**
     * 用户激活
     *
     * @param activeNumber
     * @return
     */
    boolean active(String activeNumber);

    /**
     * 用户登录
     *
     * @param user
     * @return
     */
    User login(User user);

    /**
     * 保存用户的签名
     *
     * @param user
     * @return
     */
    boolean userSavePublicKey(User user);

    /**
     * 合同的详细信息
     *
     * @param contractId
     * @return
     */
    Contract contractDetails(int contractId);

    /**
     * 查找公钥
     *
     * @param contractId
     * @return
     */
    String findPublicKey(int contractId);

    /**
     * 用户保存签名
     *
     * @param contract
     * @return
     */
    boolean userSigned(Contract contract);
}
