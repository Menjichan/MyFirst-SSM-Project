package com.menjilearn.crm.settings.service;

import com.menjilearn.crm.settings.pojo.User;

import java.util.List;
import java.util.Map;

/**
 * @author menji
 * @version 1.0
 * @date 2022/6/19
 */
public interface UserService {
    //根据用户名和密码查询用户
    User queryUserByLoginActAndLoginPwd(Map<String,Object> loginActAndLoginPwd);

    //查询所有用户
    List<User> queryAllUsers();
}
