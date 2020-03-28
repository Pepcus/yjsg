package com.pepcus.appstudent.validation;

import static com.pepcus.appstudent.validation.DataValidator.alphabetOnly;
import static com.pepcus.appstudent.validation.DataValidator.email;
import static com.pepcus.appstudent.validation.DataValidator.expect;
import static com.pepcus.appstudent.validation.DataValidator.nonEmpty;
import static com.pepcus.appstudent.validation.DataValidator.notNull;
import static com.pepcus.appstudent.validation.DataValidator.phone;
import static com.pepcus.appstudent.validation.DataValidator.validate;
import static com.pepcus.appstudent.validation.DataValidator.validateValues;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pepcus.appstudent.entity.Coordinator;
import com.pepcus.appstudent.entity.CoordinatorAssignedDepartment;
import com.pepcus.appstudent.entity.CoordinatorInterestedDepartment;
import com.pepcus.appstudent.entity.Department;
import com.pepcus.appstudent.entity.DepartmentValue;
import com.pepcus.appstudent.exception.BadRequestException;
import com.pepcus.appstudent.service.DepartmentService;
import com.pepcus.appstudent.util.ApplicationConstants;
import com.pepcus.appstudent.util.Gender;

@Component
public class CoordinatorValidator {

	@Autowired
	DepartmentService departmentService;

	/**
	 * Method to validate create coordinator request
	 * @param request
	 */
	public void validateCreateCoordinatorRequest(Coordinator request) {

		validate("firstName", request.getFirstName(), expect(nonEmpty, alphabetOnly));
		validate("lastName", request.getLastName(), expect(nonEmpty, alphabetOnly));
		validate("gender", request.getGender(), expect(nonEmpty));
		validateValues("gender", request.getGender(),
				Stream.of(Gender.values()).map(Enum::name).collect(Collectors.toList()));
		validate("whatsappNumber", request.getWhatsappNumber(), expect(nonEmpty, phone));
		validate("dob", request.getDob(), expect(nonEmpty));
		DataValidator.validateDate("dob", request.getDob());
		validate("address", request.getAddress(), expect(nonEmpty));
		validate("area", request.getArea(), expect(nonEmpty));
		validateValues("isActive", request.getIsActive(),
				Arrays.asList(ApplicationConstants.VAL_TRUE, ApplicationConstants.VAL_FALSE));
		
		if(StringUtils.isNotBlank(request.getEmail())){
			validate("email", request.getEmail(), expect(email));	
		}
		
		if(StringUtils.isNotBlank(request.getAlternateNumber())){
			validate("alternateNumber", request.getAlternateNumber(), expect(phone));
		}
		
		validateDepartments(request);

	}


	/**
	 * Method to validate update coordinator request
	 * @param request
	 */
	public void validateUpdateCoordinatorRequest(Coordinator request) {
		validate("firstName", request.getFirstName(), expect(nonEmpty, alphabetOnly));
		validate("lastName", request.getLastName(), expect(nonEmpty, alphabetOnly));
		validate("whatsappNumber", request.getWhatsappNumber(), expect(phone));
		validateValues("gender", request.getGender(),
				Stream.of(Gender.values()).map(Enum::name).collect(Collectors.toList()));
		validate("dob", request.getDob(), expect(nonEmpty));
		DataValidator.validateDate("dob", request.getDob());
		validateValues("isActive", request.getIsActive(),
				Arrays.asList(ApplicationConstants.VAL_TRUE, ApplicationConstants.VAL_FALSE));
		
		if(StringUtils.isNotBlank(request.getEmail())){
			validate("email", request.getEmail(), expect(email));	
		}
		
		if(StringUtils.isNotBlank(request.getAlternateNumber())){
			validate("alternateNumber", request.getAlternateNumber(), expect(phone));
		}
		
		validateDepartments(request);
	}

	/**
	 * Method to validate coordinate departments & their values
	 * @param request
	 */
	private void validateDepartments(Coordinator request) {
		if (CollectionUtils.isNotEmpty(request.getAssignedDepartments())
				|| CollectionUtils.isNotEmpty(request.getInterestedDepartments())) {
			Map<Integer, Department> departmentMap = departmentService.getDepartmentMap();
			validateAssignedDepartments(departmentMap, request.getAssignedDepartments());
			validateInterestedDepartments(departmentMap, request.getInterestedDepartments());
		}
	}
	
	/**
	 * Method to validate coordinator interested departments
	 * @param departmentMap
	 * @param interestedDepartments
	 */
	private static void validateInterestedDepartments(Map<Integer, Department> departmentMap,
			Collection<CoordinatorInterestedDepartment> interestedDepartments) {

		if (CollectionUtils.isEmpty(interestedDepartments)) {
			return;
		}

		for (CoordinatorInterestedDepartment interestedDepartment : interestedDepartments) {
			validate("interestedDepartment.id", interestedDepartment.getId(), expect(notNull));
			checkDepartmentId(departmentMap, interestedDepartment.getId());
		}
	}

	/**
	 * Method to validate coordinate assigned departments & their values
	 * @param departmentMap
	 * @param assignedDepartments
	 */
	private static void validateAssignedDepartments(Map<Integer, Department> departmentMap,
			Collection<CoordinatorAssignedDepartment> assignedDepartments) {

		if (CollectionUtils.isEmpty(assignedDepartments)) {
			return;
		}

		for (CoordinatorAssignedDepartment assignedDepartment : assignedDepartments) {
			validate("assignedDepartments.id", assignedDepartment.getId(), expect(notNull));
			Integer requestedDepartmentId = assignedDepartment.getId();
			checkDepartmentId(departmentMap, requestedDepartmentId);
			validateAssignedDepartmentValues(departmentMap, assignedDepartment);
		}
	}

	/**
	 * Method to validate coordinator assigned department department-values
	 * @param departmentMap
	 * @param assignedDepartment
	 */
	private static void validateAssignedDepartmentValues(Map<Integer, Department> departmentMap,
			CoordinatorAssignedDepartment assignedDepartment) {

		if (CollectionUtils.isEmpty(assignedDepartment.getDepartmentValues())) {
			return;
		}

		Integer requestedDepartmentId = assignedDepartment.getId();
		Department requestedDepartment = departmentMap.get(requestedDepartmentId);
		Map<Integer, DepartmentValue> departmentValueMap = DepartmentService.getDepartmentValueMap(requestedDepartment);
		for (DepartmentValue departmentValue : assignedDepartment.getDepartmentValues()) {
			Integer requestedDepartmentValueId = departmentValue.getId();
			validate("assignedDepartments.departmentValues.id", requestedDepartmentValueId, expect(notNull));
			checkDepartmentValueId(requestedDepartmentId, requestedDepartmentValueId, departmentValueMap);
		}
	}

	/**
	 * Method to validate department id
	 * @param departmentMap
	 * @param departmentId
	 */
	private static void checkDepartmentId(Map<Integer, Department> departmentMap, Integer departmentId) {
		if (!departmentMap.containsKey(departmentId)) {
			throw new BadRequestException("Invalid value for department id valid values are " + departmentMap.keySet());
		}
	}

	/**
	 * Method to validate department value id
	 * @param departmentId
	 * @param departmentValueId
	 * @param departmentValueMap
	 */
	private static void checkDepartmentValueId(Integer departmentId, Integer departmentValueId,
			Map<Integer, DepartmentValue> departmentValueMap) {
		if (!departmentValueMap.containsKey(departmentValueId)) {
			throw new BadRequestException("Invalid department value id for department id '" + departmentId
					+ "' valid department values ids are " + departmentValueMap.keySet());
		}
	}

}
