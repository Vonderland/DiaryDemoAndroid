package com.vonderland.diarydemo.utils;

import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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
}
