package com.firemanagement.utils;

import java.util.Date;

public class DateUtils {
    /**
     * 计算两个时间差,秒数,绝对值
     *
     * @param date1 再次打开对话框的时间
     * @param date2 发送验证码的时间
     * @return
     */
    public static Long getDateDifference(Date date1, Date date2) {
        return (date1.getTime() - date2.getTime()) / 1000;
    }

}
