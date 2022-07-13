package com.menjilearn.crm.workbench.service;

import com.menjilearn.crm.workbench.pojo.ActivityRemark;

import java.util.List;

/**
 * @Author Menji
 * @Date 2022/7/10
 * @Version 1.0
 */

public interface ActivityRemarkService {
    //根据市场活动id查询备注
    List<ActivityRemark> queryActivityRemarkForDetailByActivityId(String activityId);


    //保存创建的市场活动备注
    int saveCreateActivityRemark(ActivityRemark activityRemark);

    //根据id删除指定的市场活动备注
    int deleteActivityRemarkById(String id);

    //根据id更新市场活动备注的内容
    int saveUpdateActivityRemark(ActivityRemark activityRemark);
}
