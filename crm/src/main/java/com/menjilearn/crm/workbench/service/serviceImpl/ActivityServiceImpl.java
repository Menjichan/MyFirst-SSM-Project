package com.menjilearn.crm.workbench.service.serviceImpl;

import com.menjilearn.crm.workbench.mapper.ActivityMapper;
import com.menjilearn.crm.workbench.pojo.Activity;
import com.menjilearn.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author menji
 * @version 1.0
 * @date 2022/6/25
 */
@Service("activityService")
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public int saveActivity(Activity activity) {
        return activityMapper.insertActivity(activity);
    }

    @Override
    public List<Activity> queryActivityByConditionForPage(Map<String, Object> queryConditionMap) {
        return activityMapper.selectActivityByConditionForPage(queryConditionMap);
    }

    @Override
    public int queryCountOfActivityByCondition(Map<String, Object> queryConditionMap) {
        return activityMapper.selectCountOfActivityByCondition(queryConditionMap);
    }

    @Override
    public int deleteActivityByIds(String[] ids) {
        return activityMapper.deleteActivityByIds(ids);
    }

    @Override
    public Activity selectActivityById(String id) {
        return activityMapper.selectActivityById(id);
    }

    @Override
    public int saveEditActivity(Activity activity) {
        return activityMapper.updateActivity(activity);
    }

    @Override
    public List<Activity> queryAllActivities() {
        return activityMapper.selectAllActivities();
    }

    @Override
    public List<Activity> queryActivityByIds(String[] ids) {
        return activityMapper.selectActivitiesByIds(ids);
    }

    @Override
    public int saveCreateActivitiesByList(List<Activity> activities) {
        return activityMapper.insertActivitiesByList(activities);
    }

    @Override
    public Activity queryActivityForDetailById(String id) {
        return activityMapper.selectActivityForDetailById(id);
    }
}
