package com.menjilearn.crm.workbench.service;

import com.menjilearn.crm.workbench.pojo.Activity;

import java.util.List;
import java.util.Map;

/**
 * @author menji
 * @version 1.0
 * @date 2022/6/25
 */
public interface ActivityService {

    //添加一个刚创建的市场活动
    int saveActivity(Activity activity);

    //根据条件查询市场活动
    List<Activity> queryActivityByConditionForPage(Map<String,Object> queryConditionMap);

    //根据条件查询符合条件市场活动的总条数
    int queryCountOfActivityByCondition(Map<String,Object> queryConditionMap);

    //根据ids批量删除市场活动
    int deleteActivityByIds(String[] ids);

    //根据id查询市场活动
    Activity selectActivityById(String id);

    //保存修改后的市场活动
    int saveEditActivity(Activity activity);

    //查询所有市场活动
    List<Activity> queryAllActivities();

    //根据ids批量查询用户选择了的市场活动
    List<Activity> queryActivityByIds(String[] ids);

    //批量保存导入的市场活动
    int saveCreateActivitiesByList(List<Activity> activities);

    Activity queryActivityForDetailById(String id);
}
