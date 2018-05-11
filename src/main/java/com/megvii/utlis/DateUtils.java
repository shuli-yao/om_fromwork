package com.megvii.utlis;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Megvii on 2017/11/20.
 */
public class DateUtils {
  private static final DateFormat TIMEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  public static String  getThisTimeStr(){
      return  TIMEFORMAT.format(new Date());
  }
}
