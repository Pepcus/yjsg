/**
 * 
 */
package com.pepcus.appstudent.util;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pepcus.appstudent.entity.Student;
import com.pepcus.appstudent.exception.BadRequestException;

/**
 * @author Ajay
 *
 */
public class SMSUtil {
	
	public static final String TOKEN = "6cd48ee1d55a1a21ea8ab94c14c5afc9";
	public static final String ADMIN = "Admin";
	public static final String COORDINATOR = "Coordinator";
	public static final String LOCAL_HOST = "127.0.0.1";
	public static final String SENDER = "YJSGIN";
	public static final String ROUTE = "2";
	public static final String TYPE = "1";
	public static final String SMS_GATEWAY_BASEPATH = "http://sms.yjsg.in/";
	public static final String SMS_GATEWAY_PATH = "httpapi/httpapi";
	public static final String SMS_GATEWAY_METHOD = "POST";
	public static final String NON_STANDARD_HTTP_HEADER = "X-Forwarded-For";
	public static final String INIT_VECTOR = "PepcusEMSProduct";    // must be within 16 bytes IV
	
	/**
	 * Sends a SMS to newly created student
	 * @param student
	 */
	private static Logger logger = LoggerFactory.getLogger(SMSUtil.class);
	public static void sendSMS(Student student) {
		Map<String, String> queryParamMap = new HashMap<String, String>();
		
		try {
				String numbers = student.getMobile();
				if (!"".equals(numbers)) {
					String message = ApplicationConstants.WELCOME_SMS.replace("{{name}}",student.getName());
					message = message.replace("{{studentid}}",String.valueOf(student.getId()));	
					
				queryParamMap.put("number", numbers);
				queryParamMap.put("sms", URLEncoder.encode(message, "UTF-8"));
				try {
					invokeSendSMSAPI(queryParamMap);
				} catch (Exception e) {
					throw new BadRequestException("Unable to send the SMS to the user" + student.getId());
				}
			}
			
		} catch (Exception e) {
			 throw new BadRequestException("Unable to send the SMS to the user" + e.getMessage()); 
		}
		
	}
	
	
	
	
	
	
	/**
	 * Method to invoke sms.yjsg.in messaging api
	 * 
	 * @param basePath
	 * @param path
	 * @param method
	 * @param queryParams
	 * @return
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	public static void invokeSendSMSAPI(Map<String, String> queryParams)
			throws GeneralSecurityException, IOException {
		logger.info("##### #### invokeSendSMSAPI method invoked to send SMS for contact no.: "+queryParams.get("number"));
		StringBuffer url = new StringBuffer();
		url.append(SMS_GATEWAY_BASEPATH);
		url.append(SMS_GATEWAY_PATH);
		
		queryParams.put("token", TOKEN);
		queryParams.put("sender", SENDER);
		queryParams.put("route", ROUTE);
		queryParams.put("type", TYPE);

		
		if (null != queryParams && !queryParams.isEmpty()) {
			url.append("?");
			for (Map.Entry<String, String> entry : queryParams.entrySet()) {
				String param = entry.getKey();
				String value = entry.getValue();
				url.append(param);
				url.append("=");
				url.append(value);
				url.append("&");
			}
			int len = url.length();
			url.deleteCharAt(len-1);// remove last "&"
			
			
		}
		
		HttpResponse response=null; 

		try {
			CloseableHttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet();
			request.setURI(new URI(url.toString()));
			response = client.execute(request);	
		} catch (Exception e) {
		}

	}
	

}
