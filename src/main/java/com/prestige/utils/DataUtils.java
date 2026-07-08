package com.prestige.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtils {
    public static String convertDate(String date, String inputFormatPattern, String outputFormatPattern) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputFormatPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputFormatPattern);

            Date parsedDate = inputFormat.parse(date);
            return outputFormat.format(parsedDate);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format: " + date + ". Expected format: "+inputFormatPattern);
        }
    }
}
