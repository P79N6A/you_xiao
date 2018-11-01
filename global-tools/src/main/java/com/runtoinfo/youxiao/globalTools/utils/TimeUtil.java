package com.runtoinfo.youxiao.globalTools.utils;


import com.google.gson.internal.bind.util.ISO8601Utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by QiaoJunChao on 2018/9/12.
 */
@SuppressWarnings("all")
public class TimeUtil {

    /**
     * iso8601时间标准转Date
     *
     * @param iso8601 时间格式
     * @param type    是否需要精确到几点几分
     * @return 时间组合返回结果
     * @throws ParseException
     */
    public static String iso8601ToDate(String iso8601, int type) {
        try {
            Date date = ISO8601Utils.parse(iso8601, new ParsePosition(0));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            StringBuilder buffer = new StringBuilder("");
            buffer.append(calendar.get(Calendar.YEAR))
                    .append("-")
                    .append(calendar.get(Calendar.MONTH) < 10 ? "0" + calendar.get(Calendar.MONTH) : calendar.get(Calendar.MONTH))
                    .append("-")
                    .append(calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + calendar.get(Calendar.DAY_OF_MONTH) : calendar.get(Calendar.DAY_OF_MONTH));
            if (type == 0) {
                buffer.append(" ")
                        .append(calendar.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + calendar.get(Calendar.HOUR_OF_DAY) : calendar.get(Calendar.HOUR_OF_DAY))
                        .append(":")
                        .append(calendar.get(Calendar.MINUTE) < 10 ? "0" + calendar.get(Calendar.MINUTE) : calendar.get(Calendar.MINUTE));
            }
            return buffer.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTimeDif(String ISO1806) {
        try {
            //Date date = ISO8601Utils.parse(ISO1806, new ParsePosition(0));

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(ISO1806);
            long publish = format.parse(format.format(date), new ParsePosition(0)).getTime() / 1000;
            long now = System.currentTimeMillis() / 1000;
            long dif = now - publish;
            if (dif < 60) return dif + "秒前";
            else if (60 < dif && dif < 3600) {
                long time = dif / 60;
                return time + "分钟前";
            } else if (dif > 3600 && dif < 3600 * 24) {
                return dif / 3600 + "小时前";
            } else {
                return format.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前时间（年-月-日）
     */

    public static String getNowDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }

    public static String getNowDates(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }
}
