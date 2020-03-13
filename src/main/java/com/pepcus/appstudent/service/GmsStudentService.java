package com.pepcus.appstudent.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pepcus.appstudent.convertor.GmsStudentEntityConvertor;
import com.pepcus.appstudent.entity.GmsStudent;
import com.pepcus.appstudent.exception.ResourceNotFoundException;
import com.pepcus.appstudent.repository.GmsStudentRepository;
import com.pepcus.appstudent.specifications.GmsStudentSpecification;
import com.pepcus.appstudent.util.ApplicationConstants;
import com.pepcus.appstudent.util.SMSUtil;

/**
 * Service class to manage gms student related business operations
 * 
 * @author Sandeep Vishwakarma
 * @since 05-03-2020
 *
 */
@Service
public class GmsStudentService {
	
	@Value("${send_reg_sms_gms_student}")
	String sendRegistrationSms;
	
	@Value("${send_payment_sms_gms_student}")
	String sendPaymentSms;
	
	@Value("${gms_reg_payment_amt}")
	String paymentAmount;

	@Value("${gms_payment_contact_number}")
	String paymentContactNumber;

	@Autowired
	GmsStudentRepository gmsStudentRepository;

	/**
	 * Method to persist gms student entity
	 * 
	 * @param gmsStudent
	 * @return
	 */
	@Transactional
	public GmsStudent persistStudentGMSEntity(GmsStudent gmsStudentEntity) {
		return gmsStudentRepository.save(gmsStudentEntity);
	}

	/**
	 * Method to get StudentGMS entity for id
	 * 
	 * @param id
	 * @return
	 */
	public GmsStudent getGmsStudentEntity(Integer id) {
		GmsStudent gmsStudentEntity = gmsStudentRepository.findOne(id);
		if (null == gmsStudentEntity) {
			throw new ResourceNotFoundException("No Student found for id : " + id);
		}
		return gmsStudentEntity;

	}

	/**
	 * Method to get Gms Student entity
	 * 
	 * @param name
	 * @param mobileNumber
	 * @return
	 */
	private List<GmsStudent> getStudentsByAttributes(String name, String mobileNumber) {
		return gmsStudentRepository.findAll(GmsStudentSpecification.getStudents(name, mobileNumber));
	}

	/**
	 * Method to get all students
	 * 
	 * @return
	 */
	public List<GmsStudent> getStudentList() {
		return GmsStudentEntityConvertor.setDateInGmsStudentEntityList(gmsStudentRepository.findAll());
	}

	/**
	 * Method to get GmsStudent for given studentId
	 * 
	 * @param id
	 * @return
	 */
	public GmsStudent getStudent(Integer id) {
		GmsStudent gmsStudentEntity = getGmsStudentEntity(id);
		return GmsStudentEntityConvertor.setDateInGmsStudentEntity(gmsStudentEntity);
	}

	/**
	 * Method to get GmsStudent List for given mobileNumber
	 * 
	 * @param studentId
	 * @return
	 */
	public List<GmsStudent> getStudentsByMobileNumber(String mobileNumber) {
		List<GmsStudent> gmsStudents = getStudentsByAttributes(null, mobileNumber);
		return GmsStudentEntityConvertor.setDateInGmsStudentEntityList(gmsStudents);
	}

	/**
	 * Method to create/register new StudentGMS
	 * 
	 * @param request
	 */
	public GmsStudent createStudent(GmsStudent request) {
		// persist gms student entity
		GmsStudent gmsStudentEntity = persistStudentGMSEntity(
				GmsStudentEntityConvertor.convertGmsStudentEntity(request));

		if (gmsStudentEntity.getPaymentStatus().equalsIgnoreCase(ApplicationConstants.PAYMENT_STATUS_PENDING)
				&& Boolean.parseBoolean(sendPaymentSms)) {
			// send welcome sms to student
			sendPaymentSms(gmsStudentEntity);
		} else if (Boolean.parseBoolean(sendRegistrationSms)) {
			// send welcome sms to student
			sendRegistrationSms(gmsStudentEntity);
		}
		return GmsStudentEntityConvertor.setDateInGmsStudentEntity(gmsStudentEntity);
	}

	private void sendPaymentSms(GmsStudent gmsStudentEntity) {
		String name = gmsStudentEntity.getName();
		String message = ApplicationConstants.GMS_PAYMENT_SMS.replace("{{gmsRegPayment}}", paymentAmount);
		message = message.replace("{{paymentContactNumber}}",paymentContactNumber);
		SMSUtil.sendSMS(null, name, gmsStudentEntity.getMobile(), message);
		
	}

	private void sendRegistrationSms(GmsStudent gmsStudentEntity) {
		Integer id = gmsStudentEntity.getId();
		String name = gmsStudentEntity.getName();
		String message = ApplicationConstants.GMS_WELCOME_SMS.replace("{{name}}", name);
        message = message.replace("{{studentid}}", String.valueOf(id));
		SMSUtil.sendSMS(id, name, gmsStudentEntity.getMobile(), message);
	}

	/**
	 * Method to update existing gms student data for given studentId
	 * 
	 * @param request
	 */
	public GmsStudent updateStudent(Integer id, GmsStudent request) {
		GmsStudent gmsStudentEntity = getGmsStudentEntity(id);
		gmsStudentEntity = persistStudentGMSEntity(
				GmsStudentEntityConvertor.convertGmsStudentEntity(gmsStudentEntity, request));
		if (Boolean.parseBoolean(sendRegistrationSms)) {
			// send welcome sms to student
			sendRegistrationSms(gmsStudentEntity);
		}
		return GmsStudentEntityConvertor.setDateInGmsStudentEntity(gmsStudentEntity);
	}

	/**
	 * Method to update existing gms student registration status for given studentId
	 * @param id
	 * @param registrationStatus
	 * @return
	 */
	public GmsStudent updateStudentStatus(Integer id, String registrationStatus) {
		GmsStudent gmsStudentEntity = getGmsStudentEntity(id);
		gmsStudentEntity.setRegistrationStatus(registrationStatus);
		gmsStudentEntity.setDateLastModifiedInDB(Calendar.getInstance().getTime());
		gmsStudentEntity = persistStudentGMSEntity(gmsStudentEntity);
		return GmsStudentEntityConvertor.setDateInGmsStudentEntity(gmsStudentEntity);
	}

}
