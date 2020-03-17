package com.pepcus.appstudent.entity;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;

@Data
public class GmsStudentWrapper {

	@CsvBindByName
	private String id;

	@CsvBindByName
	private String name;

	@CsvBindByName
	private Integer age;

	@CsvBindByName
	private String mobile;

	@CsvBindByName
	private String city;

	@CsvBindByName
	private String address;

	@CsvBindByName
	private String isWhatsApp;

	@CsvBindByName
	private String registrationStatus;

	@CsvBindByName
	private String paymentStatus;

	@CsvBindByName
	private String foodOpt;


}