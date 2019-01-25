package com.pepcus.appstudent.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.pepcus.appstudent.entity.Student;

import lombok.Data;

/**
 * Class used when API return response
 * 
 * @author Shubham Solanki
 * @since 12-02-2018
 *
 */
@Data
@JsonInclude(Include.NON_EMPTY)
public class ApiResponse {
	
	private String status;
	private String code;
	private Student student;
	private String message;
	private String totalRecords;
	private List<Student> students;
	private String failRecordIds;
}
