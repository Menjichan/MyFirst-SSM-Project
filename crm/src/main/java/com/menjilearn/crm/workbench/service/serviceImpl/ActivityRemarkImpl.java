package com.menjilearn.crm.workbench.service.serviceImpl;

import com.menjilearn.crm.workbench.mapper.ActivityRemarkMapper;
import com.menjilearn.crm.workbench.pojo.ActivityRemark;
import com.menjilearn.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Menji
 * @Date 2022/7/10
 * @Version 1.0
 */
@Service("activityRemarkService")
public class ActivityRemarkImpl implements ActivityRemarkService {

    @Autowired
    private ActivityRemarkMapper activityRemarkMapper;

    @Override
    public List<ActivityRemark> queryActivityRemarkForDetailByActivityId(String Id) {
        return activityRemarkMapper.selectActivityRemarkForDetailByActivityId(Id);
    }

    @Override
    public int saveCreateActivityRemark(ActivityRemark activityRemark) {
        return activityRemarkMapper.insertActivityRemark(activityRemark);
    }

    @Override
    public int deleteActivityRemarkById(String id) {
        return activityRemarkMapper.deleteActivityRemarkById(id);
    }

    @Override
    public int saveUpdateActivityRemark(ActivityRemark activityRemark) {
        return activityRemarkMapper.updateActivityRemark(activityRemark);
    }
}
