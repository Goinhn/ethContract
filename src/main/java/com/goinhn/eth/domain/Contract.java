package com.goinhn.eth.domain;

import java.io.Serializable;

public class Contract implements Serializable {
    private int contractId;
    private int userId;
    private String contractHash;
    private String contractName;
    private String contractType;
    private String contractAddress;
    private String partyA;
    private String partyB;

    public Contract() {
    }

    public Contract(int contractId, int userId, String contractHash, String contractName, String contractType, String contractAddress, String partyA, String partyB) {
        this.contractId = contractId;
        this.userId = userId;
        this.contractHash = contractHash;
        this.contractName = contractName;
        this.contractType = contractType;
        this.contractAddress = contractAddress;
        this.partyA = partyA;
        this.partyB = partyB;
    }

    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContractHash() {
        return contractHash;
    }

    public void setContractHash(String contractHash) {
        this.contractHash = contractHash;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getPartyA() {
        return partyA;
    }

    public void setPartyA(String partyA) {
        this.partyA = partyA;
    }

    public String getPartyB() {
        return partyB;
    }

    public void setPartyB(String partyB) {
        this.partyB = partyB;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "contractId=" + contractId +
                ", userId=" + userId +
                ", contractHash='" + contractHash + '\'' +
                ", contractName='" + contractName + '\'' +
                ", contractType='" + contractType + '\'' +
                ", contractAddress='" + contractAddress + '\'' +
                ", partyA='" + partyA + '\'' +
                ", partyB='" + partyB + '\'' +
                '}';
    }
}