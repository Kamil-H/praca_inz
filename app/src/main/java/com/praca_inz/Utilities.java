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
    private static String[] monthNames = {"styczeń" ,"luty" ,"marzec" ,"kwiecień" ,"maj" ,"czerwiec" ,"lipiec" ,"sierpień" ,"wrzesień" ,"październik" ,"listopad", "grudzień"};
    private static ArrayList months = new ArrayList(Arrays.asList(monthNames));

    // dodawanie nazwy miesiąca
    public static String dateTextToNumber(String date){
        String dateArr[] = date.split(" ");
        int month = months.indexOf(dateArr[1]);
        month++;
        return dateArr[0] + " " + String.valueOf(month) + " " + dateArr[2];
    }

    // dodawanie liczby reprezentującej miesiąc
    public static String dateNumberToText(String date){
        String dateArr[] = date.split(" ");

        return dateArr[0] + " " + monthNames[Integer.valueOf(dateArr[1])-1] + " " + dateArr[2];
    }

    public static String replace(String date){
        String dateArr[] = date.split(" ");

        return dateArr[0] + "." + dateArr[1] + "." + dateArr[2];
    }

    public static String getCurrentDate(){
        return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date());
    }

    public static boolean isNotNull(String txt){
        return txt!=null && txt.trim().length()>0 ? true: false;
    }

    public static String timeConversion(double time) {
        int seconds = (int) time;

        final int MINUTES_IN_AN_HOUR = 60;
        final int SECONDS_IN_A_MINUTE = 60;

        int minutes = seconds / SECONDS_IN_A_MINUTE;
        seconds -= minutes * SECONDS_IN_A_MINUTE;

        int hours = minutes / MINUTES_IN_AN_HOUR;
        minutes -= hours * MINUTES_IN_AN_HOUR;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static String twoDigitString(int number) {
        if (number == 0) {
            return "00";
        }
        if (number / 10 == 0) {
            return "0" + number;
        }
        return String.valueOf(number);
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
