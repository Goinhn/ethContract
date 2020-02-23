package com.goinhn.eth.service.impl;


import com.goinhn.eth.dao.UserDao;
import com.goinhn.eth.domain.User;
import com.goinhn.eth.service.UserService;
import com.goinhn.eth.util.MailUtil;
import com.goinhn.eth.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public String register(User user) {
        if(userDao.findByUsername(user.getUsername()) != null){
            return "该用户名已经注册";
        }
        if(userDao.findByMail(user.getMail()) != null){
            return "该邮箱已经注册";
        }

        user.setActiveNumber(UuidUtil.getUuid());
        user.setIsActive("n");
        userDao.saveUser(user);

        String content="<a href='http://localhost/eth/user/active.html?code="+user.getActiveNumber()+"'>点击激活【智能合同】</a>";
        MailUtil.sendMail(user.getMail(),content,"智能合同激活邮件");

        return "";
    }

    @Override
    public boolean active(String activeNumber) {
        User user = userDao.findByActiveNumber(activeNumber);
        if(user != null){
            user.setIsActive("y");
            userDao.updateActive(user);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public User login(User user) {
        User userBack = userDao.findByUsernameAndPassword(user);
        if(userBack != null && userBack.getIsActive().equalsIgnoreCase("y")){
            return userBack;
        }else{
            userBack = userDao.findByMailAndPassword(user);
            if(userBack != null && userBack.getIsActive().equalsIgnoreCase("y")){
                return userBack;
            }else{
                return null;
            }
        }
    }
}
