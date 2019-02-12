package com.pepcus.appstudent.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.pepcus.appstudent.response.ApiResponse;
import com.pepcus.appstudent.service.StudentService;
import com.pepcus.appstudent.util.ApplicationConstants;
import com.pepcus.appstudent.entity.*;
import com.pepcus.appstudent.exception.ApplicationException;

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
	 * @param file
	 * @return response
	 */
	@RequestMapping(value = "/bulk-attendance", method = RequestMethod.PATCH)
	public ResponseEntity<ApiResponse> uploadAttendance(
			@RequestParam(value = "file", required = false) MultipartFile file) {
		ApiResponse response = studentService.updateStudent(file, "attendance");
		return new ResponseEntity<ApiResponse>(response, HttpStatus.MULTI_STATUS);
	}

	/**
	 * Used to update Opt In 2019 record by CSV File
	 * @param MultipartFile
	 * @return response
	 */
	@RequestMapping(value = "/bulk-optin", method = RequestMethod.PATCH)
	public ResponseEntity<ApiResponse> updateOptIn(@RequestParam(value = "file", required = false) MultipartFile file) {
		ApiResponse response = studentService.updateStudent(file, "optin");
		return new ResponseEntity<ApiResponse>(response, HttpStatus.MULTI_STATUS);
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
		if ((js.get("day").equals(null) || js.getString("day").equals(""))
				|| (js.getInt("day") > 8 && js.getInt("day") < 1)) {
			response.setMessage("Failed..! to update data is not valid");
			response.setStatus("304");
		} else {
			response.setMessage("Updated Successfully");
			response.setStatus("OK");
			response=studentService.updateStudentAttendance(new ArrayList<Integer>(Arrays.asList(studentId)), "Y", js.getInt("day"));
		}
		return new ResponseEntity<ApiResponse>(response, HttpStatus.MULTI_STATUS);
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
		response = studentService.updateStudentPrintStatus(new ArrayList<Integer>(Arrays.asList(studentId)), student);
		return new ResponseEntity<ApiResponse>(response, HttpStatus.MULTI_STATUS);
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
		response = studentService.updateStudentOptin(new ArrayList<Integer>(Arrays.asList(studentId)), student);
		return new ResponseEntity<ApiResponse>(response, HttpStatus.MULTI_STATUS);
	}
	
	
	/**
	 * Used to update print status record
	 * which is 'Y' as 'N'
	 * 
	 * @return response
	 */
	@RequestMapping(value = "/reset-reprint", method = RequestMethod.PATCH)
	public ResponseEntity<ApiResponse> resetPrintStatus() {
		ApiResponse response = new ApiResponse();
		studentService.updatePrint();
		response.setCode("200");
		response.setStatus("OK");
		return new ResponseEntity<ApiResponse>(response,HttpStatus.OK);
	}
	
	/**
	 * Used to download duplicate data CSV 
	 * 
	 * @return response
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/download-duplicate", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<InputStreamResource> findDuplicate(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletResponse response){
		File generatedFile = studentService.getDuplicateRecords(file);
		InputStreamResource resource;
		try {
			resource = new InputStreamResource(new FileInputStream(generatedFile));
		} catch (FileNotFoundException e) {
			throw new ApplicationException("Failed to generate duplicate CSV file");
		}
		response.setContentLength((int) generatedFile.length());
		response.setContentType(ApplicationConstants.APPLICATION_FORCEDOWNLOAD);
		response.setHeader(ApplicationConstants.CONTENT_DISPOSITION,
				ApplicationConstants.ATTACHMENT_FILENAME+ApplicationConstants.DOUBLE_QUOTE + generatedFile.getName() + ApplicationConstants.DOUBLE_QUOTE);// fileName);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, ApplicationConstants.ATTACHMENT_FILENAME + generatedFile.getName())
				.contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(generatedFile.length()).body(resource);
	}
}
