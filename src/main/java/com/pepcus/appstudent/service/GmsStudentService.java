package com.pepcus.appstudent.service;

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
	 * @param studentId
	 * @return
	 */
	public GmsStudent getGmsStudentEntity(Integer studentId) {
		GmsStudent gmsStudentEntity = gmsStudentRepository.findOne(studentId);
		if (null == gmsStudentEntity) {
			throw new ResourceNotFoundException("No Student found for id : " + studentId);
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
	 * @param studentId
	 * @return
	 */
	public GmsStudent getStudent(Integer studentId) {
		GmsStudent gmsStudentEntity = getGmsStudentEntity(studentId);
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

		// send welcome sms to student
		if(Boolean.parseBoolean(sendRegistrationSms)){
			SMSUtil.sendGmsStudentWelcomeSMS(gmsStudentEntity.getId(), gmsStudentEntity.getName(),
					gmsStudentEntity.getMobile());	
		}
		return GmsStudentEntityConvertor.setDateInGmsStudentEntity(gmsStudentEntity);
	}

	/**
	 * Method to update existing gms student data
	 * 
	 * @param request
	 */
	public GmsStudent updateStudent(Integer studentId, GmsStudent request) {
		GmsStudent gmsStudentEntity = getGmsStudentEntity(studentId);
		gmsStudentEntity = persistStudentGMSEntity(
				GmsStudentEntityConvertor.convertGmsStudentEntity(gmsStudentEntity, request));
		return GmsStudentEntityConvertor.setDateInGmsStudentEntity(gmsStudentEntity);
	}

}
