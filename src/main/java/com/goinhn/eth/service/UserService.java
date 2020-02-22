package com.goinhn.eth.service;

import com.goinhn.eth.domain.User;

public interface UserService {

    String register(User user);

    boolean active(String activeNumber);

    User login(User user);
}
