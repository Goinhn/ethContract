package com.goinhn.eth.dao;

import com.goinhn.eth.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface userDao {


    List<User> findAll();

    User findByUserId(Integer userId);

    User findByUsername(String username);

    User findByMail(String mail);

    User findByUsernameAndPassword(Map map);

    User findByAccountAddress(String accountAddress);

    User findByPublicKey(String publicKey);

    void saveUser(User user);

    void insertPublicKey(Map map);

    void updateActive(Map map);

}
