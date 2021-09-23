package com.xiaojumao.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-06-30 22:34
 * @Modified By:
 */
public class DateFormatUtil {
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String dateFormatToStr(Date date){
        String str = null;
        if(date != null) {
            str = DateFormatUtil.format.format(date);
            str = str.substring(0,str.length()-3);
        }
        return str;
    }

    public static Timestamp dateFormatToTimestamp(Date date){
        return new Timestamp(date.getTime());
    }

    public static long toTime(String formatString){
        try {
            return format.parse(formatString+":00").getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
