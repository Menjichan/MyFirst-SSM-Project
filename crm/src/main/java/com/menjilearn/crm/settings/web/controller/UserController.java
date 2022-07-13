package com.menjilearn.crm.settings.web.controller;

import com.menjilearn.crm.commons.contants.RespDataContants;
import com.menjilearn.crm.commons.domain.ObjectForResponse;
import com.menjilearn.crm.commons.utils.DateUtils;
import com.menjilearn.crm.settings.pojo.User;
import com.menjilearn.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;

/**
 * @author menji
 * @version 1.0
 * @date 2022/6/16
 */
@Controller
public class UserController {

    @Autowired
    UserService userService;

    //进入登录页面
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String tologin() {
        return "settings/qx/user/login";
    }

    //验证用户是否登录成功
    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody
    public Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        //用与返回信息的对象，便于转成json
        ObjectForResponse objectForResponse = new ObjectForResponse();
        ////判断用户名和密码是否为空
        //if (loginAct == null || loginPwd == null) {
        //    objectForResponse.setCode("0");
        //    objectForResponse.setMessage("用户名或密码不能为空");
        //    return objectForResponse;
        //}
        //封装用户输入的账号密码，用于查询
        HashMap<String, Object> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        User user = userService.queryUserByLoginActAndLoginPwd(map);

        //判断用户是否登录成功
        if (user == null) {
            //没有该用户，登录失败
            objectForResponse.setCode(RespDataContants.RESPONSE_OBJECT_CODE_FAIL);
            objectForResponse.setMessage("用户名或密码错误");
        } else {
            //进一步判断用户是否合法
            if (DateUtils.formatDateTimeToString(new Date()).compareTo(user.getExpireTime()) > 0) {
                objectForResponse.setCode(RespDataContants.RESPONSE_OBJECT_CODE_FAIL);
                objectForResponse.setMessage("用户已过期");
            } else if (user.getLockState().equals(RespDataContants.USER_LOCKSTATE_LOCKED)){
                objectForResponse.setCode(RespDataContants.RESPONSE_OBJECT_CODE_FAIL);
                objectForResponse.setMessage("用户已被冻结");
            } else if (!user.getAllowIps().contains(request.getRemoteAddr())) {
                objectForResponse.setCode(RespDataContants.RESPONSE_OBJECT_CODE_FAIL);
                objectForResponse.setMessage("用户ip非法");
            } else {
                objectForResponse.setCode(RespDataContants.RESPONSE_OBJECT_CODE_SUCCESS);
                session.setAttribute(RespDataContants.SESSION_LOGINUSER, user);

                //如果用户选择了记住密码，需要写入cookie到浏览器
                if (isRemPwd.equals("true")) {
                    Cookie loginActAsCookie = new Cookie("loginAct", user.getLoginAct());
                    loginActAsCookie.setMaxAge(10*24*60*60);
                    response.addCookie(loginActAsCookie);
                    Cookie loginPwdAsCookie = new Cookie("loginPwd", user.getLoginPwd());
                    loginPwdAsCookie.setMaxAge(10*24*60*60);
                    response.addCookie(loginPwdAsCookie);
                } else {
                    Cookie loginActAsCookie = new Cookie("loginAct", "1");
                    loginActAsCookie.setMaxAge(0);
                    response.addCookie(loginActAsCookie);
                    Cookie loginPwdAsCookie = new Cookie("loginPwd", "1");
                    loginPwdAsCookie.setMaxAge(0);
                    response.addCookie(loginPwdAsCookie);
                }
            }
        }
        return objectForResponse;
    }

    //用户安全退出
    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response, HttpSession session) {
        //删除cookie
        Cookie loginActAsCookie = new Cookie("loginAct", "1");
        loginActAsCookie.setMaxAge(0);
        response.addCookie(loginActAsCookie);
        Cookie loginPwdAsCookie = new Cookie("loginPwd", "1");
        loginPwdAsCookie.setMaxAge(0);
        response.addCookie(loginPwdAsCookie);
        //销毁session节省内存
        session.invalidate();

        return "redirect:/";
    }

}
