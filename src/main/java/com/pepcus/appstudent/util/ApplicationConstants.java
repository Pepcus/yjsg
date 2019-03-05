package com.pepcus.appstudent.util;


/**
 * Class to keep all the constants used by application
 * 
 * @author Surabhi Bhawsar
 * @since 2017-11-09
 *
 */
public class ApplicationConstants {
    
    private ApplicationConstants () {
        
    }

  public static final String[] ATTENDANCE_REQUIRED_HEADERS = { "id", "day1", "day2", "day3", "day4",
            "day5", "day6", "day7","day8"};

  public static final String REGEX_FOR_SPACE = "\\s";
  public static final String[] OPTIN_REQUIRED_HEADERS = { "id","optIn2019"};
  public static final String APPLICATION_FORCEDOWNLOAD="application/force-download";
  public static final String CONTENT_DISPOSITION="Content-Disposition";
  public static final String ATTACHMENT_FILENAME="attachment; filename=";
  public static final String DOUBLE_QUOTE="\"";
  public static final String VALID_FILE_EXTENSION_IMPORT = "csv";
  public static final String COMMA_SEPARATOR = ",";
  public static final String OPTIN = "optin";
  public static final String ATTENDANCE = "attendance";
  public static final String OPTOUTMESSAGECONTENT = " Dear {{name}}, We heard you will not attend ‘Jain Bal & Yuva Sanskar Sikshan Shivir’ in Sanmati School from 28 April to 5 May, 2019. If you change your mind, please register by clicking on– https://yjsg.in/reg?id=<ID>&secretCode=<Code>. Thanks! YJSG Group.";
  public static final String OPTINMESSAGECONTENT="Dear <Name>, We have received your confirmation for attending ‘Jain Bal & Yuva Sanskar Sikshan Shivir’ in Sanmati School from 28 April to 5 May, 2019. If you wish to change your personal information, please click on – https://yjsg.in/reg?id=<ID>&secretCode=<Code>. Thanks! YJSG Group.";
}