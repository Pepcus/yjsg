package com.pepcus.appstudent.validation;

class ValueLimitedType extends ValidationType {

	private double valueLimited;
	
	private String reference;
	
	public ValueLimitedType(int code) {
		super(code);
	}

	public double getValueLimited() {
		return valueLimited;
	}

	public void setValueLimited(double valueLimited) {
		this.valueLimited = valueLimited;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
	
}
