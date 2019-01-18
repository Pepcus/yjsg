package com.pepcus.appstudent.util;


import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    //GENERIC CONSTANTS
    public static final int DEFAULT_OFFSET = 0;
    public static final int DEFAULT_LIMIT = 50;
    public static final String DESENDING = "-";
    public static final String ASCENDING = "+";
    public static final String DEFAULT_SORT_BY_COMPANY_NAME = "+companyName";
    public static final String DEFAULT_SORT_BY_USER_NAME = "+userName";
    public static final String DEFAULT_SORT_BY_CONFIGURATION_NAME = "+configurationName";
    public static final String DEFAULT_SORT_BY_HOTLINE_ISSUE_CREATED = "-created";
    public static final String SUCCESS_DELETED = "SUCCESSFULLY_DELETED";
    public static final String SUCCESS_DEACTIVATED = "SUCCESSFULLY_DEACTIVATED";
    public static final String SUCCESS_ACTIVATED = "SUCCESSFULLY_ACTIVATED";
    public static final String TOTAL_RECORDS = "totalRecords";
    public static final String LIMIT_PARAM = "limit";
    public static final String OFFSET_PARAM = "offset";
    public static final String SORT_PARAM = "sort";
    public static final String VALID_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String VALID_ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";
    public static final String VALID_ISO_8601_DATE_FORMAT_1 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final String VALID_ISO_8601_DATE_DISPLAY_FORMAT = "yyyy-MM-dd'T'HH:mm:ss+|-hh:mm";
    public static final String MONTH_DATE_YEAR_FORMAT_HANDBOOK = "MMMM dd, yyyy";
    public static final String VALID_FILE_EXTENSION_IMPORT = "csv";
    public static final String[] REQUIRED_HEADERS_COMPANY_CSV_IMPORT = { "CLIENT_NAME", "DISPLAY_NAME", "PHONE", "ADDRESS", "ADDRESS2",
            "CITY", "STATE", "ZIP", "INDUSTRY", "COMPANY_SIZE", "PRODUCER", "CONFIGURATION_NAME" };
    
    public static final String[] REQUIRED_HEADERS_USER_CSV_IMPORT = { "FIRST_NAME", "LAST_NAME", "USER_NAME", "EMAIL", "CLIENT_NAME",
            "PHONE", "ROLE", "DEPARTMENT", "JOB_TITLE", "HR_LENS", "THINKHR_CRUNCH" };
    
    public static final String COMPANY = "COMPANY";
    public static final String USER = "USER";
    public static final String CONTACT = "CONTACT";
    public static final String LOCATION = "LOCATION";
    public static final String CLIENT = "CLIENT";
    public static final String COMPANY_TYPE_BROKER = "broker_partner";
    public static final String COMPANY_TYPE_CLIENT = "client";
    public static final String REQUEST_BODY_JSON = "requestBodyJson";
    public static final String PATTERN_V1 = "/v1/**";
    
    public static final String SPACE = " ";
    public static final String SMALL_OPEN_BRACKET = "(";
    public static final String CLOSE_OPEN_BRACKET = ")";

    public static final String DEFAULT_BROKER_ID = "187624";
    public static final String FILE_IMPORT_RESULT_MSG = "FILE_IMPORT_RESULT";

    public static final String COMMA_SEPARATOR = ",";
    public static final String REGEX_FOR_DOUBLE_QUOTES = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
    public static final String REGEX_FOR_PROJECTION_FIELDS = ",(?![^\\(\\[]*[\\]\\)])";
    public static final String DOUBLE_QUOTE = "\"";
    public static final String QUERY_SEPARATOR = "?";
    public static final String FAILED_COLUMN_TO_IMPORT = "FAILURE_REASON";
    public static final String SKUS_FIELD = "skus";
    public static final String CLIENT_ID_FIELD = "clientId";
    public static final String CLIENT_SECRET_FIELD = "clientSecret";

    //Paychex has special treatment for determining duplicate records
    public static final Integer SPECIAL_CASE_BROKER1 = 187624;
    public static final Integer SPECIAL_CASE_BROKER2 = 205111;
    public static final String NEW_LINE = "\n";
    public static final String ESCAPED_NEW_LINE = "\\n";
    
    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"  
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";  
    public static final String DEFAULT_ACTIVE_STATUS = "1";
    public static final String DEFAULT_COLUMN_VALUE = "";
    
    public static final String AES_PKC_PADDING = "AES/CBC/PKCS5PADDING";
    public static final String AES_ALGO = "AES";
    public static final String BCRYPT_ALGO = "BCrypt";
    public static final String BLOWFISH_ALGO = "Blowfish";
    public static final String BLOWFISH_PKC_PADDING = "Blowfish/CBC/PKCS5PADDING";
    
    public static final String UTF8 = "UTF-8";
    public static final String COMPANY_CUSTOM_COLUMN_PREFIX = "t1_customfield";
    public static final String COMPANY_CUSTOM_HEADER1 = "BUSINESS_ID";
    public static final String USER_CUSTOM_COLUMN_PREFIX = "t1_customfield";
    public static final String USER_COLUMN_CLIENT_ID = "client_id";
    public static final String USER_COLUMN_PASSWORD = "password_apps";
    public static final String USER_COLUMN_ACTIVATION_DATE = "activationDate";
    public static final String USER_COLUMN_BROKERID = "brokerId";
    public static final String COLUMN_ADDEDBY = "addedBy";
    public static final String SEARCH_HELP_COLUMN = "search_help";
    public static final String COMPANY_COLUMN_BROKER = "broker";
    public static final String COMPANY_COLUMN_CLIENT_TYPE = "client_type";
    public static final String LOCATION_TYPE_COLUMN_VALUE = "primary";
    public static final String DEFAULT_PASSWORD = "";
    
    public static final String HR_LENS_NEWSLETTER = "HR Lens";
    public static final String HR_LENS_NEWSLETTER_KEY = "hRLensOptIn";
    public static final String THINKHR_CRUNCH_NEWSLETTER = "ThinkHR Crunch";
    public static final String THINKHR_CRUNCH_NEWSLETTER_KEY = "thinkHRCrunchOptIn";
    public static final String REGEX_NEWSLETTER_SUBSCRIPTION = "0|1";
    public static final String REGEX_FOR_HANDBOOK_NAME = "[^a-zA-Z0-9]+";
    public static final Integer[] TARGETLIST_PRODUCT_IDS = {6, 22, 26};
    public static final String BROKER_CONTACT            = "broker_contact";
    public static final String RE_CONTACT                = "re_contact";
    public static final String TARGETLIST_BROKER_CONTACT = "partneradmins";
    public static final String TARGETLIST_RE_CONTACT     = "partnerclients";
    
    public static final String JWT_TOKEN_THR_CLIENT_ID =  "thr:clientId";
    public static final String JWT_TOKEN_THR_BROKER_ID =  "thr:brokerId";
    public static final String JWT_TOKEN_THR_USER =  "thr:user";
    public static final String JWT_TOKEN_THR_ROLE =  "thr:role";
    public static final String JWT_TOKEN_THR_SUB =  "sub";
    public static final String JWT_TOKEN_THR_ISS =  "iss";
    
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String CONTENT_TYPE_HEADER = "Content-Type";
    public static final String BEARER_TOKEN = "Bearer ";
    
    public static final String BROKER_ID_PARAM = "brokerId";
    public static final String COMPANY_ID_PARAM = "companyId";
    public static final String USER_ID_PARAM = "userId";
    public static final String CONFIGURATION_ID_PARAM = "configurationId";
    public static final String USER_PARAM = "user";
    public static final String ROLE_PARAM = "role";
    public static final String APP_AUTH_DATA = "appAuthData";
    
    public static final String DEVELOPMENT_ENV = "dev";
    
    public static final String INACT = "_INACT";
    public static final String UNDERSCORE = "_";
    public static final String UNDERSCORE_ESC = "\\_";
    public static final String MOD = "%";

    public static final String BROKER_ROLE = "broker";
    public static final String STUDENT_ROLE = "student";
    public static final String CLIENT_ADMIN_ROLE = "client_admin";

    public static final Integer DEFAULT_NUMBER_LICENSES = 1000;

    public static final String WELCOME_EMAIL_TYPE = "welcome";
    public static final String ESIGNATURE_EMAIL_TYPE = "esignature";
    public static final String HASH_KEY = "thinkHRLandI";
    
    //EMAIL PROPERTIES
    public static final String DEFAULT_WELCOME_SUBJECT= "";
    public static final String DEFAULT_WELCOME_BODY = "";
    public static final String FROM_EMAIL_ADDRESS = "";
    
    // For email feature 
    public static final String EMAIL_BODY = "body";
    public static final String RESET_PASSWORD_LINK = "/reset-password/";
    public static final String EMAIL_SUBJECT = "subject";
    public static final String FROM_EMAIL = "from_email";
    public static final String FROM_NAME = "from_name";
    public static final String RESET_PASSWORD_PREFIX = "C";

    public static final String MASTER_CONFIG_KEY = "master";
    public static final String MASTER_CONFIG_NAME = "Master Configuration";
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public static final Integer MAX_SENDGRID_PERSONALISATION = 1000;
    public static final Integer MAX_PHONE_LENGTH = 25;
    
    public static final String ROLE_SYSTEM_ADMIN = "System Administrator";
    public static final String ROLE_THR_ADMIN = "THR Admin";
    public static final String ROLE_BROKER_ADMIN = "Broker Admin";
    
    public static final String DEACTIVATE_ACTION = "deactivate";
    public static final String REACTIVATE_ACTION = "reactivate";
    public static final String IS_ACTIVE = "isActive";
    public static final String VALUE_ZERO = "0";
    public static final String VALUE_ONE = "1";
    public static final String PERMISSION_PREFIX = "restapis";
    public static final String DOT = ".";
    public static final String CHILD_LEVEL_PERMISSION = "child";
    public static final String SELF_ACCESS_PERMISSION = "self";
    
    public static final String COMPANY_RESOURCE = "companies";
    public static final String USER_RESOURCE = "users";
    public static final String CONFIGURATION_RESOURCE = "configurations";
    public static final String DOCUMENT_RESOURCE = "documents";
    public static final String BROKER_RESOURCE = "brokers";
    public static final String CREDENTIALS_RESOURCE = "credentials";
    public static final String HOTLINE_ISSUE_RESOURCE = "issues";
    public static final String BRANDING_RESOURCE = "brandings";
    public static final String HANDBOOK_RESOURCE = "handbooks";
    
    public static final String CREATE = "create";
    public static final String READ = "read";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";
    public static final String GET = "get";
    public static final String LIST = "list";
    public static final String IMPORT = "import";
    public static final String REQUEST_PARAMETERS = "REQUEST_PARAM";
    public static final String PARENT_ACCESS_FILTER = "PARENT_ACCESS_FILTER";
    public static final String PAYCHEX_PREFIX = "Paychex";
    public static final String CSV_FILE_FORMAT = "application/vnd.ms-excel";
    public static final String ADD = "ADD";
    
    public static final String DEFAULT_SKU_KEY = "restapis";
    
    //Constants for OAuth2
    public static final String REFRESH_TOKEN_GRANT_TYPE = "refresh_token";
    public static final String RESOURCE_OWNER_GRANT_TYPE = "password";
    public static final String IMPLICIT_GRANT_TYPE = "implicit";
    public static final String AUTHORIZATION_CODE_GRANT_TYPE = "authorization_code";
    public static final String OPEN_ID_GRANT_TYPE = "openid";
    public static final String IMPERSONATE_GRANT_TYPE = "impersonate";
    public static final String SSO_GRANT_TYPE = "sso";
    public static final List<String> SUPPORTED_CUSTOM_GRANT_TYPES = Arrays.asList(OPEN_ID_GRANT_TYPE, IMPERSONATE_GRANT_TYPE, SSO_GRANT_TYPE);
    
    public static final String SCOPE_READ = "read";
    public static final String SCOPE_WRITE = "write";
    public static final String SCOPE_ALL = "all";
    
    public static final String LEARN_CLIENT_ADMIN_ROLE = "client_admin";
    public static final String LEARN_STUDENT_ROLE = "student";
    
    public static final String LEARN_ADMIN_SKU_KEY = "learn.admin";
    public static final String TRAINING_SKU_KEY = "training";
    public static final String COMPLIANCE_SKU_KEY = "compliance";
    public static final List<String> HANDBOOK_SKU_KEYS_FOR_CONFIG_IMPLICATIONS = new ArrayList<String>() {{
        add(FEDERAL_HANDBOOK_SKU_KEY);
        add(ONE_STATE_HANDBOOK_SKU_KEY);
        add(MULTI_STATE_HANDBOOK_SKU_KEY);
    }};
    public static final String FEDERAL_HANDBOOK_SKU_KEY = "handbooksfederal";
    public static final String ONE_STATE_HANDBOOK_SKU_KEY = "handbooksonestate";
    public static final String MULTI_STATE_HANDBOOK_SKU_KEY = "handbooksmultistate";
    public static final String GREATER_THAN_EQUAL_TO = ">=";
    public static final String EQUALS = "=";
    public static final Integer MULTI_STATE_HB_MIN_COUNT = 2;
    public static final Integer ONE_STATE_FEDERAL_HB_MIN_COUNT = 1;
    public static final String HANDBOOK_TYPE_MULTI_STATE = "multistate";
    public static final String HANDBOOK_TYPE_FEDERAL = "federal";
    public static final String HANDBOOK_TYPE_ALL = "all";
    
    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
    public static final String SESSION = "session";
    
    public static final String SEARCH_SPEC = "searchSpec";
    public static final String PLUS_CHAR = "+";
    public static final String BROKER_NAME = "brokerName";

    public static final String ROLE_TYPE_THINKHR = "thinkhr";
    public static final String ROLE_TYPE_BROKER = "broker";
    public static final String ROLE_TYPE_RE = "re";
    public static final String SKU_TYPE_SYSTEM = "SYSTEM";
    public static final String ISSUER = "issuer";
    public static final String JWK_URL = "jwkUrl";
    public static final String USERINFO_URL = "userInfoUrl";
    public static final String MAPPED_FIELD = "mappedField";
    public static final String DEFAULT_ROLE_FOR_OPENID_USER = "Broker Admin";
    public static final String KEY_ID = "kid";
    public static final String EXPIRY_DATE = "exp";
    
    public static final String SLASH = "/";
    
    public static final String HOTLINE_ISSUE_DEFAULT_CATEGORY = "Other";
    public static final String HOTLINE_ISSUE_SOURCE = "restapis";
    
    public static final String USER_AGENT_KEY = "user-agent";
    public static final String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36";
    
    public static final String IMAGE_TYPE_FOR_CHART = "image/png";
    public static final String PIE_CHART = "pie";
    public static final String DOUGHNUT_CHART = "doughnut";
    public static final String BAR_CHART = "bar";
    public static final String COLUMN_CHART = "column";
    public static final String LINE_CHART = "line";
    public static final String AREA_CHART = "area";
    
    public static final String FONT_WEIGHT = "bold";
    public static final String FONT_COLOR = "black";
    public static final String FONT_SIZE = "15px";
    public static final Integer DOUGHNUT_INNER_SIZE = 170;
    
    public static final String AXIS_TITLE_FONT_COLOR = "grey";
    public static final String AXIS_TITLE_FONT_SIZE = "18PX";
    public static final Integer AXIS_TITLE_MARGIN = 30;
    
    public static final String DATE_RANGE_SEPARATOR = "-";
    
    public static final String LESS_THAN = "[lt]";
    public static final String GREATER_THAN = "[gt]";
    public static final String LESS_THAN_EQUALS = "[lte]";
    public static final String GREATER_THAN_EQUALS = "[gte]";
    public static final String EQUALS_OPERATOR = "[eq]";
    
    public static final List<String> LIST_OPERATORS = new ArrayList<String>(Arrays.asList(LESS_THAN, GREATER_THAN, LESS_THAN_EQUALS, GREATER_THAN_EQUALS, EQUALS_OPERATOR));
    public static final String LAST_UPDATED_ATTRIBUTE = "lastUpdated";
    public static final String OPERATOR_START = "[";
    public static final List<String> OPERATOR_BASED_ATTRIBUTES = new ArrayList<String>(Arrays.asList(LAST_UPDATED_ATTRIBUTE));
    public static final List<String> COLORS_FOR_CHART_LEGENDS = Arrays.asList("#f5a955", "#db6657", "#86c982", "#4ea9d6", "#9c6fb0", "#17ab9b", "#61839c", "#81bc45", 
            "#ccccc7", "#4794c3",  "#008080", "#e6beff", "#aa6e28", "#fffac8", "#800000", "#aaffc3", "#808000", "#ffd8b1", "#000080", "#808080");
    
    public static final String DEFAULT_SORT_BY_LAWALERT_PUB_DATE = "-publicationDate";
    public static final String LAWALERTS_DATE_PATTERN = "yyyy-MM";
    public static final String LAWALERTS_JURISDICTION_ATTRIB = "jurisdiction";
    public static final String LAWALERTS_STARTMONTH_ATTRIB = "startMonth";
    public static final String LAWALERTS_ENDMONTH_ATTRIB = "endMonth";
    public static final String LAWALERTS_PUBLICATION_DATE_ATTRIB = "publicationDate";
    public static final String LAWALERTS_YEAR_ATTRIB = "year";
    public static final String LAWALERTS_MONTH_ATTRIB = "month";
    public static final String LAWALERTS_FEDERAL_ABBR = "FE";
    public static final String LAWALERTS_FEDERAL_NAME = "Federal";
    
    public static final String EMPTY_STRING = "";
    public static final String NULL_STRING = "null";
    
    public static final String API_VERSION = "apiVersion";
    public static final String STATUS = "status";
    public static final String CODE = "code";
    public static final String TIMESTAMP = "timestamp";
    public static final String MESSAGE = "message";
    public static final String API_ERROR_CODE = "errorCode";
    public static final String API_EXCEPTION_DETAIL = "exceptionDetail";
    
    public static final String ERROR_STATUS_CODE = "javax.servlet.error.status_code";
    
    public static final String FIELDS_KEY = "fields";

    public static final List<String> SUPPORTED_MEDIA_TYPES = Arrays.asList(APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE, MULTIPART_FORM_DATA_VALUE);
    
    public static final String DEFAULT_SORT_BY_SIGNER_NAME= "+signerName";
    public static final String DEFAULT_SORT_BY_DOCUMENT_NAME = "+documentName";
    
    public static final String CORE_POLICY_TYPE = "core";
    public static final String FEDERAL_POLICY_TYPE = "federal";
    public static final String STATE_POLICY_TYPE = "state";
    public static final String CUSTOM_POLICY_TYPE = "custom";
    
    public static final String CUSTOM_POLICIES_HEADING = "Custom Policies";
    
    public static final String AMPERSAND_ACTUAL_CHAR = "&";
    public static final String AMPERSAND_REPLACED_CHAR = "&amp;";
    public static final String BR_ACTUAL_TAG = "<br>";
    public static final String BR_REPLACED_TAG = "<br/>";
    public static final String HR_ACTUAL_TAG = "<hr>";
    public static final String HR_REPLACED_TAG = "<hr/>";
    public static final String PARAGRAPH_OPEN_TAG = "<p>";
    public static final String PARAGRAPH_CLOSE_TAG = "</p>";
    public static final String BLOCKQUOTE_OPEN_TAG = "<blockquote>";
    public static final String BLOCKQUOTE_CLOSE_TAG = "</blockquote>";
    
    public static final String CLOSING_STATEMENT_SECTION = "Closing Statement";
    public static final String ACKNOWLEDGEMENT_SECTION = "Acknowledgement of Receipt and Review";
    
    public static final String COMPANY_NAME_KEY = "{{COMPANY-NAME}}";
    public static final String EMPLOYEE_TERM_KEY = "{{EMPLOYEE-TERM}}";
    public static final String COMPANY_TERM_KEY = "{{COMPANY-TERM}}";
    public static final String PRIMARY_CONTACT_KEY = "{{PRIMARY-CONTACT}}";
    public static final String HEAD_COMPANY_KEY = "{{HEAD-OF-COMPANY}}";
    public static final String HEAD_COMPANY_TITLE_KEY = "{{HEAD-OF-COMPANY-TITLE}}";
    public static final String MANAGER_SUPERVISOR_KEY = "{{MANAGER-SUPERVISOR}}";
    
    public static final String API_ACCESS_TOKEN = "ACCESS_TOKEN_VALUE";
    
    public static final String WORD_FORMAT = "docx";
    public static final String PDF_FORMAT = "pdf";
    
    public static final String MIME_TYPE_FOR_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    
    public static final String LANGUAGE_EN = "en";
    public static final String LANGUAGE_ES = "es";
    public static final String LOGIN_IMPERSONATE_ACTION = "login";
    public static final String LOGOUT_IMPERSONATE_ACTION = "logout";
    public static final List<String> IMPERSONATE_ACTIONS = Arrays.asList(LOGIN_IMPERSONATE_ACTION,LOGOUT_IMPERSONATE_ACTION);
    public static final String IMPERSONATION_RESOURCE = "impersonation";
    
    public static final List<String> RESOURCES_BROKERID_NOT_NEEDED = Arrays.asList(BROKER_RESOURCE, CREDENTIALS_RESOURCE, DOCUMENT_RESOURCE, HOTLINE_ISSUE_RESOURCE,
            BRANDING_RESOURCE, IMPERSONATION_RESOURCE);
                
}