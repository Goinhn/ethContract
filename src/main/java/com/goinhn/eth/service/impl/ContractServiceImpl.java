package com.goinhn.eth.service.impl;


import com.goinhn.eth.dao.ContractDao;
import com.goinhn.eth.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("contactService")
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractDao contractDao;
}
