package com.pepcus.appstudent.validation;

import static com.pepcus.appstudent.validation.DataValidator.alphabetOnly;
import static com.pepcus.appstudent.validation.DataValidator.email;
import static com.pepcus.appstudent.validation.DataValidator.expect;
import static com.pepcus.appstudent.validation.DataValidator.nonEmpty;
import static com.pepcus.appstudent.validation.DataValidator.phone;
import static com.pepcus.appstudent.validation.DataValidator.validate;
import static com.pepcus.appstudent.validation.DataValidator.validateValues;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pepcus.appstudent.entity.Coordinator;
import com.pepcus.appstudent.entity.CoordinatorAssignedDepartment;
import com.pepcus.appstudent.entity.CoordinatorInterestedDepartment;
import com.pepcus.appstudent.entity.Department;
import com.pepcus.appstudent.exception.BadRequestException;
import com.pepcus.appstudent.service.CoordinatorDepartmentService;
import com.pepcus.appstudent.util.Gender;

@Component
public class CoordinatorValidator {

	@Autowired
	CoordinatorDepartmentService coordinatorDepartmentService;

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
		validate("email", request.getEmail(), expect(email));
		validate("alternateNumber", request.getAlternateNumber(), expect(phone));

		if (CollectionUtils.isNotEmpty(request.getAssignedDepartments())
				|| CollectionUtils.isNotEmpty(request.getInterestedDepartments())) {

			Map<String, Department> departmentMap = coordinatorDepartmentService.getDepartmentMap();

			validateAssignedDepartments(departmentMap, request.getAssignedDepartments());
			validateInterestedDepartments(departmentMap, request.getInterestedDepartments());
		}

	}

	public void validateUpdateCoordinatorRequest(Coordinator request) {
		validate("firstName", request.getFirstName(), expect(nonEmpty, alphabetOnly));
		validate("lastName", request.getLastName(), expect(nonEmpty, alphabetOnly));
		validate("email", request.getEmail(), expect(email));
		validate("whatsappNumber", request.getWhatsappNumber(), expect(phone));
		validate("alternateNumber", request.getAlternateNumber(), expect(phone));
		validateValues("gender", request.getGender(),
				Stream.of(Gender.values()).map(Enum::name).collect(Collectors.toList()));
		validate("dob", request.getDob(), expect(nonEmpty));
		DataValidator.validateDate("dob", request.getDob());
		if (CollectionUtils.isNotEmpty(request.getAssignedDepartments())
				|| CollectionUtils.isNotEmpty(request.getInterestedDepartments())) {

			Map<String, Department> departmentMap = coordinatorDepartmentService.getDepartmentMap();
			validateAssignedDepartments(departmentMap, request.getAssignedDepartments());
			validateInterestedDepartments(departmentMap, request.getInterestedDepartments());
		}
	}

	private static void validateInterestedDepartments(Map<String, Department> departmentMap,
			Collection<CoordinatorInterestedDepartment> interestedDepartments) {
		if (CollectionUtils.isNotEmpty(interestedDepartments)) {
			for (CoordinatorInterestedDepartment interestedDepartment : interestedDepartments) {
				validate("interestedDepartment.internalName", interestedDepartment.getInternalName(), expect(nonEmpty));
				checkDepartmentInternalName(departmentMap, interestedDepartment.getInternalName());
			}
		}
	}

	private static void validateAssignedDepartments(Map<String, Department> departmentMap,
			Collection<CoordinatorAssignedDepartment> assignedDepartments) {
		if (CollectionUtils.isNotEmpty(assignedDepartments)) {
			for (CoordinatorAssignedDepartment assignedDepartment : assignedDepartments) {
				validate("assignedDepartments.internalName", assignedDepartment.getInternalName(), expect(nonEmpty));
				checkDepartmentInternalName(departmentMap, assignedDepartment.getInternalName());
			}
		}
	}

	private static void checkDepartmentInternalName(Map<String, Department> departmentMap, String internalName) {
		if (!departmentMap.containsKey(internalName)) {
			throw new BadRequestException("Invalid value for internalName valid values are " + departmentMap.keySet());
		}
	}
}
