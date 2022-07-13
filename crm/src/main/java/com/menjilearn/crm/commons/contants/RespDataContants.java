package com.menjilearn.crm.commons.contants;

/**
 * @author menji
 * @version 1.0
 * @date 2022/6/20
 */
public class RespDataContants {

    public static final String RESPONSE_OBJECT_CODE_SUCCESS = "1";//登录成功
    public static final String RESPONSE_OBJECT_CODE_FAIL = "0";//登录失败

    public static final String USER_LOCKSTATE_LOCKED = "0";//用户被锁定

    public static final String USER_LOCKSTATE_UDLOCKED = "1";//用户被锁定
    public static final String SESSION_LOGINUSER = "loginUser";//登录成功的用户需要放到session作用域中使用

    public static final String ACTIVITY_REMARK_EDIT_FLAG_CREATE = "0";//市场活动备注没有被修改

    public static final String ACTIVITY_REMARK_EDIT_FLAG_EDITED = "1";//市场活动备注被修改过
}
