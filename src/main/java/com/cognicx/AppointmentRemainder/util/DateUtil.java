package com.cognicx.AppointmentRemainder.util;

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognicx.AppointmentRemainder.constant.ApplicationConstant;


public class DateUtil {

  private DateUtil() {
    throw new IllegalStateException("Utility class");
  }

  private static Logger logger = LoggerFactory.getLogger("COMMON");

  public static final String DATE_MONTH_YEAR_SPACE_PATTERN = "dd MMM yyyy";

  public static final String DATE_MONTH_YEAR_HYPHEN_PATTERN = "dd-MMM-yyyy";

  public static final String DATE_FULL_MONTH_YEAR_SPACE_PATTERN = "dd MMMM yyyy";

  public static final String DATE_MONTH_YEAR_SLASH_PATTERN = "dd/MM/yyyy";

  public static final String DATE_MONTH_STRING_YEAR_SLASH_PATTERN = "dd/MMM/yyyy";

  public static Date convertStringtoDate(final String dateVariable, final String dateFormat) throws Exception {
    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
    Date dt = new Date();
    try {
      if (dateVariable != null && !ApplicationConstant.EMPTY_STR.equalsIgnoreCase(dateVariable)) {
        dt = formatter.parse(dateVariable);
      }
    } catch (Exception e) {
      logger.info("Error :: DateUtil :: convertStringToDate :: " + e);
      throw e;
    }
    return dt;
  }

  public static Date getSysDate() throws Exception {
    Date dt = new Date();
    try {
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_MONTH_YEAR_SPACE_PATTERN);
      LocalDate localDate = LocalDate.now();
      dt = convertStringtoDate(dtf.format(localDate), DATE_MONTH_YEAR_SPACE_PATTERN);
    } catch (Exception e) {
      logger.info("Error :: DateUtil :: getSysDate :: " + e);
    }
    return dt;
  }

  public static String getSystemDate() throws Exception {
    String date = "";
    try {
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_MONTH_YEAR_SPACE_PATTERN);
      LocalDate localDate = LocalDate.now();
      date = dtf.format(localDate);
    } catch (Exception e) {
      logger.info("Error :: DateUtil :: getSystemDate :: " + e);
      throw e;
    }
    return date;
  }

  public static String getFormattedDate(final String dateVariable, final String dateFormat) throws Exception {
    String date = "";
    try {
      if (dateVariable != null && !ApplicationConstant.EMPTY_STR.equalsIgnoreCase(dateVariable)) {
        Date dt = convertStringtoDate(dateVariable, dateFormat);
        SimpleDateFormat format = new SimpleDateFormat(DATE_MONTH_YEAR_SPACE_PATTERN);
        date = format.format(dt);
      }
    } catch (ParseException e) {
      logger.info("Error :: DateUtil :: getFormattedDate :: " + e);
      throw e;
    }
    return date;
  }

  public static String getFullFormattedDate(final String dateVariable, final String dateFormat) throws Exception {
    String date = "";
    try {
      if (dateVariable != null && !ApplicationConstant.EMPTY_STR.equalsIgnoreCase(dateVariable)) {
        Date dt = convertStringtoDate(dateVariable, dateFormat);
        SimpleDateFormat format = new SimpleDateFormat(DATE_FULL_MONTH_YEAR_SPACE_PATTERN);
        date = format.format(dt);
      }
    } catch (ParseException e) {
      logger.info("Error :: DateUtil :: getFormattedDate :: " + e);
      throw e;
    }
    return date;
  }

  public static Date convertUtilDateToSqlDate(final Date date) {
    return java.sql.Date.valueOf(date.toString());
  }


  public static String convertDatetoString(final Date dateVariable, final String dateFormat) throws Exception {
    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
    String dtStr = null;
    try {
      if (dateVariable != null) {
        dtStr = formatter.format(dateVariable);
      }
    } catch (Exception e) {
      logger.info("Error :: DateUtil :: convertStringToDate :: " + e);
      throw e;
    }
    return dtStr;
  }

  public static boolean checkNumeric(String str) {
    boolean returnVal = true;
    String refStr = "0123456789";
    try {
      if ((str == null) || (str.trim().length() == 0)) {
        returnVal = false;
      } else {
        for (int i = 0; i < str.length(); i++) {
          if (refStr.indexOf(str.charAt(i)) == -1) {
            returnVal = false;
            break;
          }
        }
      }
    } catch (Exception e) {
      returnVal = false;
    }
    return returnVal;
  }

  public static String getCurrentDate() {
    Date date = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    return formatter.format(date);
  }
}


