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
  public static final String STATUS_CODE_200="200";
  public static final String STATUS_OK="OK";
  public static final String SMS_SENT_SUCCESSFULLY="SMS sent successfully";
  public static final String SMS_NOT_SENT="SMS not sent";
  public static final String FOR_ABSENT_STUDENTS=" for absent students";
  public static final String FOR_PRESENT_STUDENTS=" for present students";
  public static final String FOR_OPTOUT_STUDENTS=" for OptOut students";
  public static final String FOR_OPTIN_STUDENTS=" for OptIn students";
  public static final String FAILED_TO_SEND_SMS="Failed..! to send SMS";
  public static final String SMS_FEATURE_DISABLE="SMS not sent.Please make sure that send SMS feature is enable";
  public static final String STATUS_CODE_501="501";
  public static final String UNABLE_TO_READ_JSON="Unable to read Json value";
  public static final String DAY_INVALID=" Given input day is not valid";
  public static final String FAILED_TO_GENERATE="Failed to generate duplicate CSV file";
  public static final String STATUS_CODE_304="304";
  public static final String UNABLE_TO_READ_CSV="Failed to read CSV file..!";
  public static final String SMS_OPTOUT_FEATURE_DISABLE="Please make sure that send OptOutSMS feature is enabled";
  public static final String SMS_OPTIN_FEATURE_DISABLE=" Please make sure that send OptInSMS feature is enabled";
  public static final String INVALID_DATA="Invalid data";
  public static final String NO="N";
  public static final String UPDATED_SUCCESSFULLY="Updated Successfully";
  public static final String RECORD_NOT_EXIST="recordNotExist";
  public static final String SOME_UPDATED_SOME_FAILED="Some records failed and some updated";
  public static final String INVALID_DATA_IN_ID_COLUMN="Invalid data in 'id' column can not process further: ";
  public static final String FAILED_TO_UPDATE="Failed to update records: ";
  public static final String DAY="day";
  
}