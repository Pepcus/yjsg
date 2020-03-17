package com.pepcus.appstudent.validation;

import static com.pepcus.appstudent.validation.DataValidator.alphabetOnly;
import static com.pepcus.appstudent.validation.DataValidator.email;
import static com.pepcus.appstudent.validation.DataValidator.expect;
import static com.pepcus.appstudent.validation.DataValidator.nonEmpty;
import static com.pepcus.appstudent.validation.DataValidator.phone;
import static com.pepcus.appstudent.validation.DataValidator.validate;
import static com.pepcus.appstudent.validation.DataValidator.validateValues;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.pepcus.appstudent.entity.Coordinator;
import com.pepcus.appstudent.util.ApplicationConstants;

public class CoordinatorValidation {

	public static void validateCreateCoordinatorRequest(Coordinator request) {

		validate("firstName", request.getFirstName(), expect(nonEmpty, alphabetOnly));
		validate("lastName", request.getLastName(), expect(nonEmpty, alphabetOnly));
		validate("gender", request.getGender(), expect(nonEmpty));
		validateValues("gender", request.getGender(),
				Arrays.asList(ApplicationConstants.GENDER_MALE, ApplicationConstants.GENDER_FEMALE));
		validate("primaryContactNumber", request.getPrimaryContactNumber(), expect(nonEmpty, phone));
		validate("dob", request.getDob(), expect(nonEmpty));
		DataValidator.validateDate("dob", request.getDob());

		validate("address", request.getAddress(), expect(nonEmpty));
		validate("area", request.getArea(), expect(nonEmpty));
		validate("email", request.getEmail(), expect(nonEmpty, email));
		validate("alternateContactNumber", request.getAlternateContactNumber(), expect(phone));

		if(StringUtils.isEmpty(request.getAlternateContactNumber())) {
			request.setAlternateContactNumber(null);
		}
		if(StringUtils.isEmpty(request.getRemarks())) {
			request.setRemarks(null);
		}

	}

	public static void validateUpdateCoordinatorRequest(Coordinator request) {
		
		validate("firstName", request.getFirstName(), expect(alphabetOnly));
		validate("lastName", request.getLastName(), expect(alphabetOnly));
		validate("email", request.getEmail(), expect(email));
		validate("primaryContactNumber", request.getPrimaryContactNumber(), expect(phone));
		validate("alternateContactNumber", request.getAlternateContactNumber(), expect(phone));
		validateValues("gender", request.getGender(),
				Arrays.asList(ApplicationConstants.GENDER_MALE, ApplicationConstants.GENDER_FEMALE));
		
		if (StringUtils.isNotEmpty(request.getDob())) {
			DataValidator.validateDate("dob", request.getDob());
		}
	}

	public static void validateGetCoordinatorRequest(String firstName, String lastName, String primaryContactNumber,
			String dob) {
		validate("firstName", firstName, expect(alphabetOnly));
		validate("lastName", lastName, expect(alphabetOnly));
		validate("primaryContactNumber", primaryContactNumber, expect(phone));
		if (StringUtils.isNotEmpty(dob)) {
			DataValidator.validateDate("dob", dob);
		}
	}

}
