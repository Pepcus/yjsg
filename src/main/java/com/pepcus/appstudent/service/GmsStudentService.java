package com.pepcus.appstudent.service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.pepcus.appstudent.convertor.GmsStudentEntityConvertor;
import com.pepcus.appstudent.entity.GmsStudent;
import com.pepcus.appstudent.entity.GmsStudentWrapper;
import com.pepcus.appstudent.exception.BadRequestException;
import com.pepcus.appstudent.exception.ResourceNotFoundException;
import com.pepcus.appstudent.repository.GmsStudentRepository;
import com.pepcus.appstudent.response.ApiResponse;
import com.pepcus.appstudent.specifications.GmsStudentSpecification;
import com.pepcus.appstudent.util.ApplicationConstants;
import com.pepcus.appstudent.util.CsvFileUtil;
import com.pepcus.appstudent.util.GmsStudentNullAwareBeanUtil;
import com.pepcus.appstudent.util.SMSUtil;

/**
 * Service class to manage gms student related business operations
 * 
 * @author Sandeep Vishwakarma
 * @since 05-03-2020
 *
 */
@Service
public class GmsStudentService extends GmsStudentServiceHelper{
	
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
	 * Method to get gms student entity list for Id list
	 * @param ids
	 * @return
	 */
	private List<GmsStudent> getStudentEntityList(List<Integer> ids) {
		List<GmsStudent> gmsStudentEntityList = gmsStudentRepository.findByIdIn(ids);
		if (gmsStudentEntityList == null || gmsStudentEntityList.isEmpty()) {
			throw new ResourceNotFoundException("student not found by studentIds=" + ids);
		}
		return gmsStudentEntityList;
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
	public GmsStudent updateStudentRegistrationStatus(Integer id, String registrationStatus) {
		GmsStudent gmsStudentEntity = getGmsStudentEntity(id);
		gmsStudentEntity.setRegistrationStatus(registrationStatus);
		gmsStudentEntity.setDateLastModifiedInDB(Calendar.getInstance().getTime());
		gmsStudentEntity = persistStudentGMSEntity(gmsStudentEntity);
		return GmsStudentEntityConvertor.setDateInGmsStudentEntity(gmsStudentEntity);
	}

	
	/**
	 * Method to update existing gms student payment status for given studentId
	 * @param id
	 * @param paymentStatus
	 * @return
	 */
	public GmsStudent updateStudentPaymentStatus(Integer id, String paymentStatus) {
		GmsStudent gmsStudentEntity = getGmsStudentEntity(id);
		gmsStudentEntity.setPaymentStatus(paymentStatus);
		gmsStudentEntity.setDateLastModifiedInDB(Calendar.getInstance().getTime());
		gmsStudentEntity = persistStudentGMSEntity(gmsStudentEntity);
		if(ApplicationConstants.PAYMENT_STATUS_COMPLETE.equalsIgnoreCase(gmsStudentEntity.getPaymentStatus())){
			// TODO : Send SMS based on flag
			// sendPaymentCompleteSms(gmsStudentEntity);
		}
		return GmsStudentEntityConvertor.setDateInGmsStudentEntity(gmsStudentEntity);
	}

	private void sendPaymentCompleteSms(GmsStudent gmsStudentEntity) {
		String name = gmsStudentEntity.getName();
		String message = ApplicationConstants.GMS_PAYMENT_CNF_SMS;
		SMSUtil.sendSMS(null, name, gmsStudentEntity.getMobile(), message);
		
	}
	
	/**
	 * Method to read csv file and update student record based on Id
	 * @param file
	 * @param flag
	 * @return
	 */
	public ApiResponse updateStudentInBulk(MultipartFile file, String flag) {
		ApiResponse apiResponse = new ApiResponse();
		List<GmsStudentWrapper> uploadedGmsStudentList = CsvFileUtil.convertToCsvBean(file, flag, GmsStudentWrapper.class, null);
		
		List<Integer> studentIdList = getStudentIdsFromFile(uploadedGmsStudentList);
		List<GmsStudent> studentListDB = getStudentEntityList(studentIdList);

		Map<String, List<Integer>> validVsInvalidMap = getInvalidIdsList(studentIdList, studentListDB);

		if (validVsInvalidMap.get(ApplicationConstants.INVALID).size() > 0) {
			throw new BadRequestException(
					"Id's" + validVsInvalidMap.get(ApplicationConstants.INVALID) + " not exist.Failed to processed");
		}
		
		GmsStudentNullAwareBeanUtil onlyNotNullCopyProperty = new GmsStudentNullAwareBeanUtil();
		copyBeanProperty(studentListDB, uploadedGmsStudentList, onlyNotNullCopyProperty);

		// getting inValidIdList whose data is not correct
		Set<Integer> invalidDataIdList = onlyNotNullCopyProperty.getInvalidDataList();
		if (invalidDataIdList.size() > 0) {
			throw new BadRequestException("Id's " + invalidDataIdList + " data is not correct.Failed to processed");
		}
		gmsStudentRepository.save(studentListDB);
		apiResponse = populateUpdateStudentsInBulkResponse(validVsInvalidMap);
		return apiResponse;
	}

}
