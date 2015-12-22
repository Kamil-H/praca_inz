package com.praca_inz;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by KamilH on 2015-12-22.
 */
public class DateAndTime {

    public static String getNewDate(int day, int month, int year) {
        try {
            SimpleDateFormat dateFormatOld = new SimpleDateFormat("dd MM yyyy", Locale.getDefault());
            SimpleDateFormat dateFormatNew = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            Date date = dateFormatOld.parse(String.format("%d %d %d", day, month + 1, year));
            return dateFormatNew.format(date);

        } catch (ParseException e) {
            return null;
        }
    }

    public static int getDaysBetween(String date){
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd MMM yyyy");
        DateTime endDate = DateTime.parse(date, formatter);
        DateTime today = DateTime.parse(getCurrentDate(), formatter);

        Days days = Days.daysBetween(today, endDate);
        int daysBetween = days.getDays();

        return daysBetween;
    }

    public static String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return getNewDate(day, month, year);
    }

    public static String getCurrentTime(){
        return new SimpleDateFormat("dd MMM yyyy, HH:mm").format(new Date());
    }

    public static int[] getDMYfromString(String stringDate){
        try {
            SimpleDateFormat dateFormatNew = new SimpleDateFormat("dd MM yyyy", Locale.getDefault());
            SimpleDateFormat dateFormatOld = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            Date date = dateFormatOld.parse(stringDate);
            String newDate = dateFormatNew.format(date);
            String dateArray[] = newDate.split(" ");
            int array[] = {Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1]), Integer.parseInt(dateArray[2])};
            return array;

        } catch (ParseException e) {
            return null;
        }
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
}
