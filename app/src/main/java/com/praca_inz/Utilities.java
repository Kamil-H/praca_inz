package com.praca_inz;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by KamilH on 2015-10-18.
 */
public class Utilities {

    public static boolean isNotNull(String txt){
        return txt!=null && txt.trim().length()>0 ? true: false;
    }

    public static double roundOff(double number){
        BigDecimal bigDecimal = new BigDecimal(number);
        BigDecimal roundedWithScale = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);

        return roundedWithScale.doubleValue();
    }

    public static String getTripTime(long duration){
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.SECOND, (int) duration);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        String hourString = String.valueOf(hour);
        String minString = String.valueOf(min);
        if(hour < 10){
            hourString = "0" + hourString;
        }
        if(min < 10){
            minString = "0" + minString;
        }

        return hourString + ":" + minString;
    }
}
