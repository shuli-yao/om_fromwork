package com.megvii.utlis;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Megvii on 2017/11/20.
 */
public class DateUtils {
  public static final DateFormat TIMEFORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

  public static String  getThisTimeStr(){
      return  TIMEFORMAT.format(new Date());
  }

  public static Date  parseStr(String date) throws ParseException {
        return  TIMEFORMAT.parse(date);
    }
}