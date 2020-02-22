package com.goinhn.eth.dao;

import com.goinhn.eth.domain.Contract;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ContractDao {

    @Select("select * from tab_contract")
    @Results(id = "contractMap",
            value = {
                    @Result(id = true, column = "contractId", property = "contractId"),
                    @Result(column = "userId", property = "userId"),
                    @Result(column = "contractHash", property = "contractHash"),
                    @Result(column = "contractName", property = "contractName"),
                    @Result(column = "contractType", property = "contractType"),
                    @Result(column = "contractAddress", property = "contractAddress"),
                    @Result(column = "partyA", property = "partyA"),
                    @Result(column = "publicKeyA", property = "publicKeyA"),
                    @Result(column = "partyB", property = "partyB"),
                    @Result(column = "publicKeyB", property = "publicKeyB")
            })
    List<Contract> findAll();


    @Select("select * from tab_contract where contractId = #{contractId}")
    @ResultMap("contractMap")
    Contract findByContractId(Integer contractId);


    @Select("select * from tab_contract where userId = #{userId}")
    @ResultMap("contractMap")
    List<Contract> findByUserId(Integer userId);


    @Select("select count(*) from tab_contract where userId = #{userId} and contractType = #{contractType}")
    int findCountByUserIdAndContractType(Map map);

    @Select("select * from tab_contract where userId = #{userId} and contractType = #{contractType} limit #{begin}, #{pageNumber}")
    @ResultMap("contractMap")
    List<Contract> findByUserIdAndContractType(Map map);


    @Select("select * from tab_contract where contractHash = #{contractHash}")
    @ResultMap("contractMap")
    Contract findByContractHash(String contractHash);


    @Select("select * from tab_contract where contractName like #{contractName}")
    @ResultMap("contractMap")
    List<Contract> findByContractName(String contractName);


    @Select("select * from tab_contract where contractAddress = contractAddress")
    @ResultMap("contractMap")
    Contract findByContractAddress(String contractAddress);


    @Insert("insert into tab_contract (userId, contractHash, contractName, contractType, contractAddress, partyA, publicKeyA, partyB, publicKeyB)" +
            "values(#{userId}, #contractHash}, #{contractName}, #{contractType}, #{contractAddress}, #{partyA}, #{publicKeyA}, #{partyB}, #{publicKeyB})")
    @SelectKey(keyColumn = "contractId", keyProperty = "contractId", resultType = Integer.class, before = false, statement = {"select last_insert_id()"})
    int saveContract(Contract contract);


    @Update("update tab_contract set contract = #{contract}, contractAddress = #{contractAddress}, partyA = #{partyA}, publicKeyA = {publicKeyA}, partyB = #{partyB}, publicKeyB = #{publicKeyB}")
    int updateContract(Contract contract);


    @Delete("delete from tab_contract where contractId = #{contractId}")
    int deleteContract(Integer contractId);

}
