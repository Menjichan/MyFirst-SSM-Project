package com.menjilearn.crm.workbench.web.controller;

import com.menjilearn.crm.commons.contants.RespDataContants;
import com.menjilearn.crm.commons.domain.ObjectForResponse;
import com.menjilearn.crm.commons.utils.DateUtils;
import com.menjilearn.crm.commons.utils.HSSFCellTypeUtils;
import com.menjilearn.crm.commons.utils.UUIDUtils;
import com.menjilearn.crm.settings.pojo.User;
import com.menjilearn.crm.settings.service.UserService;
import com.menjilearn.crm.workbench.pojo.Activity;
import com.menjilearn.crm.workbench.pojo.ActivityRemark;
import com.menjilearn.crm.workbench.service.ActivityRemarkService;
import com.menjilearn.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author menji
 * @version 1.0
 * @date 2022/6/25
 */
@Controller
public class ActivityController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityRemarkService activityRemarkService;

    //跳转市场活动主页面
    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request) {
        List<User> userList = userService.queryAllUsers();
        //将用户列表保存到请求作用域中
        request.setAttribute("userList", userList);
        return "workbench/activity/index";
    }

    //创建市场活动
    @RequestMapping("/workbench/activity/createActivity.do")
    @ResponseBody
    public Object createActivity(Activity activity, HttpSession session) {
        //封装参数
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateUtils.formatDateTimeToString(new Date()));
        User user = (User) session.getAttribute(RespDataContants.SESSION_LOGINUSER);
        activity.setCreateBy(user.getId());

        ObjectForResponse objectForResponse = new ObjectForResponse();
        //调用service方法
        try {
            int result = activityService.saveActivity(activity);
            if (result > 0) {
                objectForResponse.setCode(RespDataContants.RESPONSE_OBJECT_CODE_SUCCESS);
            } else {
                objectForResponse.setCode(RespDataContants.RESPONSE_OBJECT_CODE_FAIL);
                objectForResponse.setMessage("系统忙,请稍后再试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            objectForResponse.setCode(RespDataContants.RESPONSE_OBJECT_CODE_FAIL);
            objectForResponse.setMessage("系统忙,请稍后再试...");
        }

        return objectForResponse;
    }

    //分页查询市场活动
    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    @ResponseBody
    public Object queryActivityByConditionForPage(String name, String owner, String startDate, String endDate, int pageNum, int pageSize) {
        //封装参数
        HashMap<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("name", name);
        conditionMap.put("owner", owner);
        conditionMap.put("startDate", startDate);
        conditionMap.put("endDate", endDate);
        conditionMap.put("beginNum", (pageNum - 1) * pageSize);
        conditionMap.put("pageSize", pageSize);

        //调用service方法
        List<Activity> activityList = activityService.queryActivityByConditionForPage(conditionMap);
        int countOfActivity = activityService.queryCountOfActivityByCondition(conditionMap);

        //封装响应信息
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("activityList", activityList);
        resultMap.put("countOfActivity", countOfActivity);

        return resultMap;
    }

    //批量删除市场活动
    @RequestMapping("/workbench/activity/deleteActivityByIds.do")
    @ResponseBody
    public Object deleteActivityByIds(String[] id) {
        ObjectForResponse objectForResponse = new ObjectForResponse();
        try {
            int affectedRows = activityService.deleteActivityByIds(id);
            if (affectedRows > 0) {
                objectForResponse.setCode(RespDataContants.RESPONSE_OBJECT_CODE_SUCCESS);
            } else {
                objectForResponse.setCode(RespDataContants.RESPONSE_OBJECT_CODE_FAIL);
                objectForResponse.setMessage("系统忙，请稍后重试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            objectForResponse.setCode(RespDataContants.RESPONSE_OBJECT_CODE_FAIL);
            objectForResponse.setMessage("系统忙，请稍后重试...");
        }

        return objectForResponse;
    }

    //根据id查询市场活动
    @RequestMapping("/workbench/activity/queryActivityById.do")
    @ResponseBody
    public Object queryActivityById(String id) {
        Activity activity = activityService.selectActivityById(id);
        return activity;
    }

    //保存修改的市场活动
    @RequestMapping("/workbench/activity/saveEditActivity.do")
    @ResponseBody
    public Object saveEditActivity(Activity activity, HttpSession session) {
        //封装参数
        activity.setEditTime(DateUtils.formatDateTimeToString(new Date()));
        User user = (User) session.getAttribute(RespDataContants.SESSION_LOGINUSER);
        activity.setEditBy(user.getId());

        ObjectForResponse objectForResponse = new ObjectForResponse();
        //调用service,保存修改的市场活动
        try {
            int affectedRows = activityService.saveEditActivity(activity);
            if (affectedRows > 0) {
                objectForResponse.setCode(RespDataContants.RESPONSE_OBJECT_CODE_SUCCESS);
            } else {
                objectForResponse.setCode(RespDataContants.RESPONSE_OBJECT_CODE_FAIL);
                objectForResponse.setMessage("系统忙，请稍后");
            }
        } catch (Exception e) {
            e.printStackTrace();
            objectForResponse.setCode(RespDataContants.RESPONSE_OBJECT_CODE_FAIL);
            objectForResponse.setMessage("系统忙，请稍后");
        }

        return objectForResponse;
    }

    //文件下载
    @RequestMapping("/workbench/activity/fileDownload.do")
    public void fileDownload(HttpServletResponse response) throws IOException {
        //1.设置响应类型
        response.setContentType("application/octet-stream;charset=UTF-8");
        //2.获取输出流
        OutputStream os = response.getOutputStream();
        //浏览器接收到响应信息之后，默认情况下，直接在窗口中打开响应信息，即使打不开，也会调用程序打开；只有实在打不开才会激活文件下载窗口
        //可以设置响应头信息，使浏览器接收到响应信息之后，直接激活文件下载窗口，即使能打开也不打开
        response.addHeader("Content-Disposition","attachment;filename=testDownload01.xls");
        //3.将需要下载的文件读取到内存
        FileInputStream is = new FileInputStream("D:\\程序员学习\\动力节点CRM项目（SSM框架版）\\poi-demo\\demo01.xls");
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len=is.read(bytes)) != -1) {
            os.write(bytes,0,len);
        }
        //关闭资源
        is.close();
        os.flush();
    }

    //文件上传
    @RequestMapping("/workbench/activity/fileUpload.do")
    @ResponseBody
    public Object fileUpload(String myName, MultipartFile myFile) throws IOException {
        System.out.println(myName);
        //获取上传文件名
        String originalFilename = myFile.getOriginalFilename();
        String newFileName = UUIDUtils.getUUID() + "-" + originalFilename;

        File file = new File("D:\\javacode\\CRM(SSM-Version)-resourcesFile-Server\\uploadFiles", newFileName);
        myFile.transferTo(file);

        ObjectForResponse objectForResponse = new ObjectForResponse();
        objectForResponse.setCode(RespDataContants.RESPONSE_OBJECT_CODE_SUCCESS);
        objectForResponse.setMessage("上传成功");
        return originalFilename;
    }

    @RequestMapping("/workbench/activity/exportAllActivities.do")
    public void exportAllActivities(HttpServletResponse response) throws IOException{
        //查询并返回所有市场活动
        List<Activity> activities = activityService.queryAllActivities();
        //创建excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();//文件本身
        HSSFSheet sheet = workbook.createSheet("市场活动列表");//工作页
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);

        cell.setCellValue("Id");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("活动名称");
        cell = row.createCell(3);
        cell.setCellValue("开始日期");
        cell = row.createCell(4);
        cell.setCellValue("结束日期");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("描述");
        cell = row.createCell(7);
        cell.setCellValue("活动创建时间");
        cell = row.createCell(8);
        cell.setCellValue("活动创建者");
        cell = row.createCell(9);
        cell.setCellValue("活动修改时间");
        cell = row.createCell(10);
        cell.setCellValue("活动修改者");

        //遍历市场活动List,将市场活动写入excel
        if (activities != null && activities.size() > 0) {
            Activity activity = null;
            for (int i = 0; i < activities.size(); i++) {
                activity = activities.get(i);
                //新建一行
                row = sheet.createRow(i + 1);
                //新建十一列
                cell = row.createCell(0);
                cell.setCellValue(activity.getId());
                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell = row.createCell(2);
                cell.setCellValue(activity.getName());
                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell = row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }
        }

        //生成excel文件
        /*FileOutputStream os = new FileOutputStream("D:\\javacode\\CRM(SSM-Version)-resourcesFile\\activityList.xls");
        workbook.write(os);*/

        //关闭资源
        //workbook.close();
        //os.close();

        //把生成的excel文件下载到客户端
        //设置响应类型、响应头信息
        response.setContentType("application/oct-stream,charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=activityList.xls");
        OutputStream out = response.getOutputStream();
        /*FileInputStream is = new FileInputStream("D:\\javacode\\CRM(SSM-Version)-resourcesFile\\activityList.xls");
        byte[] buff = new byte[256];
        int len = 0;
        while((len=is.read(buff)) != -1) {
            out.write(buff,0,len);
        }*/
        //关闭资源
        //is.close();
        workbook.write(out);

        workbook.close();
        out.flush();

    }

    @RequestMapping("/workbench/activity/exportSelectedActivities.do")
    public void exportSelectedActivities(String[] id,HttpServletResponse response) throws IOException{
        //查询并返回所有市场活动
        List<Activity> activities = activityService.queryActivityByIds(id);
        //创建excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();//文件本身
        HSSFSheet sheet = workbook.createSheet("市场活动列表");//工作页
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);

        cell.setCellValue("Id");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("活动名称");
        cell = row.createCell(3);
        cell.setCellValue("开始日期");
        cell = row.createCell(4);
        cell.setCellValue("结束日期");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("描述");
        cell = row.createCell(7);
        cell.setCellValue("活动创建时间");
        cell = row.createCell(8);
        cell.setCellValue("活动创建者");
        cell = row.createCell(9);
        cell.setCellValue("活动修改时间");
        cell = row.createCell(10);
        cell.setCellValue("活动修改者");

        //遍历市场活动List,将市场活动写入excel
        if (activities != null && activities.size() > 0) {
            Activity activity = null;
            for (int i = 0; i < activities.size(); i++) {
                activity = activities.get(i);
                //新建一行
                row = sheet.createRow(i + 1);
                //新建十一列
                cell = row.createCell(0);
                cell.setCellValue(activity.getId());
                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell = row.createCell(2);
                cell.setCellValue(activity.getName());
                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell = row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }
        }

        //生成excel文件
        /*FileOutputStream os = new FileOutputStream("D:\\javacode\\CRM(SSM-Version)-resourcesFile\\activityList.xls");
        workbook.write(os);*/

        //关闭资源
        //workbook.close();
        //os.close();

        //把生成的excel文件下载到客户端
        //设置响应类型、响应头信息
        response.setContentType("application/oct-stream,charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=selectedActivitiesList.xls");
        OutputStream out = response.getOutputStream();
        /*FileInputStream is = new FileInputStream("D:\\javacode\\CRM(SSM-Version)-resourcesFile\\activityList.xls");
        byte[] buff = new byte[256];
        int len = 0;
        while((len=is.read(buff)) != -1) {
            out.write(buff,0,len);
        }*/
        //关闭资源
        //is.close();
        workbook.write(out);

        workbook.close();
        out.flush();

    }

    //导入市场活动

    @RequestMapping("/workbench/activity/importActivities.do")
    @ResponseBody
    public Object importActivities(MultipartFile activityList,HttpSession session){
        User user = (User) session.getAttribute(RespDataContants.SESSION_LOGINUSER);
        ObjectForResponse objectForResponse = new ObjectForResponse();
        //把接收到的文件写入磁盘
        try {
            //String originalFilename = activityList.getOriginalFilename();
            //String newFilename = UUIDUtils.getUUID() + "-" + originalFilename;
            //File file = new File("D:\\javacode\\CRM(SSM-Version)-resourcesFile-Server\\uploadFiles", newFilename);
            //activityList.transferTo(file);
            InputStream is = activityList.getInputStream();
            //解析excel文件，获取数据，封装到activity，activity封装到list里面
            HSSFWorkbook workbookForImport = new HSSFWorkbook(is);
            HSSFSheet sheet = workbookForImport.getSheetAt(0);
            HSSFRow row = null;
            HSSFCell cell = null;

            //封装参数的对象
            Activity activity = null;
            List<Activity> activities = new ArrayList<>();

            //循环遍历获取数据并封装到对象中
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i);
                activity = new Activity();
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(user.getId());
                activity.setCreateBy(user.getId());
                activity.setCreateTime(DateUtils.formatDateTimeToString(new Date()));

                for (int j = 0; j < row.getLastCellNum(); j++) {
                    cell = row.getCell(j);
                    String cellValue = HSSFCellTypeUtils.getHSSFCellValueToString(cell);
                    //判断该单元格是第几列，然后放到哪个属性里
                    if (j == 0) {
                        activity.setName(cellValue);
                    } else if (j == 1) {
                        activity.setStartDate(cellValue);
                    } else if (j == 2) {
                        activity.setEndDate(cellValue);
                    } else if (j == 3) {
                        activity.setCost(cellValue);
                    } else if (j == 4) {
                        activity.setDescription(cellValue);
                    }
                }
                activities.add(activity);
            }

            //调用service方法保存记录
            int affectedRows = activityService.saveCreateActivitiesByList(activities);
            objectForResponse.setCode(RespDataContants.RESPONSE_OBJECT_CODE_SUCCESS);
            objectForResponse.setRespData(affectedRows);

        } catch (IOException e) {
            e.printStackTrace();
            objectForResponse.setMessage(RespDataContants.RESPONSE_OBJECT_CODE_FAIL);
            objectForResponse.setMessage("系统忙，请稍后重试...");
        }

        return objectForResponse;
    }

    //查看市场活动明细
    @RequestMapping("/workbench/activity/checkActivityDetail.do")
    public String checkActivityDetail(String id, HttpServletRequest request) {
        //调用service查询市场活动及其备注详细信息
        Activity activity = activityService.queryActivityForDetailById(id);
        List<ActivityRemark> activityRemarkList = activityRemarkService.queryActivityRemarkForDetailByActivityId(id);
        //把数据存到作用域中，供前台使用
        request.setAttribute("activity",activity);
        request.setAttribute("activityRemarkList",activityRemarkList);
        //请求转发
        return "workbench/activity/detail";
    }
}
