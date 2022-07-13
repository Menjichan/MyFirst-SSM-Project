package com.menjilearn.crm.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author menji
 * @version 1.0
 * @date 2022/6/20
 */
public class DateUtils {

    /**
     * 对时间进行格式化成字符串,格式为"yyyy-MM-dd HH:mm:ss"
     * @param date
     * @return
     */
    public static String formatDateTimeToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }
}
