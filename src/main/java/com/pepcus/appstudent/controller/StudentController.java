package com.pepcus.appstudent.controller;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
	
	@Autowired
	private FileImportUtil fileImportService;

	private Logger logger = LoggerFactory.getLogger(StudentController.class);

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
	 * @param pathVars
	 * @param student
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	@PutMapping(value = "/{studentId}",consumes=org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Student> updateStudent(@PathVariable(value="studentId")Integer studentId, Map<String, String> pathVars,
			@RequestBody String student) throws JsonProcessingException, IOException {
		Student updatedStudent = studentService.updateStudent(student, studentId);
		return new ResponseEntity<Student>(updatedStudent, HttpStatus.OK);
	}
	
	/**
	 * Used to update attendance record by CSV File
	 * @param file
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	@RequestMapping(value="/bulk-attendance", method = RequestMethod.PATCH)
	public ResponseEntity<ApiResponse> uploadAttendance(@RequestParam(value = "file", required = true) MultipartFile file){
			fileImportService.uploadStudentAttendance(file);
			return new ResponseEntity<ApiResponse>(HttpStatus.OK);
	}

	/**
	 * Used to update Opt In 2019 record by CSV File
	 * @param file
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	@RequestMapping(value = "/bulk-optin", method = RequestMethod.PATCH)
	public ResponseEntity<ApiResponse> updateOptInViaCSV(@RequestParam(value = "file", required = true) MultipartFile file, HttpServletRequest request) {
			fileImportService.uploadStudentAttendance(file);
		return new ResponseEntity<ApiResponse>(HttpStatus.OK);
	}
		
	/**
	 * Used to update days attendance
	 * @param file
	 * @param pathVars
	 * @param presentabsent
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */

	@PutMapping(value="/attendance") // put studentId in body and remove studentId form url only pass key, pass int in array,set in api response
	public ResponseEntity<Student> updateTodaysStudentAttendance(@RequestParam(value = "id", required = true) Integer studentId[], 
			@RequestBody String json) throws JsonProcessingException, IOException {
		JSONObject js=new JSONObject(json);
		String ispresent="Y";
		studentService.updateStudentTodaysAttendance(Arrays.asList(studentId), ispresent,js.getInt("day"));
		return new ResponseEntity<Student>(HttpStatus.OK);  // set message also success
	}

	/**
	 * Used to update optIn2019 and reprintId
	 * @param studentId[]
	 * @param student
	 * @param pathVars
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	@RequestMapping(value="/reprint",method = RequestMethod.PATCH)
	public ResponseEntity<ApiResponse> updateReprint(@RequestParam(value = "id", required = true) Integer studentId[],@RequestBody Student student) throws JsonProcessingException, IOException  {
		studentService.updateStudentPrintStatus(Arrays.asList(studentId),student);
		return new ResponseEntity<ApiResponse>(HttpStatus.OK);
	}

	@RequestMapping(value="/optin",method = RequestMethod.PATCH)
	public ResponseEntity<ApiResponse> updateOptin(@RequestParam(value = "id", required = true) Integer studentId[],@RequestBody Student student) throws JsonProcessingException, IOException  {
		studentService.updateStudentOptin(Arrays.asList(studentId),student);
		return new ResponseEntity<ApiResponse>(HttpStatus.OK);
	}
}