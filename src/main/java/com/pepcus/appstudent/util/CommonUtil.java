package com.pepcus.appstudent.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.pepcus.appstudent.entity.Coordinator;
import com.pepcus.appstudent.entity.StudentWrapper;

/**
 * Class to keep common utility methods
 * 
 * @author Shubham Solanki
 * @since 12-02-2018
 *
 */
public class CommonUtil {
	
	public static final String TOTAL_RECORDS = "RECORDS";
	
	/**
	 * Method to convert Date to String 
	 * 
	 * @param date
	 * @return
	 */
	public static String convertDateToString(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return dateFormat.format(date);
	}
	
	/**
     * To set request attributes 
     * 
     * @param attributeName
     * @param attributeValue
     */
    public static void setRequestAttribute(String attributeName, Object attributeValue) {
    	HttpServletRequest request = getRequest();
        if (request != null) {
            request.setAttribute(attributeName, attributeValue);
        }
    }
    
    /**
     * To fetch attribute value from request for given attribute name 
     * 
     * @param attributeName
     * @return
     */
    public static Object getRequestAttribute(String attributeName) {
        HttpServletRequest request = getRequest();
        Object attrVal = null; 
        if (request != null) {
            return request.getAttribute(attributeName);
        }
        return attrVal;
    }
    
    /**
     * Fetch request object from RequestContextHolder 
     * 
     * @return
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes servletReqAttr = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if ( servletReqAttr != null) {
            return servletReqAttr.getRequest();
        }
        return null;
    }
    
	public static boolean isValidateEmail(String email) {
		Pattern p = Pattern.compile("^(.+)@(.+)$");
		Matcher m = p.matcher(email);
		return (m.find() && m.group().equals(email));
	}

	public static boolean isContentAlphabetsOnly(String fieldName) {
		Pattern p = Pattern.compile("^[a-zA-Z]*$");
		Matcher m = p.matcher(fieldName);
		return (m.find() && m.group().equals(fieldName));

	}

	public static boolean isValidMobileNumber(String mobileNumber) {
		Pattern p = Pattern.compile("[- +()0-9]+");
		Matcher m = p.matcher(mobileNumber);
		return (m.find() && m.group().equals(mobileNumber));
	}
	
	public static String dateFormatForJsonResponse(String stringDate) {
		String responseDate = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = dateFormat.parse(stringDate);
			DateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
			responseDate = dateFormat1.format(date);
			return responseDate;
		} catch (ParseException e) {
			return responseDate;
		}
	}
	
	public static boolean validateStringDateWithFormat(String stringDate, String format) {
		boolean isValidDate = false;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			simpleDateFormat.setLenient(false);
			simpleDateFormat.parse(stringDate);
			isValidDate = true;
		} catch (ParseException e) {
			isValidDate = false;
		}
		return isValidDate;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Object> getBeanFromCSV(MultipartFile file, Class className) {
		List<Object> beanObjectList = null;
		try {
			CsvToBean csvToBean = new CsvToBean();
			CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()));
			beanObjectList = csvToBean.parse(setColumMapping(), csvReader);
			return beanObjectList;
		} catch (IOException e) {
			return beanObjectList;
		}
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	private static HeaderColumnNameMappingStrategy setColumMapping() {
		HeaderColumnNameMappingStrategy<Coordinator> strategy = new HeaderColumnNameMappingStrategy<Coordinator>();
		strategy.setType(Coordinator.class);
		return strategy;
	}
	
	/**
     * Method to generate secretKey for student
     * 
     * @return
     */
    public static String generateSecretKey() {
        Random random = new Random();
        return String.format("%04d", random.nextInt(10000));
    }

}
