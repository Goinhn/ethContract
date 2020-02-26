package com.goinhn.eth.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Contract implements Serializable {
    private int contractId;         //合同的Id
    private int userId;             //用户的Id
    private String contractHash;    //合同的hash
    private String contractName;    //合同的名称
    private String contractType;    //合同的类型，“y”：已经签署，“n”：未签署
    private String contractAddress; //合同在以太坊中的地址
    private String partyA;          //甲方
    private String partyB;          //乙方
}