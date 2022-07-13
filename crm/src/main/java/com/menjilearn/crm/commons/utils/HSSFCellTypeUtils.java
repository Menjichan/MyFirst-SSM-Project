package com.menjilearn.crm.commons.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * @Author Menji
 * @Date 2022/7/7
 * @Version 1.0
 */
public class HSSFCellTypeUtils {
    /**
     * 从指定的HSSFCell中获取值，统一以字符串形式返回
     * @param cell
     * @return
     */
    public static String getHSSFCellValueToString(HSSFCell cell) {

        String val = "";
        if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
            val = cell.getStringCellValue();
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
            val = cell.getNumericCellValue()+ "";
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
            val = cell.getBooleanCellValue()+ "";
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
            val = cell.getCellFormula();
        } else {
            val = "";
        }

        return val;
    }
}
