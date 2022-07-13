package com.menjilearn.crm.commons.utils;

import java.util.UUID;

/**
 * @Author Menji
 * @Date 2022/6/27
 * @Version 1.0
 */
public class UUIDUtils {

    /**
     * 返回一个没有"-"的UUID
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
