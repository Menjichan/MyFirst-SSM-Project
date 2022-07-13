package com.menjilearn.crm.workbench.web.controller;

import com.menjilearn.crm.commons.contants.RespDataContants;
import com.menjilearn.crm.commons.domain.ObjectForResponse;
import com.menjilearn.crm.commons.utils.DateUtils;
import com.menjilearn.crm.commons.utils.UUIDUtils;
import com.menjilearn.crm.settings.pojo.User;
import com.menjilearn.crm.workbench.pojo.ActivityRemark;
import com.menjilearn.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @Author Menji
 * @Date 2022/7/10
 * @Version 1.0
 */
@Controller
public class ActivityRemarkController {
    @Autowired
    private ActivityRemarkService activityRemarkService;

    @RequestMapping("/workbench/activity/saveCreateActivityRemark.do")
    @ResponseBody
    public Object saveCreateActivityRemark(ActivityRemark remark, HttpSession session) {
        User user = (User) session.getAttribute(RespDataContants.SESSION_LOGINUSER);
        //封装参数
        remark.setId(UUIDUtils.getUUID());
        remark.setCreateTime(DateUtils.formatDateTimeToString(new Date()));
        remark.setCreateBy(user.getId());
        remark.setEditFlag(RespDataContants.ACTIVITY_REMARK_EDIT_FLAG_CREATE);
        //调用service方法保存市场活动备注
        ObjectForResponse objectForResponse = new ObjectForResponse();
        try {
            int affectedRows = activityRemarkService.saveCreateActivityRemark(remark);
            if (affectedRows > 0) {
                objectForResponse.setCode(RespDataContants.RESPONSE_OBJECT_CODE_SUCCESS);
                objectForResponse.setRespData(remark);
            } else {
                objectForResponse.setCode(RespDataContants.RESPONSE_OBJECT_CODE_FAIL);
                objectForResponse.setMessage("系统忙，请稍后...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            objectForResponse.setCode(RespDataContants.RESPONSE_OBJECT_CODE_FAIL);
            objectForResponse.setMessage("系统忙，请稍后...");
        }

        return objectForResponse;
    }

    //删除市场活动备注
    @RequestMapping("/workbench/activity/deleteActivityRemarkById.do")
    @ResponseBody
    public Object deleteActivityRemarkById(String id) {

        ObjectForResponse objectForResponse = new ObjectForResponse();
        try {
            int affectedRows = activityRemarkService.deleteActivityRemarkById(id);
            if (affectedRows > 0) {
                objectForResponse.setCode(RespDataContants.RESPONSE_OBJECT_CODE_SUCCESS);
                //objectForResponse.setRespData(affectedRows);
            } else {
                objectForResponse.setCode(RespDataContants.RESPONSE_OBJECT_CODE_FAIL);
                objectForResponse.setMessage("系统忙，请稍后...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            objectForResponse.setCode(RespDataContants.RESPONSE_OBJECT_CODE_FAIL);
            objectForResponse.setMessage("系统忙，请稍后...");
        }

        return objectForResponse;
    }

    //保存修改的市场活动备注
    @RequestMapping("/workbench/activity/saveUpdateActivityRemark.do")
    @ResponseBody
    public Object saveUpdateActivityRemark(ActivityRemark activityRemark,HttpSession session) {
        User user = (User) session.getAttribute(RespDataContants.SESSION_LOGINUSER);
        //封装参数
        activityRemark.setEditTime(DateUtils.formatDateTimeToString(new Date()));
        activityRemark.setEditBy(user.getId());
        activityRemark.setEditFlag(RespDataContants.ACTIVITY_REMARK_EDIT_FLAG_EDITED);
        //调用service
        ObjectForResponse objectForResponse = new ObjectForResponse();
        try {
            int affectedRows = activityRemarkService.saveUpdateActivityRemark(activityRemark);
            if (affectedRows > 0) {
                objectForResponse.setCode(RespDataContants.RESPONSE_OBJECT_CODE_SUCCESS);
                objectForResponse.setRespData(activityRemark);
            } else {
                objectForResponse.setCode(RespDataContants.RESPONSE_OBJECT_CODE_FAIL);
                objectForResponse.setMessage("系统忙，请稍后...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            objectForResponse.setCode(RespDataContants.RESPONSE_OBJECT_CODE_FAIL);
            objectForResponse.setMessage("系统忙，请稍后...");
        }
        return objectForResponse;
    }

}
