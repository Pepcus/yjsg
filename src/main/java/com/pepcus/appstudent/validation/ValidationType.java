package com.pepcus.appstudent.validation;

public class ValidationType {
	private int code;

	public ValidationType(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}

	public boolean equals(ValidationType type) {
		return this.code == type.code;
	}
}
