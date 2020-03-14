package com.pepcus.appstudent.validation;

import static com.pepcus.appstudent.validation.DataValidator.alphabetOnly;
import static com.pepcus.appstudent.validation.DataValidator.between;
import static com.pepcus.appstudent.validation.DataValidator.expect;
import static com.pepcus.appstudent.validation.DataValidator.nonEmpty;
import static com.pepcus.appstudent.validation.DataValidator.nonNegative;
import static com.pepcus.appstudent.validation.DataValidator.notNull;
import static com.pepcus.appstudent.validation.DataValidator.phone;
import static com.pepcus.appstudent.validation.DataValidator.validate;
import static com.pepcus.appstudent.validation.DataValidator.validateValues;

import java.util.Arrays;

import com.pepcus.appstudent.entity.GmsStudent;
import com.pepcus.appstudent.util.ApplicationConstants;

public class GmsStudentValidator {

	/**
	 * Used to validate create student request
	 * 
	 * @param request
	 */
	public static void validateCreateStudentRequest(GmsStudent request) {

		validate("name", request.getName(), expect(nonEmpty, alphabetOnly));
		validate("mobile", request.getMobile(), expect(nonEmpty, phone));
		if (request.getPaymentStatus() != null
				&& request.getPaymentStatus().equalsIgnoreCase(ApplicationConstants.PAYMENT_STATUS_PENDING)) {
			validate("age", request.getAge(), expect(nonNegative, between(0, 100)));
		} else {
			validate("age", request.getAge(), expect(notNull, nonNegative, between(0, 100)));
			validate("city", request.getCity(), expect(nonEmpty));
			validate("isWhatsApp", request.getIsWhatsApp(), expect(nonEmpty));
		}

		validateValues("isWhatsApp", request.getIsWhatsApp(),
				Arrays.asList(ApplicationConstants.VAL_TRUE, ApplicationConstants.VAL_FALSE));

		validateValues("registrationStatus", request.getRegistrationStatus(),
				Arrays.asList(ApplicationConstants.REG_STATUS_REG, ApplicationConstants.REG_STATUS_CNF));

		validateValues("paymentStatus", request.getPaymentStatus(),
				Arrays.asList(ApplicationConstants.PAYMENT_STATUS_COMPLETE, ApplicationConstants.PAYMENT_STATUS_PENDING,
						ApplicationConstants.PAYMENT_STATUS_NA));

		validateValues("foodOpt", request.getFoodOpt(),
				Arrays.asList(ApplicationConstants.VAL_Y, ApplicationConstants.VAL_N));
	}

	/**
	 * Used to validate update student request
	 * 
	 * @param request
	 */
	public static void validateUpdateStudentRequest(GmsStudent request) {
		validate("name", request.getName(), expect(alphabetOnly));
		validate("age", request.getAge(), expect(nonNegative, between(0, 100)));
		validate("mobile", request.getMobile(), expect(phone));

		validateValues("isWhatsApp", request.getIsWhatsApp(),
				Arrays.asList(ApplicationConstants.VAL_TRUE, ApplicationConstants.VAL_FALSE));
		validateValues("registrationStatus", request.getRegistrationStatus(),
				Arrays.asList(ApplicationConstants.REG_STATUS_REG, ApplicationConstants.REG_STATUS_CNF));
		validateValues("paymentStatus", request.getPaymentStatus(),
				Arrays.asList(ApplicationConstants.PAYMENT_STATUS_COMPLETE, ApplicationConstants.PAYMENT_STATUS_PENDING,
						ApplicationConstants.PAYMENT_STATUS_NA));
		validateValues("foodOpt", request.getFoodOpt(),
				Arrays.asList(ApplicationConstants.VAL_Y, ApplicationConstants.VAL_N));

	}

	/**
	 * Used to validate student registration status
	 * 
	 * @param registrationStatus
	 */
	public static void validateStudentRegistrationStatus(String registrationStatus) {
		validate("registrationStatus", registrationStatus, expect(nonEmpty));
		validateValues("registrationStatus", registrationStatus,
				Arrays.asList(ApplicationConstants.REG_STATUS_REG, ApplicationConstants.REG_STATUS_CNF));
	}

	/**
	 * Used to validate student payment status
	 * 
	 * @param registrationStatus
	 */
	public static void validateStudentPaymentStatus(String paymentStatus) {
		validate("paymentStatus", paymentStatus, expect(nonEmpty));
		validateValues("paymentStatus", paymentStatus,
				Arrays.asList(ApplicationConstants.PAYMENT_STATUS_COMPLETE, ApplicationConstants.PAYMENT_STATUS_PENDING,
						ApplicationConstants.PAYMENT_STATUS_NA));
		
	}

}
