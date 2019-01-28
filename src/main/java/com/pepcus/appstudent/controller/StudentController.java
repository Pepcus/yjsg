package com.pepcus.appstudent.controller;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.pepcus.appstudent.response.ApiResponse;
import com.pepcus.appstudent.service.StudentService;
import com.pepcus.appstudent.util.FileImportUtil;
import com.pepcus.appstudent.exception.APIErrorCodes;
import com.pepcus.appstudent.exception.ApplicationException;
import com.pepcus.appstudent.entity.*;

/**
 * This is a controller for handling/delegating requests to service layer.
 * 
 * @author Shubham Solanki
 * @since 12-02-2018
 *
 */
@RestController
@RequestMapping("/v1/students")
public class StudentController {

	@Autowired
	private StudentService studentService;

	/**
	 * Used to fetch student record by studentId
	 * 
	 * @param pathVars
	 * @return
	 */
	@GetMapping(value = "/{studentId}")
	public ResponseEntity<Student> getStudent(@PathVariable Map<String, String> pathVars) {
		Integer studentId = Integer.valueOf(pathVars.get("studentId"));
		Student student = studentService.getStudent(studentId);
		return new ResponseEntity<Student>(student, HttpStatus.OK);
	}

	/**
	 * Used to fetch all students based on specific search criteria.
	 * 
	 * @param pathVars
	 * @return
	 */
	@GetMapping
	public List<Student> getAllStudents(@RequestParam Map<String, String> allRequestParams) {
		return studentService.getAllStudents(allRequestParams);
	}

	/**
	 * Used to create student record
	 * 
	 * @param student
	 * @return
	 */
	@PostMapping
	public ResponseEntity<Student> createStudent(@RequestBody Student student) {
		student.setPrintStatus("Y");
		Student savedStudent = studentService.createStudent(student);
		return new ResponseEntity<Student>(savedStudent, HttpStatus.CREATED);
	}

	/**
	 * Used to update student record by studentId
	 * 
	 * @param studentId
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	@PutMapping(value = "/{studentId}", consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Student> updateStudent(@PathVariable(value = "studentId") Integer studentId,
			@RequestBody String student) throws JsonProcessingException, IOException {
		Student updatedStudent = studentService.updateStudent(student, studentId);
		return new ResponseEntity<Student>(updatedStudent, HttpStatus.OK);
	}

	/**
	 * Used to update attendance record by CSV File
	 * 
	 * @param file
	 * @return response
	 */
	@RequestMapping(value = "/bulk-attendance", method = RequestMethod.PATCH)
	public ResponseEntity<ApiResponse> uploadAttendance(
			@RequestParam(value = "file", required = false) MultipartFile file) {
		ApiResponse response = studentService.updateStudentAttendance(file, "attendance");
		return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
	}

	/**
	 * Used to update Opt In 2019 record by CSV File
	 * 
	 * @param MultipartFile
	 * @return response
	 */
	@RequestMapping(value = "/bulk-optin", method = RequestMethod.PATCH)
	public ResponseEntity<ApiResponse> updateOptIn(@RequestParam(value = "file", required = false) MultipartFile file) {
		ApiResponse response = studentService.updateStudentAttendance(file, "optin");
		return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
	}

	/**
	 * Used to update days attendance
	 * 
	 * @param studentId[]
	 * @param json
	 * @return response
	 */

	@PutMapping(value = "/attendance")
	public ResponseEntity<ApiResponse> updateStudentAttendance(
			@RequestParam(value = "id", required = true) Integer studentId[], @RequestBody String json) {
		ApiResponse response = new ApiResponse();
		JSONObject js = new JSONObject(json);
		System.out.println("get: "+js.get("day"));
		//System.out.println("String: "+js.getString("day"));
		if ((js.get("day").equals(null) || js.getString("day").equals(""))
				|| (js.getInt("day") > 8 && js.getInt("day") < 1)) {
			response.setMessage("Failed..! to update data is not valid");
			response.setStatus("304");
		} else {
			response.setMessage("Updated Successfully");
			response.setStatus("OK");
			studentService.updateStudentAttendance(Arrays.asList(studentId), "Y", js.getInt("day"));
		}
		return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
	}

	/**
	 * Used to update reprint
	 * 
	 * @param studentId[]
	 * @param student
	 * @return
	 */
	@RequestMapping(value = "/reprint", method = RequestMethod.PATCH)
	public ResponseEntity<ApiResponse> updateReprintStatus(
			@RequestParam(value = "id", required = true) Integer studentId[], @RequestBody Student student) {
		ApiResponse response = new ApiResponse();
		if (student.getPrintStatus() == null || (!student.getPrintStatus().equalsIgnoreCase("Y")
				&& !student.getPrintStatus().equalsIgnoreCase("N"))) {
			response.setMessage("Failed..! to update data is not valid");
			response.setStatus("304");
		} else {
			studentService.updateStudentPrintStatus(Arrays.asList(studentId), student);
			response.setMessage("Updated Successfully");
			response.setStatus("OK");
			response.setCode("200");
		}
		return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
	}

	/**
	 * Used to update optIn
	 * 
	 * @param studentId[]
	 * @param student
	 * @return
	 */
	@RequestMapping(value = "/optin", method = RequestMethod.PATCH)
	public ResponseEntity<ApiResponse> updateOptIn(@RequestParam(value = "id", required = true) Integer studentId[],
			@RequestBody Student student) {
		ApiResponse response = new ApiResponse();
		if (student.getOptIn2019() == null
				|| (!student.getOptIn2019().equalsIgnoreCase("Y") && !student.getOptIn2019().equalsIgnoreCase("N"))) {
			response.setMessage("Failed..! to update data is not valid");
			response.setStatus("304");
		} else {
			studentService.updateStudentOptin(Arrays.asList(studentId), student);
			response.setMessage("Updated Successfully");
			response.setStatus("OK");
			response.setCode("200");
		}
		return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
	}
}