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

  public static final String[] ATTENDANCE_REQUIRED_HEADERS = { "id" };

  public static final String REGEX_FOR_SPACE = "\\s";
  public static final String[] OPTIN_REQUIRED_HEADERS = { "id","optIn2019","Y/N","y/n"};
  public static final String APPLICATION_FORCEDOWNLOAD="application/force-download";
  public static final String CONTENT_DISPOSITION="Content-Disposition";
  public static final String ATTACHMENT_FILENAME="attachment; filename=";
  public static final String DOUBLE_QUOTE="\"";
  public static final String VALID_FILE_EXTENSION_IMPORT = "csv";
  public static final String COMMA_SEPARATOR = ",";
  public static final String OPTIN = "optin";
  public static final String ATTENDANCE = "attendance";
  public static final String OPTOUTMESSAGECONTENT = " Dear {{name}}, We heard you will not attend ‘Jain Bal & Yuva Sanskar Sikshan Shivir’ in Sanmati School from 28 April to 5 May, 2019. If you change your mind, please register by clicking on– https://yjsg.in/reg?id=<ID>&secretCode=<Code>. Thanks! YJSG Group.";
  public static final String OPTINMESSAGECONTENT="Dear {{name}}, We have received your confirmation for attending ‘Jain Bal & Yuva Sanskar Sikshan Shivir’ in Sanmati School from 28 April to 5 May, 2019. If you wish to change your personal information, please click on – https://yjsg.in/reg?id=<ID>&secretCode=<Code>. Thanks! YJSG Group.";
  public static final String ISPRESENT="Y";
  public static final String PRESENT_MESSAGE_CONTENT="Dear {{name}}, We appreciate your presence in Shivir today. Thanks! YJSG Group.";
  public static final String ABSENT_MESSAGE_CONTENT="Dear {{name}}, We missed you in Shivir today. Please try to attend for remaining days. Thanks! YJSG Group.";
  public static final String DATE_FORMAT_DDMMYYYY="dd/MM/yyyy"; 
  public static final String ON="on ";
  public static final String VALID="valid";
  public static final String INVALID="invalid";
  public static final String IS_ABSENT = "isabsent";
  public static final String OPTOUT = "optout";
  public static final String WELCOME_SMS="Dear {{name}}, We have received your registration for 'Jain Bal & Yuva Sanskar Sikshan Shivir' in Sanmati School from 28 April to 5 May, 2019. Your ID number is {{studentid}}.Thanks! YJSG Group.";
  public static final String SMS_CREATE="smsCreate";
  public static final String SMS_OPTIN="smsOptIn";
  public static final String SMS_OPTOUT="smsOptOut";
  public static final String SMS_PRESENT="smsPresent";
  public static final String SMS_ABSENT="smsAbsent";
}