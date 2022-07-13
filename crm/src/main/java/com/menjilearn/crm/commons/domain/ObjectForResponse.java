package com.menjilearn.crm.commons.domain;

/**
 * @author menji
 * @version 1.0
 * @date 2022/6/20
 */
public class ObjectForResponse {

    private String code;//用于标记获取用户成功还是失败，1表示成功，0表示失败
    private String message;//失败后客户端的弹窗信息
    private Object respData;//返回的其他数据内容

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getRespData() {
        return respData;
    }

    public void setRespData(Object respData) {
        this.respData = respData;
    }
}
