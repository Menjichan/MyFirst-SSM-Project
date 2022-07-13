package com.menjilearn.crm.settings.service.serviceImpl;

import com.menjilearn.crm.settings.service.UserService;
import com.menjilearn.crm.settings.mapper.UserMapper;
import com.menjilearn.crm.settings.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author menji
 * @version 1.0
 * @date 2022/6/19
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryUserByLoginActAndLoginPwd(Map<String, Object> loginActAndLoginPwdMap) {
        return userMapper.queryUserByLoginActAndLoginPwd(loginActAndLoginPwdMap);
    }

    @Override
    public List<User> queryAllUsers() {
        return userMapper.selectAllUsers();
    }
}
