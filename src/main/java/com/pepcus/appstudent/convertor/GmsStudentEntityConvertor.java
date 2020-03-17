package com.pepcus.appstudent.convertor;

import static com.pepcus.appstudent.util.CommonUtil.convertDateToString;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.pepcus.appstudent.entity.GmsStudent;
import com.pepcus.appstudent.util.ApplicationConstants;

/**
 * Converter class to build/convert student gms entity
 * 
 * @author Sandeep Vishwakarma
 * @since 05-03-2020
 *
 */
public class GmsStudentEntityConvertor {

	public static GmsStudent convertGmsStudentEntity(GmsStudent request) {
		Date currentDate = Calendar.getInstance().getTime();
		request.setDateCreatedInDB(currentDate);
		request.setDateLastModifiedInDB(currentDate);
		if (request.getIsWhatsApp() == null) {
			request.setIsWhatsApp(ApplicationConstants.VAL_FALSE);
		}
		if (request.getPaymentStatus() == null) {
			request.setPaymentStatus(ApplicationConstants.PAYMENT_STATUS_NA);
		}
		return request;
	}

	public static GmsStudent convertGmsStudentEntity(GmsStudent gmsStudentEntity, GmsStudent request) {
		request.setDateLastModifiedInDB(new Date());
		if (Optional.ofNullable(request.getName()).isPresent()) {
			gmsStudentEntity.setName(request.getName());
		}
		if (Optional.ofNullable(request.getAge()).isPresent()) {
			gmsStudentEntity.setAge(request.getAge());
		}
		if (Optional.ofNullable(request.getMobile()).isPresent()) {
			gmsStudentEntity.setMobile(request.getMobile());
		}
		if (Optional.ofNullable(request.getCity()).isPresent()) {
			gmsStudentEntity.setCity(request.getCity());
		}
		if (Optional.ofNullable(request.getAddress()).isPresent()) {
			gmsStudentEntity.setAddress(request.getAddress());
		}
		if (Optional.ofNullable(request.getIsWhatsApp()).isPresent()) {
			gmsStudentEntity.setIsWhatsApp(request.getIsWhatsApp());
		}
		if (Optional.ofNullable(request.getRegistrationStatus()).isPresent()) {
			gmsStudentEntity.setRegistrationStatus(request.getRegistrationStatus());
		}
		if (Optional.ofNullable(request.getPaymentStatus()).isPresent()) {
			gmsStudentEntity.setPaymentStatus(request.getPaymentStatus());
		}
		if (Optional.ofNullable(request.getFoodOpt()).isPresent()) {
			gmsStudentEntity.setFoodOpt(request.getFoodOpt());
		}
		return gmsStudentEntity;
	}

	public static List<GmsStudent> setDateInGmsStudent(List<GmsStudent> students) {
		if (students != null && !students.isEmpty()) {
			for (GmsStudent student : students) {
				setDateInGmsStudent(student);
			}
		}
		return students;
	}

	public static GmsStudent setDateInGmsStudent(GmsStudent student) {
		if (null != student.getDateLastModifiedInDB()) {
			student.setLastModifiedDate(convertDateToString(student.getDateLastModifiedInDB()));
		}
		if (null != student.getDateCreatedInDB()) {
			student.setCreatedDate(convertDateToString(student.getDateCreatedInDB()));
		}
		return student;
	}
}
