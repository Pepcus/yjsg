package com.pepcus.appstudent.service;

import static com.pepcus.appstudent.util.ApplicationConstants.ABSENT_MESSAGE_CONTENT;
import static com.pepcus.appstudent.util.ApplicationConstants.ABSENT_MESSAGE_DAY_CONTENT;
import static com.pepcus.appstudent.util.ApplicationConstants.ATTENDANCE;
import static com.pepcus.appstudent.util.ApplicationConstants.DATE_FORMAT_DDMMYYYY;
import static com.pepcus.appstudent.util.ApplicationConstants.IS_ABSENT;
import static com.pepcus.appstudent.util.ApplicationConstants.ON;
import static com.pepcus.appstudent.util.ApplicationConstants.OPTIN;
import static com.pepcus.appstudent.util.ApplicationConstants.OPTINMESSAGECONTENT;
import static com.pepcus.appstudent.util.ApplicationConstants.OPTIN_OPTOUT;
import static com.pepcus.appstudent.util.ApplicationConstants.OPTOUT;
import static com.pepcus.appstudent.util.ApplicationConstants.OPTOUTMESSAGECONTENT;
import static com.pepcus.appstudent.util.ApplicationConstants.PRESENT_MESSAGE_CONTENT;
import static com.pepcus.appstudent.util.ApplicationConstants.PRESENT_MESSAGE_DAY_CONTENT;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pepcus.appstudent.entity.Student;
import com.pepcus.appstudent.exception.BadRequestException;
import com.pepcus.appstudent.repository.StudentRepository;
import com.pepcus.appstudent.util.SMSUtil;

import antlr.StringUtils;

@Service
public class SMSService {

	@Autowired
	private StudentRepository studentRepository;

	@PersistenceContext
	private EntityManager em;
	
	@Value("${com.pepcus.appstudent.admin.sendAbsentSMS}")
	private boolean isSendAbsentSMS;
	
	@Value("${com.pepcus.appstudent.admin.sendPresentSMS}")
	private boolean isSendPresentSMS;
	
	@Value("${com.pepcus.appstudent.admin.sendOptOut}")
	private boolean isSendOptOutSMS;
	
	@Value("${com.pepcus.appstudent.admin.sendOptIn}")
	private boolean isSendOptInSMS;	
	
	private Logger logger = LoggerFactory.getLogger(SMSService.class);	
	
	/**
	 * Method to Send SMS for OptiIn or attendance activity
	 * @param studentList
	 * @param activity
	 * @param day
	 */
	
	public void sendBulkSMS(List<Student> studentList,String activity,Integer day) {
		logger.info("##### ######### sendBulkSMS method invoked  ######### #####");
		try {
			switch (activity) {
			case OPTIN_OPTOUT:
				if(isSendOptInSMS){
				sendOptInOptOutBulkMessage(studentList,OPTIN);
				}
				if(isSendOptOutSMS){
					sendOptInOptOutBulkMessage(studentList,OPTOUT);
				}
				break;
			case ATTENDANCE:
				if(isSendPresentSMS){
				sendSMSToPresentStudents(studentList,day);
				}
				break;

			case IS_ABSENT:
				if(isSendAbsentSMS){
				sendSMSToAbsentStudents(day);
				}
				break;
			}

		} catch (Exception e) {
			logger.info("Exception: inside sendBulkSMS method ",e);
			throw new BadRequestException("Unable to send the SMS to the user" + e.getMessage());
		}
	}

	
	
	/**
	 * Method to get List of Students who is Opted In 2019 and who is not present on particular day
	 */
	private List<Student> getAbsentStudents(String day){
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Student> criteriaQuery = criteriaBuilder.createQuery(Student.class);
		Root<Student> root = criteriaQuery.from(Student.class);
		ParameterExpression<String> optInExp = criteriaBuilder.parameter(String.class);
		ParameterExpression<String> dayExp = criteriaBuilder.parameter(String.class);
		Predicate predicate=criteriaBuilder.and(criteriaBuilder.equal(root.get("optIn2019"),optInExp),criteriaBuilder.or(criteriaBuilder.equal(root.get(day),""),criteriaBuilder.isNull(root.get(day)),criteriaBuilder.equal(root.get(day),dayExp)));
		criteriaQuery.where(predicate);
		
		List<Student> studentList = em.createQuery(criteriaQuery)
									  .setParameter(optInExp,"Y")
				                      .setParameter(dayExp,"N")
				                      .getResultList();
		return studentList;
	}

	
	/**
	 * Method to get map<day,date>
	 */
	private static Map<Integer,String> getDateMap(){
		Map<Integer,String>map=new HashMap<Integer,String>();
		map.put(1, "28/04/2019");
		map.put(2, "29/04/2019");
		map.put(3, "30/04/2019");
		map.put(4, "01/05/2019");
		map.put(5, "02/05/2019");
		map.put(6, "03/05/2019");
		map.put(7, "04/05/2019");
		map.put(8, "05/05/2019");
		return map;
	}

	
	/**
	 * Method to get map<day,date>
	 */
	private static Map<Integer,String> getDayMap(){
		Map<Integer,String>map=new HashMap<Integer,String>();
		map.put(1, "day1");
		map.put(2, "day2");
		map.put(3, "day3");
		map.put(4, "day4");
		map.put(5, "day5");
		map.put(6, "day6");
		map.put(7, "day7");
		map.put(8, "day8");
		return map;
	}

	
	/**
	 * Method to Send bulk SMS to students who Opted In and who doesn't
	 * @param studentList
	 */	
	private  void sendOptInOptOutBulkMessage(List<Student>studentList,String activity){
		logger.info("##### ######### sendOptInOptOutBulkMessage method invoked  ######### #####");
		
		switch (activity) {
		case OPTIN:	sendOptInSMS(studentList);
			break;

		case OPTOUT: sendOptOutSMS(studentList);
			break;
		}
		
	}
	
	
	public void sendOptOutSMS(List<Student> studentList)  {
		logger.info("##### ######### sendOptOutSMS method invoked  ######### #####");
		Map<String, String> queryParamMap = new HashMap<String, String>();
		for (Student student : studentList) {
		String numbers = student.getMobile();
		try {
				queryParamMap.put("number", numbers);
				if (!"".equals(numbers) && student.getOptIn2019().equalsIgnoreCase("N")) {
					String message = OPTOUTMESSAGECONTENT.replace("{{name}}", student.getName());
					queryParamMap.put("sms", URLEncoder.encode(message, "UTF-8"));
					SMSUtil.invokeSendSMSAPI(queryParamMap);
			}
			} catch (IOException |GeneralSecurityException e) {
				logger.info("Exception: inside sendOptOutSMS method ",e);
				throw new BadRequestException("Unable to send the SMS to the user" + student.getId());
			}
		}
	}



	public void sendOptInSMS(List<Student> studentList) {
		logger.info("##### ######### sendOptInSMS method invoked  ######### #####");
		Map<String, String> queryParamMap = new HashMap<String, String>();
		for (Student student : studentList) {
			String numbers = student.getMobile();
			try {
				queryParamMap.put("number", numbers);
				if (!"".equals(numbers) && student.getOptIn2019().equalsIgnoreCase("Y")) {
					String message = OPTINMESSAGECONTENT.replace("{{name}}", student.getName());
					queryParamMap.put("sms", URLEncoder.encode(message, "UTF-8"));
					SMSUtil.invokeSendSMSAPI(queryParamMap);
				}
			} catch (IOException | GeneralSecurityException e) {
				logger.info("Exception: inside sendOptInSMS method ", e);
				throw new BadRequestException("Unable to send the SMS to the user" + student.getId());
			}
		}		
	}



	/**
	 * Method to Send SMS to students who are present on particular day
	 * @param studentList
	 * @param day
	 */	
	private  void sendSMSToPresentStudents(List<Student>studentList,Integer day){
		logger.info("##### ######### sendSMSToPresentStudents method invoked  ######### #####");
		Map<String, String> queryParamMap = new HashMap<String, String>();
		String mapDate = getDateMap().get(day);
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_DDMMYYYY);
		String todaysDate = dateFormat.format(new Date());
		for (Student student : studentList) {
			String numbers = student.getMobile();
			queryParamMap.put("number", numbers);
			try {
				if (todaysDate.equals(mapDate) && org.apache.commons.lang3.StringUtils.isEmpty(numbers)) {
					String message = PRESENT_MESSAGE_CONTENT.replace("{{name}}",
							student.getName());
					queryParamMap.put("sms", URLEncoder.encode(message, "UTF-8"));

				} else if (!todaysDate.equals(mapDate) && (!"".equals(numbers) || !numbers.equals(null))) {
					String message = PRESENT_MESSAGE_DAY_CONTENT.replace("{{name}}",
							student.getName());
					message = message.replace("{{date}}", ON+mapDate);
					queryParamMap.put("sms", URLEncoder.encode(message, "UTF-8"));

				}
				SMSUtil.invokeSendSMSAPI(queryParamMap);		
			} catch (IOException | GeneralSecurityException e) {
				logger.info("Exception: inside sendSMSToPresentStudents method ",e);
				throw new BadRequestException("Unable to send the SMS to the user" + student.getId());
			}
		}
	}


	/**
	 * Method to Send SMS to students who are absent on particular day
	 * @param day	
	 */
	private void sendSMSToAbsentStudents(Integer day) {
		logger.info("##### ######### sendSMSToAbsentStudents method invoked  ######### #####");
		String mapDay=getDayMap().get(day);
		String mapDate = getDateMap().get(day);
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_DDMMYYYY);
		String todaysDate = dateFormat.format(new Date());
		
		List<Student> studentList = getAbsentStudents(mapDay);
		Map<String, String> queryParamMap = new HashMap<String, String>();
			for (Student student : studentList) {
				try {
					String numbers = student.getMobile();
					queryParamMap.put("number", numbers);
					if (todaysDate.equals(mapDate) && (!"".equals(numbers) || !numbers.equals(null))) {
						String message = ABSENT_MESSAGE_CONTENT.replace("{{name}}", student.getName());
						queryParamMap.put("sms", URLEncoder.encode(message, "UTF-8"));
					} else if(!todaysDate.equals(mapDate) && (!"".equals(numbers) || !numbers.equals(null))) {
						String message = ABSENT_MESSAGE_DAY_CONTENT.replace("{{name}}",
								student.getName());
						message = message.replace("{{date}}", ON+mapDate);
						queryParamMap.put("sms", URLEncoder.encode(message, "UTF-8"));
					}
					 SMSUtil.invokeSendSMSAPI(queryParamMap);
				} catch (IOException | GeneralSecurityException e) {
					logger.info("Exception: inside sendSMSToAbsentStudents method ",e);
					throw new BadRequestException("Unable to send the SMS to the user" + student.getId());
				}
		} 
	}
}

