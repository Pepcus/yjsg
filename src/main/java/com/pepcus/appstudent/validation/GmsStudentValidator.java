package com.pepcus.appstudent.validation;

import static com.pepcus.appstudent.validation.DataValidator.expect;
import static com.pepcus.appstudent.validation.DataValidator.nonEmpty;
import static com.pepcus.appstudent.validation.DataValidator.nonNegative;
import static com.pepcus.appstudent.validation.DataValidator.nonZero;
import static com.pepcus.appstudent.validation.DataValidator.notNull;
import static com.pepcus.appstudent.validation.DataValidator.phone;
import static com.pepcus.appstudent.validation.DataValidator.email;
import static com.pepcus.appstudent.validation.DataValidator.validate;
import static com.pepcus.appstudent.validation.DataValidator.validateValues;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.pepcus.appstudent.entity.GmsStudent;
import com.pepcus.appstudent.util.ApplicationConstants;


public class GmsStudentValidator {

	public static final String NONE = "0";	
	public static final String DHYAN_SVR = "2";
	public static final String GMS_JIVKAND_SVR = "1";
	public static final String GMS_KARMAND_SVR = "3";
	
	
	/**
	 * Used to validate create student request
	 * 
	 * @param request
	 */
	public static void validateCreateStudentRequest(GmsStudent request) {

		validate("name", request.getName(), expect(nonEmpty));
		validate("mobile", request.getMobile(), expect(nonEmpty, phone));
		if (request.getPaymentStatus() != null
				&& request.getPaymentStatus().equalsIgnoreCase(ApplicationConstants.PAYMENT_STATUS_PENDING)) {
			validate("age", request.getAge(), expect(nonNegative, nonZero));
		} else {
			validate("age", request.getAge(), expect(notNull, nonNegative, nonZero));
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
		
		validate("email", request.getEmail(), expect(email));
		validate("previousShivir", request.getPreviousShivir(), expect(nonEmpty));
		validatePreviousShivirValues(request.getPreviousShivir());
	}

	/**
	 * Used to validate update student request
	 * 
	 * @param request
	 */
	public static void validateUpdateStudentRequest(GmsStudent request) {
		validate("age", request.getAge(), expect(notNull, nonNegative, nonZero));
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

		if(StringUtils.isNotBlank(request.getEmail())){
			validate("email", request.getEmail(), expect(email));
		}
		validatePreviousShivirValues(request.getPreviousShivir());
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

	private static void validatePreviousShivirValues(String previousShivir) {
		if(StringUtils.isNoneEmpty(previousShivir)){
			List<String> previousShivirPossibleValueList = Arrays.asList(GMS_JIVKAND_SVR, GMS_KARMAND_SVR, DHYAN_SVR,
					NONE);
			List<String> previousShivirList = Arrays.asList(previousShivir.split(","));
			for(String previousShivirValue : previousShivirList){
				validateValues("previousShivir", previousShivirValue.trim(), previousShivirPossibleValueList);
			}
		}
	}
	
	
}
