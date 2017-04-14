package com.udacity.assignment.android.popularmoviesstageone.utilities;

/**
 * Created by gaurav.b on 4/10/2017.
 */

public class DateUtils {

    // To Show the Date in Word Format
    public static String convertDate(String date){
        String[] splitDate = date.split("-");
        String year = splitDate[0];
        String month = splitDate[1];
        String day = splitDate[2];
        int mm = Integer.parseInt(month);
        String monthString = getMonth(mm);
        return day+" "+monthString+", "+year;
    }

    // Retrieving the Month in Word Format
    private static String getMonth(int month){
        switch (month){
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
        }
        return String.valueOf(month);
    }
}
