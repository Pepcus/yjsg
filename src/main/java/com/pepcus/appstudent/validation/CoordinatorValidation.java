package com.pepcus.appstudent.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.pepcus.appstudent.entity.Coordinator;
import com.pepcus.appstudent.exception.BadRequestException;
import com.pepcus.appstudent.util.CommonUtil;
import com.pepcus.appstudent.util.ErrorMessageConstants;

public class CoordinatorValidation {

	public static void validateCoordinatorRequest(Coordinator coordinator) {

		if (StringUtils.isEmpty(coordinator.getFirstName())) {
			throw new BadRequestException("firstName " + ErrorMessageConstants.EMPTY_FIELD);
		}
		if (!CommonUtil.isContentAlphabetsOnly(coordinator.getFirstName())) {
			throw new BadRequestException("firstName " + ErrorMessageConstants.INVALID_CHARACTERS);
		}
		if (StringUtils.isEmpty(coordinator.getLastName())) {
			throw new BadRequestException("lastName " + ErrorMessageConstants.EMPTY_FIELD);
		}
		if (!CommonUtil.isContentAlphabetsOnly(coordinator.getLastName())) {
			throw new BadRequestException("lastName " + ErrorMessageConstants.INVALID_CHARACTERS);
		}
		if (StringUtils.isEmpty(coordinator.getGender())) {
			throw new BadRequestException("gender " + ErrorMessageConstants.EMPTY_FIELD);
		}
		if (StringUtils.isEmpty(coordinator.getPrimaryContactNumber())) {
			throw new BadRequestException("primaryContactNumber " + ErrorMessageConstants.EMPTY_FIELD);
		}
		if (!CommonUtil.isValidMobileNumber(coordinator.getPrimaryContactNumber())) {
			throw new BadRequestException(ErrorMessageConstants.INVALID_MOBILE_NUMBER);
		}
		if (StringUtils.isEmpty(coordinator.getDob())) {
			throw new BadRequestException("dob" + ErrorMessageConstants.EMPTY_FIELD);
		}

		if (StringUtils.isEmpty(coordinator.getAddress())) {
			throw new BadRequestException("address " + ErrorMessageConstants.EMPTY_FIELD);
		}
		if (StringUtils.isEmpty(coordinator.getArea())) {
			throw new BadRequestException("area " + ErrorMessageConstants.EMPTY_FIELD);
		}
		if (StringUtils.isNotEmpty(coordinator.getAlternateContactNumber()) && !CommonUtil.isValidMobileNumber(coordinator.getAlternateContactNumber())) {
			throw new BadRequestException(ErrorMessageConstants.INVALID_MOBILE_NUMBER);
		}
		if(StringUtils.isNotEmpty(coordinator.getEmail()) && !CommonUtil.isValidateEmail(coordinator.getEmail())) {
			throw new BadRequestException(ErrorMessageConstants.INVALID_EMAIL);
		}

	}

	public static void validateCoordinatorUpdateRequest(Coordinator coordinator) {
		if (StringUtils.isEmpty(coordinator.getFirstName())) {
			throw new BadRequestException("firstName " + ErrorMessageConstants.EMPTY_FIELD);
		}
		if (!CommonUtil.isContentAlphabetsOnly(coordinator.getFirstName())) {
			throw new BadRequestException("firstName " + ErrorMessageConstants.INVALID_CHARACTERS);
		}
		if (StringUtils.isEmpty(coordinator.getLastName())) {
			throw new BadRequestException("lastName " + ErrorMessageConstants.EMPTY_FIELD);
		}
		if (!CommonUtil.isContentAlphabetsOnly(coordinator.getLastName())) {
			throw new BadRequestException("lastName " + ErrorMessageConstants.INVALID_CHARACTERS);
		}
		if (StringUtils.isEmpty(coordinator.getGender())) {
			throw new BadRequestException("gender " + ErrorMessageConstants.EMPTY_FIELD);
		}
		if (StringUtils.isEmpty(coordinator.getPrimaryContactNumber())) {
			throw new BadRequestException("primaryContactNumber " + ErrorMessageConstants.EMPTY_FIELD);
		}
		if (!CommonUtil.isValidMobileNumber(coordinator.getPrimaryContactNumber())) {
			throw new BadRequestException(ErrorMessageConstants.INVALID_MOBILE_NUMBER);
		}
		if (StringUtils.isEmpty(coordinator.getDob())) {
			throw new BadRequestException("dob" + ErrorMessageConstants.EMPTY_FIELD);
		}

		if (StringUtils.isEmpty(coordinator.getAddress())) {
			throw new BadRequestException("address " + ErrorMessageConstants.EMPTY_FIELD);
		}
		if (StringUtils.isEmpty(coordinator.getArea())) {
			throw new BadRequestException("area " + ErrorMessageConstants.EMPTY_FIELD);
		}
		if (StringUtils.isNotEmpty(coordinator.getAlternateContactNumber()) && !CommonUtil.isValidMobileNumber(coordinator.getAlternateContactNumber())) {
			throw new BadRequestException(ErrorMessageConstants.INVALID_MOBILE_NUMBER);
		}
		if(StringUtils.isNotEmpty(coordinator.getEmail()) && !CommonUtil.isValidateEmail(coordinator.getEmail())) {
			throw new BadRequestException(ErrorMessageConstants.INVALID_EMAIL);
		}

	}

}
