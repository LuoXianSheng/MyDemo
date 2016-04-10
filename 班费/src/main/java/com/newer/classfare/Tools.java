package com.newer.classfare;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Mr_LUO on 2016/2/2.
 */
public class Tools {

    public static String dateFormat(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.format(format.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
