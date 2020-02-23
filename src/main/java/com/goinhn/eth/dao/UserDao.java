package com.goinhn.eth.dao;

import com.goinhn.eth.domain.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserDao {

    @Select("select * from tab_user")
    @Results(id = "userMap",
            value = {
                    @Result(id = true, column = "userId", property = "userId"),
                    @Result(column = "username", property = "username"),
                    @Result(column = "password", property = "password"),
                    @Result(column = "mail", property = "mail"),
                    @Result(column = "accountAddress", property = "accountAddress"),
                    @Result(column = "publicKey", property = "publicKey"),
                    @Result(column = "isActive", property = "isActive"),
                    @Result(column = "activeNumber", property = "activeNumber")
            })
    List<User> findAll();


    @Select("select * from tab_user where userId = #{userId}")
    @ResultMap("userMap")
    User findByUserId(Integer userId);


    @Select("select * from tab_user where username = #{username}")
    @ResultMap("userMap")
    User findByUsername(String username);


    @Select("select * from tab_user where mail = #{mail}")
    @ResultMap("userMap")
    User findByMail(String mail);


    @Select("select * from tab_user where username = #{username} and password = #{password}")
    @ResultMap("userMap")
    User findByUsernameAndPassword(User user);


    @Select("select 8 from tab_user where mail = #{mail} and password = #{password}")
    @ResultMap("userMap")
    User findByMailAndPassword(User user);


    @Select("select * from tab_user where accountAddress = #{accountAddress}")
    @ResultMap("userMap")
    User findByAccountAddress(String accountAddress);


    @Select("select * from tab_user where publicKey = #{publicKey}")
    @ResultMap("userMap")
    User findByPublicKey(String publicKey);


    @Select("select * from tab_user where activeNumber = #{activeNumber}")
    @ResultMap("userMap")
    User findByActiveNumber(String activeNumber);


    @Insert("insert into tab_user(username, password, mail, isActive, activeNumber) " +
            "values(#{username}, #{password}, #{mail}, #{isActive}, #{activeNumber})")
    @SelectKey(keyColumn = "userId", keyProperty = "userId", resultType = Integer.class, before = false, statement = {"select last_insert_id()"})
    int saveUser(User user);


    @Update("update tab_user set publicKey = #{publicKey} where userId = #{userId}")
    int updatePublicKey(User user);


    @Update("update tab_user set accountAddress = #{accountAddress} where userId = #{userId}")
    int updateAccountAddress(User user);


    @Update("update tab_user set isActive = #{isActive} where userId = #{userId}")
    int updateActive(User user);


    @Delete("delete from tab_user where userId = #{userId}")
    int deleteUser(Integer userId);

}
