package com.vonderland.diarydemo.utils;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Vonderland on 2017/2/2.
 */

public class DateTimeUtil {

    private static final String TAG = "DebugDateTimeUtil";

    public static String formatDate(long pubTimeMillis) {

        Date pubDate = new Date(pubTimeMillis);

        DateFormat localDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String localPubDateString = localDateFormat.format(pubDate);

        return localPubDateString;
    }

    public static int getYear(long pubTimeMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(pubTimeMillis);
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth(long pubTimeMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(pubTimeMillis);
        return calendar.get(Calendar.MONTH);
    }

    public static int getDay(long pubTimeMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(pubTimeMillis);
        return calendar.get(Calendar.DATE);
    }
}
