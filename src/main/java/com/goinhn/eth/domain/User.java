package com.goinhn.eth.domain;

import java.io.Serializable;

public class User implements Serializable {
    private int userId;
    private String username;
    private String password;
    private String mail;
    private String accountAddress;
    private String publicKey;
    private String isActive;
    private String activeNumber;

    public User() {
    }

    public User(int userId, String username, String password, String mail, String accountAddress, String publicKey, String isActive, String activeNumber) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.accountAddress = accountAddress;
        this.publicKey = publicKey;
        this.isActive = isActive;
        this.activeNumber = activeNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAccountAddress() {
        return accountAddress;
    }

    public void setAccountAddress(String accountAddress) {
        this.accountAddress = accountAddress;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getActiveNumber() {
        return activeNumber;
    }

    public void setActiveNumber(String activeNumber) {
        this.activeNumber = activeNumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", mail='" + mail + '\'' +
                ", accountAddress='" + accountAddress + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", isActive='" + isActive + '\'' +
                ", activeNumber='" + activeNumber + '\'' +
                '}';
    }
}
