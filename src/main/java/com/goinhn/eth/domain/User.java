package com.goinhn.eth.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private int userId;             //用户Id
    private String username;        //用户昵称
    private String password;        //用户密码
    private String mail;            //邮箱
    private String accountAddress;  //账户地址
    private String publicKey;       //公钥
    private String isActive;        //账户是否激活
    private String activeNumber;    //激活的号码
}
