package com.pepcus.appstudent.util;


/**
 * Class to keep all the constants used by application
 * 
 * @author Akshay Sakunde
 * @since 2019-02-09
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
  public static final String ISPRESENT="Y";
  public static final String PRESENT_MESSAGE_CONTENT="Dear {{name}}, We appreciate your presence in Shivir today. Thanks! YJSG Group.";
  public static final String ABSENT_MESSAGE_CONTENT="Dear {{name}}, We missed you in Shivir today. Please try to attend for remaining days. Thanks! YJSG Group.";
  public static final String DATE_FORMAT_DDMMYYYY="dd/MM/yyyy"; 
  public static final String ON="on ";
  public static final String VALID="valid";
  public static final String INVALID="invalid";
  public static final String IS_ABSENT = "isabsent";
  public static final String OPTOUT = "optout";
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
  public static final String EXCEPTION_PARSING_DATE="Exception while parsing the date";
  public static final String FROM_AND_TO_DATE_NOT_VALID=" from date and to date of shivir is not valid";
  public static final String[] HEADERS = { "id","name","father_name","gender","age","education","occupation","mother_mobile","father_mobile","email","address","bus_num","bus_stop","remark","class_attended_2016","class_attended_2017","class_attended_2018","class_attended_2019","class_room_no_2016","class_room_no_2017","class_room_no_2018","class_room_no_2019","attendance_2016","attendance_2017","attendance_2018","attendance_2019","marks_2016","marks_2017","marks_2018","marks_2019","opt_in_2018","course_2018","course_2019","course_2017","course_2016","secret_key","remark","created_date","last_modified_date","opt_in_2019","print_status", "day1","day2","day3", "day4","day5", "day6", "day7", "day8"};
  public static final String BULK_UPDATE = "bulk update";
  public static final String FILE_NOT_FOUND ="File not found..! Please select a file";
  public static final String INVALID_FILE_FORMAT="Upload is supported only for 'CSV' data files";
  public static final String REQUIRED_HEADERS="Uploaded file should contain column as: ";
  public static final String EMPTY_FILE="There is no record found in file";
  public static final String MOBILE="mobile";
  public static final String ID="id";
  public static final String NULL="NULL";
  public static final String EXACT_DUPLICATE = " We already have your registration. To login you will receive an SMS on your registered phone number ";
  public static final String DATE_FORMAT_DD_MM_YYYY = "dd/MM/yyyy";
  
  public static final String VAL_Y = "Y";
  public static final String VAL_N = "N";
  public static final String VAL_TRUE = "true";
  public static final String VAL_FALSE = "false";
  public static final String REG_STATUS_REG = "REG";
  public static final String REG_STATUS_CNF = "CNF";
  public static final String PAYMENT_STATUS_COMPLETE = "Complete";
  public static final String PAYMENT_STATUS_NA = "NA";
  public static final String PAYMENT_STATUS_PENDING = "Pending";
  public static final String GENDER_MALE = "Male";
  public static final String GENDER_FEMALE = "FeMale";
  
  
  // YJSG SMS Constants
  public static final String WELCOME_SMS ="प्रिय  {{name}}, YJSG जैन शिविर (सन्मति स्कूल - ४ मई से १२ मई २०२० तक) के लिए आपका रजिस्ट्रेशन प्राप्त हुआ।  आपका ID {{studentid}} है। Thanks, YJSG.";
  public static final String OPT_OUT_SMS ="प्रिय {{name}}, YJSG जैन शिविर (सन्मति स्कूल - ४ मई से १२ मई २०२० तक) के लिए आपकी अस्वीकृति प्राप्त हुई। अगर आप फिर भी आना चाहते है तो https://yjsg.in/reg?id=<ID>&secretCode=<Code> पर क्लिक करें। Thanks! YJSG.";
  public static final String OPT_IN_SMS ="प्रिय {{name}}, YJSG जैन शिविर (सन्मति स्कूल - ४ मई से १२ मई २०२० तक) के लिए आपका रजिस्ट्रेशन कन्फर्म है। किसी भी परिवर्तन के लिए - https://yjsg.in/reg?mode=e&id=<ID>&secretCode=<Code>. Thanks! YJSG.";

  public static final String ALREADY_REGISTER_SMS = "प्रिय {{name}}, YJSG जैन शिविर (सन्मति स्कूल - ४ मई से १२ मई २०२० तक) के लिए आपका रजिस्ट्रेशन प्राप्त हुआ। आपका ID {{studentid}} है। Thanks, YJSG.";
  public static final String PARTIAL_DUPLICATE_SMS = "प्रिय {{name}}, YJSG जैन शिविर (सन्मति स्कूल - ४ मई से १२ मई २०२० तक) के लिए आपका रजिस्ट्रेशन प्राप्त हुआ। आपका ID एवं अन्य जानकारी शीघ्र ही SMS पर भेजी जाएगी। Thanks, YJSG.";
  public static final String PARTIAL_DUPLICATE_SMS_TO_ADMIN = "Potential Duplicate Entry Created with Duplicate Record ID # {{DuplicateRegistrationId}} matching with Student ID # {{studentId}}.";

  public static final String COORDINATOR_WELCOME_SMS="Dear {{name}}, We have received your coordinator registration for 'YJSG - Jain Shivir' in Sanmati school from 4 May to 12 May, 2020. Your ID # {{coordinatorId}} & SecretCode # {{secretCode}}. Thanks! YJSG.";

  
  // GMS SMS Constants
  public static final String GMS_WELCOME_SMS="प्रिय  {{name}}, आपका रजिस्ट्रेशन गोम्मटसार कर्मकांड शिविर के लिए प्राप्त हुआ । आपका ID {{studentid}} है।";
  public static final String GMS_PAYMENT_SMS= "रजिस्ट्रेशन शुल्क ₹{{gmsRegPayment}} है। कृपया यह शुल्क paytm नंबर {{paymentContactNumber}} पर भेजें। शुल्क भरने के बाद इसी नंबर पर अपना नाम और फोन नंबर WhatsApp पर भेजें। शुल्क प्राप्त होने के बाद आप इसी वेबसाइट पर रजिस्ट्रेशन कर सकेंगे।";
  public static final String GMS_PAYMENT_CNF_SMS = "We have received your registration fee. Now you can register for rituals camp from here (http://yjsg.in/gms/reg)";
  
}