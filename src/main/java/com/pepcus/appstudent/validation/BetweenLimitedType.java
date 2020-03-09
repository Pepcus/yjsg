package com.pepcus.appstudent.validation;

public class BetweenLimitedType extends ValidationType {
	
	private double lowerLimited;

	private double upperLimited;

	public BetweenLimitedType(int code) {
		super(code);
	}

	public double getLowerLimited() {
		return lowerLimited;
	}

	public void setLowerLimited(double lowerLimited) {
		this.lowerLimited = lowerLimited;
	}

	public double getUpperLimited() {
		return upperLimited;
	}

	public void setUpperLimited(double upperLimited) {
		this.upperLimited = upperLimited;
	}

}
