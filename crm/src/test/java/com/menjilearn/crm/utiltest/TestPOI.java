package com.menjilearn.crm.utiltest;

import com.menjilearn.crm.commons.utils.HSSFCellTypeUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 使用apache-poi生成excel文件
 *
 * @Author Menji
 * @Date 2022/7/4
 * @Version 1.0
 */
public class TestPOI {

    @Test
    public void createExcel() throws IOException {
        //创建HSSFWorkbook对象，对应一个excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建HSSFSheet对象，对应一个页
        HSSFSheet sheet = workbook.createSheet("英雄列表");
        //创建HSSFRow对象，对应行
        HSSFRow row = sheet.createRow(0);//参数为行下标，从0开始
        //创建HSSFCell对象，对应列
        HSSFCell cell = row.createCell(0);//参数为列下标，从0开始
        //生成样式对象
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        //写入数据
        cell.setCellValue("上单");
        cell.setCellStyle(cellStyle);//居中
        cell = row.createCell(1);
        cell.setCellValue("打野");
        cell.setCellStyle(cellStyle);//居中
        cell = row.createCell(2);
        cell.setCellValue("中单");
        cell.setCellStyle(cellStyle);//居中

        //在sheet中创建10行，每行写入3列数据
        for (int i = 1; i <= 10 ; i++) {
            row = sheet.createRow(i);

            cell = row.createCell(0);//参数为列下标，从0开始
            //写入数据
            cell.setCellValue(1000 + i);
            cell.setCellStyle(cellStyle);//居中
            cell = row.createCell(1);
            cell.setCellValue(2000 + i);
            cell.setCellStyle(cellStyle);//居中
            cell = row.createCell(2);
            cell.setCellValue(3000 + i);
            cell.setCellStyle(cellStyle);//居中
        }

        //调用工具函数，生成excel文件
        OutputStream os = new FileOutputStream("D:\\程序员学习\\动力节点CRM项目（SSM框架版）\\poi-demo\\demo01.xls");
        workbook.write(os);

        //关闭资源
        os.close();
        workbook.close();

        System.out.println("==========文件创建成功==========");
    }

    //使用poi解析excel
    @Test
    public void testParseExcel() throws IOException {
        //根据指定excel文件生成HSSFWorkbook对象，封装了excel文件的所有信息
        //HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream("D:\\javacode\\CRM(SSM-Version)-download-Client\\activityList.xls"));
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream("D:\\javacode\\CRM(SSM-Version)-resourcesFile-Server\\uploadFiles\\9583d22b46a547f7b05012b92021cc59-activityList.xls"));

        //获取工作页，封装了页的所有信息
        HSSFSheet sheet = workbook.getSheetAt(0);//页的下标，从0开始
        //获取行
        HSSFRow row = null;
        HSSFCell cell = null;
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {//sheet.getLastRowNum()最后一行的下标
            row = sheet.getRow(i);//行的下标，从0开始

            //获取列
            for (int j = 0; j < row.getLastCellNum(); j++) {//row.getLastCellNum()最后一列的下标+1
                cell = row.getCell(j);

                //获取数据,但是要判断数据的类型
                System.out.print(HSSFCellTypeUtils.getHSSFCellValueToString(cell) + "\t\t");

            }
            System.out.println();
        }
    }

}
