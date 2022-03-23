package com.hgy.tool;

import android.text.TextUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MyTimeUtil {

    /**
     * 格式化日期时间
     * <p>
     *
     * @return
     */
    public static String formatDatetime5(Date date) {
        SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        return datetimeFormat.format(date);
    }

    /**
     * 获取系统时间
     * 日期时间格式yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String formatDatetime(Date date) {
        SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return datetimeFormat.format(date);
    }

    /**
     * 获取系统时间
     * 时间格式 yyyyMMddHHmmss
     *
     * @return
     */

    public static String getSystemTIme() {
        Date date = new Date();
        SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return datetimeFormat.format(date);
    }


    /**
     * 获取系统时间
     * 日期时间格式yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String formatDatetimeName(Date date) {
        SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyyMMdd");
        return datetimeFormat.format(date);
    }

    /**
     * 获取系统时间
     * 时间格式 HH:mm:ss
     *
     * @param type
     * @return
     */

    public static Date getNowTime(String type) {
        try {
            Date date = new Date();
            SimpleDateFormat datetimeFormat = new SimpleDateFormat(type);
            String time = datetimeFormat.format(date);
            Date newData = datetimeFormat.parse(time);
            return newData;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取系统时间
     * 时间格式 yyyy/M/d HH:mm:ss
     *
     * @return
     */

    public static Date getNowDate() {
        try {
            Date date = new Date();
            SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = datetimeFormat.format(date);
            Date newData = datetimeFormat.parse(time);
            return newData;
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("出错=====" + e.toString());
            return null;
        }
    }


    /**
     * 获取系统时间
     * 时间格式 yyyy/M/d HH:mm:ss
     *
     * @return
     */

    public static String getNowDateTime() {
        Date date = new Date();
        SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = datetimeFormat.format(date);
        return time;
    }


    /**
     * 获取系统时间
     * 时间格式 yyyy/M/d HH:mm:ss
     *
     * @return
     */

    public static String DateTime() {
        Date date = new Date();
        SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = datetimeFormat.format(date);
        return time;
    }


    /**
     * 时间格式化
     * 时间格式 HH:mm:ss
     *
     * @return
     */

    public static Date getDataTime(String time) {
        try {
            DateFormat format = new SimpleDateFormat("HH:mm:ss");
            Date data = format.parse(time);
            return data;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 时间格式化
     * 时间格式 2019/6/24 23:59:59
     *
     * @return
     */

    public static Date getDataDate(String time) {
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date data = format.parse(time);
            return data;
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("出错=====" + time + e.toString());
            return null;
        }
    }

    /**
     * 时间格式化
     * 时间格式 2019/6/24 23:59:59
     *
     * @return
     */

    public static Date getDataDate1(String time) {
        try {
            DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            Date data = format.parse(time);
            return data;
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("出错=====" + time + e.toString());
            return null;
        }
    }


    /**
     * 获取系统日期
     * 日期格式yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    /**
     * 格式化日期时间
     * <p>
     * 日期时间格式yyyyMMdd
     *
     * @return
     */
    public static String formatDatetime3(Date date) {
        SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return datetimeFormat.format(date);
    }

    /**
     * 格式化日期时间
     * <p>
     * 日期时间格式yyyyMMdd
     *
     * @return
     */
    public static String getTargetTime(int day) {
        try {
            SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
            Date beginDate = new Date();
            Calendar date = Calendar.getInstance();
            date.setTime(beginDate);
            date.set(Calendar.DATE, date.get(Calendar.DATE) - 1);
            Date endDate = dft.parse(dft.format(date.getTime()));
            SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd");
            return datetimeFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }


    }

    /**
     * 设置系统时间
     *
     * @param datetimes
     */
    public static void setSystemTime(String datetimes) {
        try {
            String time = dateChange(datetimes);
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("date -s " + time + "; \n");
            os.flush();
        } catch (IOException e) {
            e.toString();
        }
    }

    /**
     * 日期转换 yyyyMMddHHmmss--->yyyyMMdd.HHmmss
     *
     * @param date
     * @return
     */
    public static String dateChange(String date) {

        try {
            DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            Date cahngdate = format.parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd.HHmmss");
            String dateString = formatter.format(cahngdate);
            return dateString;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * @param date     要转换的时间
     * @param in_type  原始时间格式
     * @param out_type 输出格式
     * @return
     */
    public static String dateChange(String date, String in_type, String out_type) {
        if (TextUtils.isEmpty(date)) {
            return formatDate(new Date());
        }
        if (date.equals("长期")) {
            return "2121-12-11";
        }
        try {
            DateFormat format = new SimpleDateFormat(in_type);
            Date cahngdate = format.parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat(out_type);
            String dateString = formatter.format(cahngdate);
            return dateString;
        } catch (Exception e) {
            e.printStackTrace();
            return date;
        }
    }

    /**
     * 日期转换 yyyy/M/d HH:mm:ss--->yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String dateChangeTwo(String date) {

        try {
            DateFormat format = new SimpleDateFormat("yyyy/M/d HH:mm:ss");
            Date cahngdate = format.parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(cahngdate);
            return dateString;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取日历
     *
     * @param time
     * @return
     */
    public static Calendar getCalender(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = sdf.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static int hourGet = 23;
    public static int minuteGet = 59;

    public static long getTime() {
        Calendar calender = Calendar.getInstance();
        calender.set(Calendar.MINUTE, minuteGet);
        calender.set(Calendar.HOUR_OF_DAY, hourGet);
        long time = calender.getTimeInMillis();
        //Toast.makeText(getApplicationContext(), time + "last", Toast.LENGTH_LONG).show();
        //做一个判断，如果获取的设置时间小于当前时间，则需要把提醒设置为后一天，否则会立马执行提醒
        if (time < System.currentTimeMillis()) {
            calender.set(Calendar.DAY_OF_MONTH, calender.get(Calendar.DAY_OF_MONTH) + 1);
            calender.set(Calendar.MINUTE, minuteGet);
            calender.set(Calendar.HOUR_OF_DAY, hourGet);
            time = calender.getTimeInMillis();
        }

        return time;
        // Toast.makeText(getApplicationContext(), time + "finalily", Toast.LENGTH_LONG).show();
    }


    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     * @author jqlin
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime() || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否在星期内
     *
     * @param Week
     * @return
     */
    public static boolean isWeekIn(String Week) {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK) - 1);
        if ("0".equals(mWay)) {
            mWay = "7";
        }
        int indexOf = Week.indexOf(mWay);
        boolean flag;
        if (indexOf != -1) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    /**
     * 判断是否有权限
     *
     * @param passStartTime 允许通行开始时间
     * @param passEndTime   允许通行结束时间
     * @param passWeek      允许通行星期内的指定几天
     * @param passTime      允许通行天内的时间段
     * @return
     */
    public static boolean isJurisdiction(String passStartTime, String passEndTime, String passWeek, String passTime) {
        if (isNUll(passStartTime) || isNUll(passEndTime)) {
            if (isNUll(passWeek)) {
                if (isNUll(passTime)) {
                    return true;
                } else {
                    return isPassTime(passTime);//判断是否在指定通行时间
                }
            } else {
                if (isWeekIn(passWeek)) {//判断是否在指定星期内
                    if (isNUll(passTime)) {
                        return true;
                    } else {
                        return isPassTime(passTime);//判断是否在指定通行时间内
                    }
                } else {
                    return false;
                }
            }
        } else {
            boolean flag = isEffectiveDate(getNowDate(), getDataDate(passStartTime), getDataDate(passEndTime));//判断是否在指定通行日期时间内
            if (flag) {
                if (isNUll(passWeek)) {
                    if (isNUll(passTime)) {
                        return true;
                    } else {
                        return isPassTime(passTime);//判断是否在指定通行时间
                    }
                } else {
                    if (isWeekIn(passWeek)) {//判断是否在指定星期内
                        if (isNUll(passTime)) {
                            return true;
                        } else {
                            return isPassTime(passTime);//判断是否在指定通行时间内
                        }
                    } else {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }

    }

    /**
     * 判断2个时间大小
     * yyyy-MM-dd HH:mm 格式（自己可以修改成想要的时间格式）
     *
     * @param time1
     * @param time2
     * @return
     */
    public static int timeCompare(String time1, String time2) {
        int i = 0;
        //注意：传过来的时间格式必须要和这里填入的时间格式相同
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = dateFormat.parse(time1);//开始时间
            Date date2 = dateFormat.parse(time2);//结束时间
            // 1 结束时间小于开始时间 2 开始时间与结束时间相同 3 结束时间大于开始时间
            if (date2.getTime() < date1.getTime()) {
                //time2<time1
                i = 1;
            } else if (date2.getTime() == date1.getTime()) {
                //time2=time1
                i = 2;
            } else if (date2.getTime() > date1.getTime()) {
                //time2>time1
                i = 3;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * 判断字符串是否为空
     *
     * @param data
     * @return
     */
    public static boolean isNUll(String data) {
        if (data == null) {
            return true;
        }
        String str = data.replaceAll(" ", "");
        if (str.equals("") || str == null || str.equals("null")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否在通行时间内
     *
     * @param passTime
     * @return
     */
    public static boolean isPassTime(String passTime) {
        String[] passTimes = passTime.split(",");
        System.out.println(passTimes.length);
        boolean flag = true;
        if (passTimes.length > 1) {
            for (int i = 0; i < passTimes.length; i++) {
                String time = passTimes[i];
                String[] times = time.split("-");
                flag = isEffectiveDate(getNowTime("HH:mm:ss"), getDataTime(times[0]), getDataTime(times[1]));
                if (flag) {
                    break;
                }
            }
        } else {
            String time = passTimes[0];
            String[] times = time.split("-");
            flag = isEffectiveDate(getNowTime("HH:mm:ss"), getDataTime(times[0]), getDataTime(times[1]));
        }
        return flag;
    }

    /**
     * 判断系统时间和服务端返回的时间是否相差10分钟
     * yyyyMMddHHmmss 格式（自己可以修改成想要的时间格式）
     *
     * @param endTime
     * @return
     */
    public static boolean timeCompare(String endTime) {
        Date d = getDataDate1(endTime);
        Date endDate = new Date(System.currentTimeMillis());
        long diff = endDate.getTime() - d.getTime();
        if (diff >= -10 * 60 * 1000 || diff <= -10 * 60 * 1000) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断2个时间大小
     * yyyy-MM-dd HH:mm 格式（自己可以修改成想要的时间格式）
     *
     * @param endTime
     * @return
     */
    public static int timeCompareRegister(String endTime) {
        //注意：传过来的时间格式必须要和这里填入的时间格式相同
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date date1 = getNowTime("yyyyMMdd");//系统时间
            Date date2 = dateFormat.parse(endTime);//传入时间
            // 1 结束时间小于开始时间 2 开始时间与结束时间相同 3 结束时间大于开始时间
            long time = date2.getTime() - date1.getTime();
            if (time < 0) {
                return 0;
            } else {
                long day = time / (1000 * 60 * 60 * 24);   //以天数为单位取整
                return (int) day;
            }
        } catch (Exception e) {
            return 0;
        }
    }


}
