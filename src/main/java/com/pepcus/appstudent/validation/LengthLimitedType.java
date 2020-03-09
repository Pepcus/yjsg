package com.pepcus.appstudent.validation;

class LengthLimitedType extends ValidationType {

	private int sizeLimited;
	
	public LengthLimitedType(int code) {
		super(code);
	}

	public int getSizeLimited() {
		return sizeLimited;
	}

	public void setSizeLimited(int sizeLimited) {
		this.sizeLimited = sizeLimited;
	}

}
