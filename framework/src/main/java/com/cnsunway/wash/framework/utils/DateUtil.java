package com.cnsunway.wash.framework.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    static SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
    static SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static SimpleDateFormat date2 = new SimpleDateFormat("yyyy.MM.dd  HH:mm");
    static SimpleDateFormat date3 = new SimpleDateFormat("HH");
    static SimpleDateFormat date4 = new SimpleDateFormat("yyyy.MM.dd");
    static SimpleDateFormat date5 = new SimpleDateFormat("mm");
    static SimpleDateFormat dateFormatExpectS = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    static SimpleDateFormat date6 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static String getCurrentDate() {
        return date1.format(new Date());
    }

    public static String getCurrentDate2() {
        return date.format(new Date());
    }

   public static String getRechargeTime(String time){

       Date d;
       try {
           d = date1.parse(time);
       } catch (ParseException e) {
           d = new Date();
           e.printStackTrace();
       }

       return date.format(d);

   }
    public static String getTodayDate() {

        Date mDate = new Date(System.currentTimeMillis());

        return date.format(mDate);
    }

    public static String getTomorrowDate() {
        Date mDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24);

        return date.format(mDate);
    }

    public static String getOneAfterTomorrowDate() {
        Date mDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24
                * 2);

        return date.format(mDate);
    }

    public static String getFourDate() {
        Date mDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24
                * 3);

        return date.format(mDate);
    }

    public static String getFiveDate() {
        Date mDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24
                * 4);

        return date.format(mDate);
    }

    public static String getSixDate() {
        Date mDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24
                * 5);

        return date.format(mDate);
    }


    public static String getSevenDate() {
        Date mDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24
                * 6);

        return date.format(mDate);
    }


    public static int getCurrentHour() {
        return Integer.parseInt(date3.format(new Date()));
    }

    public static int getCurrentHourAfterHalf() {
        return Integer.parseInt(date3.format(new Date(System.currentTimeMillis() + 1000 * 60 * 30)));
    }



//    public static String getWeekOfDate(Context context, String dt) {
//        String[] weekDays = {context.getString(R.string.sunday), context.getString(R.string.monday), context.getString(R.string.tuesday), context.getString(R.string.wensyday), context.getString(R.string.thursday), context.getString(R.string.friday), context.getString(R.string.saturday)};
//        Calendar cal = Calendar.getInstance();
//        try {
//            cal.setTime(date.parse(dt));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
//        if (w < 0)
//            w = 0;
//        return weekDays[w];
//    }

//    public static String getMyDate(Context context, String dt, Date now) {
//
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        String nowString = formatter.format(now);
//        Calendar c = new GregorianCalendar();
//        c.setTime(now);
//        Calendar c2 = new GregorianCalendar();
//        c2.setTime(now);
//        String t1 = formatter.format(c.getTime());
//        c.add(Calendar.DATE, 1);
//        String t2 = formatter.format(c.getTime());
//        c2.add(Calendar.DATE, 2);
//        String t3 = formatter.format(c2.getTime());
//        if (t1.equals(dt)) {
//            dt = context.getString(R.string.today);
//        } else {
//            dt = getWeekOfDate(context, dt);
//        }
//        return dt;
//
////		else if(t2.equals(dt)){
////			dt = context.getString(R.string.tomorrow);
////		}else if(t3.equals(dt)){
////			dt = context.getString(R.string.after_tomorrow);
////		}
//
//    }

    public static String getServerDate(String date) {

        Date d;
        try {
            d = date1.parse(date);
        } catch (ParseException e) {
            d = new Date();
            e.printStackTrace();
        }

        return dateFormatExpectS.format(d);
    }
    public static long getMilliTime(String date){
        long time = 0;
        try {
            time = date1.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(time);
        return time;
    }


}
