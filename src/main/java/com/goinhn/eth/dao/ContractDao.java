package com.goinhn.eth.dao;

import com.goinhn.eth.domain.Contract;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 合同数据层
 */
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
                    @Result(column = "partyB", property = "partyB"),
            })
    List<Contract> findAll();


    @Select("select * from tab_contract where contractId = #{contractId}")
    @ResultMap("contractMap")
    Contract findByContractId(Integer contractId);


    @Select("select * from tab_contract where userId = #{userId}")
    @ResultMap("contractMap")
    List<Contract> findByUserId(Integer userId);


    @Select("select count(*) from tab_contract where userId = #{userId} and contractType = #{contractType}")
    int findCountByUserIdAndContractType(Contract contract);

    @Select("select * from tab_contract where userId = #{userId} and contractType = #{contractType} limit #{begin}, #{pageNumber}")
    @ResultMap("contractMap")
    List<Contract> findByUserIdAndContractType(Contract contract);


    @Select("select * from tab_contract where userId = #{userId} and contractHash = #{contractHash}")
    @ResultMap("contractMap")
    Contract findByUserIdAndContractHash(Contract contract);


    @Select("select * from tab_contract where contractHash = #{contractHash}")
    @ResultMap("contractMap")
    List<Contract> findByContractHash(String contractHash);


    @Select("select * from tab_contract where contractName like #{contractName}")
    @ResultMap("contractMap")
    List<Contract> findByContractName(String contractName);


    @Select("select * from tab_contract where contractAddress = contractAddress")
    @ResultMap("contractMap")
    Contract findByContractAddress(String contractAddress);


    @Insert("insert into tab_contract (userId, contractHash, contractName, contractType, contractAddress, partyA, partyB)" +
            "values(#{userId}, #contractHash}, #{contractName}, #{contractType}, #{contractAddress}, #{partyA}, #{partyB}")
    @SelectKey(keyColumn = "contractId", keyProperty = "contractId", resultType = Integer.class, before = false, statement = {"select last_insert_id()"})
    int saveContract(Contract contract);


    @Update("update tab_contract set contractType = #{contractType}, contractAddress = #{contractAddress}")
    int updateContractTypeAndContractAddress(Contract contract);


    @Update("update tab_contract set contractType = #{contractType}, contractAddress = #{contractAddress}, partyA = #{partyA}, partyB = #{partyB}")
    int updateContract(Contract contract);


    @Delete("delete from tab_contract where contractId = #{contractId}")
    int deleteContract(Integer contractId);

}
