package com.pepcus.appstudent.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.pepcus.appstudent.exception.BadRequestException;
import com.pepcus.appstudent.util.ApplicationConstants;

public class DataValidator {

	// general
	public static final ValidationType any = new ValidationType(0);
	public static final ValidationType general = new ValidationType(1);
	public static final ValidationType notNull = new ValidationType(2);
	public static final ValidationType allNotNull = new ValidationType(3);
	public static final ValidationType nil = new ValidationType(4);
	public static final ValidationType nonEmpty = new ValidationType(5);
	public static final ValidationType noBadChars = new ValidationType(6);
	private static final ValidationType notLongerThan = new ValidationType(7);
	public static final ValidationType email = new ValidationType(8);
	public static final ValidationType phone = new ValidationType(9);
	public static final ValidationType number = new ValidationType(10);
	public static final ValidationType nonZero = new ValidationType(11);
	public static final ValidationType nonNegative = new ValidationType(12);
	public static final ValidationType nonPositive = new ValidationType(13);
	public static final ValidationType integer = new ValidationType(14);
	private static final ValidationType lessThan = new ValidationType(15);
	private static final ValidationType lessThanOrEqual = new ValidationType(16);
	private static final ValidationType moreThan = new ValidationType(17);
	private static final ValidationType moreThanOrEqual = new ValidationType(18);
	public static final ValidationType betweenZeroAndOne = new ValidationType(19);
	public static final ValidationType alphabetOnly = new ValidationType(20);
	public static final ValidationType between = new ValidationType(30);

	public static ValidationType[] expect(ValidationType... codes) {
		return codes;
	}

	public static final String[] except(String... excepts) {
		return excepts;
	}

	public static String validate(String value, ValidationType[] codes) {
		return validate(value, codes, (BadRequestException) null);
	}

	public static String validate(String value, ValidationType[] codes, BadRequestException error) {
		return validate("data", value, codes, error);
	}

	public static String validate(String propName, String value, ValidationType[] codes) {
		return validate(propName, value, codes, (BadRequestException) null);
	}

	public static String validate(String propName, String value, ValidationType[] codes, BadRequestException error) {
		for (ValidationType code : codes) {
			if (nonEmpty.equals(code)) {
				nonEmpty(propName, value, error);
			} else if (notNull.equals(code)) {
				nonNull(propName, value, error);
			} else if (noBadChars.equals(code) || general.equals(code)) {
				noBadChars(propName, value, error);
			}
			if (alphabetOnly.equals(code)) {
				alphabet(propName, value, error);
			} else if (integer.equals(code)) {
				integer(propName, value, error);
			} else if (notLongerThan.equals(code)) {
				noLongerThan(propName, value, ((LengthLimitedType) code).getSizeLimited(), error);
			} else if (email.equals(code)) {
				email(propName, value, error);
			} else if (phone.equals(code)) {
				phoneNumber(propName, value, error);
			} else if (number.equals(code)) {
				number(propName, value, error);
			}
		}
		return value;
	}

	public static Number validate(String propName, Number value, ValidationType[] codes, BadRequestException error) {
		for (ValidationType code : codes) {
			if (nonZero.equals(code)) {
				nonZero(propName, value, error);
			} else if (notNull.equals(code)) {
				nonNull(propName, value, error);
			} else if (nonNegative.equals(code)) {
				nonNegatvie(propName, value, error);
			} else if (nonPositive.equals(code)) {
				nonPostive(propName, value, error);
			} else if (integer.equals(code)) {
				integer(propName, value, error);
			} else if (lessThan.equals(code)) {
				lessThan(propName, value, ((ValueLimitedType) code).getValueLimited(),
						((ValueLimitedType) code).getReference(), error);
			} else if (moreThan.equals(code)) {
				moreThan(propName, value, ((ValueLimitedType) code).getValueLimited(),
						((ValueLimitedType) code).getReference(), error);
			} else if (lessThanOrEqual.equals(code)) {
				lessThanOrEqual(propName, value, ((ValueLimitedType) code).getValueLimited(),
						((ValueLimitedType) code).getReference(), error);
			} else if (moreThanOrEqual.equals(code)) {
				moreThanOrEqual(propName, value, ((ValueLimitedType) code).getValueLimited(),
						((ValueLimitedType) code).getReference(), error);
			} else if (between.equals(code)) {
				between(propName, value, ((BetweenLimitedType) code).getLowerLimited(),
						((BetweenLimitedType) code).getUpperLimited(), error);
			} else if (betweenZeroAndOne.equals(code)) {
				betweenZeroAndOne(propName, value, error);
			}
		}
		return value;
	}

	// numeric validation
	public static Number validate(Number value, ValidationType[] codes) {
		return validate(value, codes, (BadRequestException) null);
	}

	public static Number validate(Number value, ValidationType[] codes, BadRequestException error) {
		return validate("data", value, codes, error);
	}

	public static Number validate(String propName, Number value, ValidationType[] codes) {
		return validate(propName, value, codes, (BadRequestException) null);
	}

	private static void betweenZeroAndOne(String propName, Number value, BadRequestException error) {
		between(propName, value, 0, 1, error);
	}

	public static final ValidationType between(double lower, double upper) {
		BetweenLimitedType type = new BetweenLimitedType(30);
		type.setLowerLimited(lower);
		type.setUpperLimited(upper);
		return type;
	}

	private static void between(String propName, Number value, double lowerLimited, double upperLimited,
			BadRequestException error) {
		if (value != null) {
			if (value.doubleValue() < lowerLimited || value.doubleValue() > upperLimited) {
				if (error == null) {
					throw new BadRequestException(propName + " must be between " + Double.toString(lowerLimited)
							+ " and " + Double.toString(upperLimited));
				} else {
					throw error;
				}
			}
		}
	}

	public static void email(String propName, String value, BadRequestException error) {
		if (value != null) {
			String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
			if (!value.matches(EMAIL_REGEX)) {
				if (error == null) {
					throw new BadRequestException(propName + " is not a valid email address.");
				} else {
					throw error;
				}
			}
			;
		}
	}

	private static void lessThan(String propName, Number value, double limit, String reference,
			BadRequestException error) {
		if (value != null) {
			if (value.doubleValue() >= limit) {
				if (reference == null) {
					reference = Double.toString(limit);
				}
				if (error == null) {
					throw new BadRequestException(propName + " must be less than " + reference);
				} else {
					throw error;
				}
			}
		}
	}

	private static void moreThan(String propName, Number value, double limit, String reference,
			BadRequestException error) {
		if (value != null) {
			if (value.doubleValue() <= limit) {
				if (reference == null) {
					reference = Double.toString(limit);
				}
				if (error == null) {
					throw new BadRequestException(propName + " must be greater than " + reference);
				} else {
					throw error;
				}
			}
		}
	}

	private static void lessThanOrEqual(String propName, Number value, double limit, String reference,
			BadRequestException error) {
		if (value != null) {
			if (value.doubleValue() > limit) {
				if (reference == null) {
					reference = Double.toString(limit);
				}
				if (error == null) {
					throw new BadRequestException(propName + " cannot be greater than " + reference);
				} else {
					throw error;
				}
			}
		}
	}

	private static void moreThanOrEqual(String propName, Number value, double limit, String reference,
			BadRequestException error) {
		if (value != null) {
			if (value.doubleValue() < limit) {
				if (reference == null) {
					reference = Double.toString(limit);
				}
				if (error == null) {
					throw new BadRequestException(propName + "cannot be less than " + reference);
				} else {
					throw error;
				}
			}
		}
	}

	private static void nonZero(String propName, Number value, BadRequestException error) {
		if (value != null) {
			if (value.doubleValue() == 0.0) {
				if (error == null) {
					throw new BadRequestException(propName + " must not be zero.");
				} else {
					throw error;
				}
			}
		}
	}

	private static void nonPostive(String propName, Number value, BadRequestException error) {
		if (value != null) {
			if (value.doubleValue() > 0.0) {
				if (error == null) {
					throw new BadRequestException(propName + " must be non-positive.");
				} else {
					throw error;
				}
			}
		}
	}

	private static void integer(String propName, String svalue, BadRequestException error) {
		if (svalue != null) {
			try {
				integer(propName, Double.parseDouble(svalue), error);
			} catch (NumberFormatException ex) {
				if (error == null) {
					throw new BadRequestException(propName + " must be integer.");
				} else {
					throw error;
				}
			}
		}
	}

	private static void integer(String propName, Number value, BadRequestException error) {
		if (value != null) {
			if (Math.floor(value.doubleValue()) != value.doubleValue()) {
				if (error == null) {
					throw new BadRequestException(propName + " must be integer.");
				} else {
					throw error;
				}
			}
		}
	}

	private static void nonNegatvie(String propName, Number value, BadRequestException error) {
		if (value != null) {
			if (value.doubleValue() < 0.0) {
				if (error == null) {
					throw new BadRequestException(propName + " must be non-negative.");
				} else {
					throw error;
				}
			}
		}
	}

	private static void nonNull(String propName, Object value, BadRequestException error) {
		if (value == null) {
			if (error == null) {
				throw new BadRequestException("Bad or missing data in request: " + propName);
			} else {
				throw error;
			}
		}
	}

	private static void nonEmpty(String propName, String value, BadRequestException error) {
		nonNull(propName, value, error);
		if (value.trim().length() == 0.0) {
			if (error == null) {
				throw new BadRequestException("Bad or missing data in request: " + propName);
			} else {
				throw error;
			}
		}
	}

	private static void noBadChars(String propName, String value, BadRequestException error) {
		if (value != null) {
			String match = containsIllegals(value);
			if (match != null) {
				if (error == null) {
					throw new BadRequestException(propName + " cannot have character " + match);
				} else {
					throw error;
				}
			}
		}
	}

	private static void alphabet(String propName, String value, BadRequestException error) {
		if (value != null) {
			Pattern p = Pattern.compile("^[a-zA-Z\\s]*$");
			Matcher m = p.matcher(value);
			Boolean match = (m.find() && m.group().equals(value));
			if (!match) {
				if (error == null) {
					throw new BadRequestException(propName + " must be alphabets only.");
				} else {
					throw error;
				}
			}
		}

	}

	private static void noLongerThan(String propName, String value, int len, BadRequestException error) {
		if (value != null) {
			if (value.length() > len) {
				if (error == null) {
					throw new BadRequestException(propName + " must not exceed " + len + " characters.");
				} else {
					throw error;
				}
			}
		}
	}

	private static String containsIllegals(String toExamine) {
		Pattern pattern = Pattern.compile("[~#@*+%{}<>\\[\\]|\"\\^;]");
		Matcher matcher = pattern.matcher(toExamine);
		if (matcher.find()) {
			return matcher.group();
		} else {
			return null;
		}
	}

	private static void phoneNumber(String propName, String value, BadRequestException error) {
		if (value != null) {
			if (!value.matches("[- +()0-9]+")) {
				if (error == null) {
					throw new BadRequestException(propName + " is not a valid phone number");
				} else {
					throw error;
				}
			}
		}
	}

	private static void number(String propName, String value, BadRequestException error) {
		if (value != null) {
			try {
				Double.parseDouble(value);
			} catch (NumberFormatException ex) {
				if (error == null) {
					throw new BadRequestException(propName + " is not a number");
				} else {
					throw error;
				}
			}
		}
	}

	public static ValidationType notLongerThan(int len) {
		LengthLimitedType type = new LengthLimitedType(32);
		type.setSizeLimited(len);
		return type;
	}

	public static ValidationType fixedLength(int len) {
		LengthLimitedType type = new LengthLimitedType(33);
		type.setSizeLimited(len);
		return type;
	}

	public static ValidationType lessThan(double value) {
		return lessThanOrEqual(null, value);
	}

	public static ValidationType lessThan(String ref, double value) {
		ValueLimitedType type = new ValueLimitedType(34);
		type.setReference(ref);
		type.setValueLimited(value);
		return type;
	}

	public static ValidationType lessThanOrEqual(double value) {
		return lessThanOrEqual(null, value);
	}

	public static ValidationType lessThanOrEqual(String ref, double value) {
		ValueLimitedType type = new ValueLimitedType(35);
		type.setReference(ref);
		type.setValueLimited(value);
		return type;
	}

	public static ValidationType moreThan(double value) {
		return moreThanOrEqual(null, value);
	}

	public static ValidationType moreThan(String ref, double value) {
		ValueLimitedType type = new ValueLimitedType(36);
		type.setReference(ref);
		type.setValueLimited(value);
		return type;
	}

	public static ValidationType moreThanOrEqual(double value) {
		return moreThanOrEqual(null, value);
	}

	public static ValidationType moreThanOrEqual(String ref, double value) {
		ValueLimitedType type = new ValueLimitedType(37);
		type.setReference(ref);
		type.setValueLimited(value);
		return type;
	}

	public static boolean containsCaseInsensitive(String s, Collection<String> l) {
		return l.stream().anyMatch(x -> x.equalsIgnoreCase(s));
	}

	public static void validateValues(String propName, String value, Collection<String> possibleValues) {
		if (StringUtils.isNotBlank(value)) {
			if (!containsCaseInsensitive(value, possibleValues)) {
				throw new BadRequestException(
						"Invalid value for " + propName + ", valid values are " + possibleValues.toString());
			}
		}
	}

	/**
	 * This method is validate date format 'YYYY-MM-DD' and check valid date
	 * 
	 * @param propName
	 * @param value
	 */
	public static void validateDate(String propName, String value) {
		if ((!validateDateFormat(value)) || !isValidDate(value, ApplicationConstants.DATE_FORMAT_DD_MM_YYYY)) {
			throw new BadRequestException("Invalid value for " + propName + ", Acceptable date formats are "
					+ ApplicationConstants.DATE_FORMAT_DD_MM_YYYY);
		}
	}

	private static boolean validateDateFormat(String date) {
		return (date.matches("\\d{2}/\\d{2}/\\d{4}")) ? true : false;
	}

	private static boolean isValidDate(String dateStr, String format) {
		boolean isValidDate = false;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			simpleDateFormat.setLenient(false);
			simpleDateFormat.parse(dateStr);
			isValidDate = true;
		} catch (ParseException e) {
			isValidDate = false;
		}
		return isValidDate;
	}
}
