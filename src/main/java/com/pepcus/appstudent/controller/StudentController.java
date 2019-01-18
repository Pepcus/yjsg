package com.pepcus.appstudent.controller;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.pepcus.appstudent.response.ApiResponse;
import com.pepcus.appstudent.service.FileImportService;
import com.pepcus.appstudent.service.StudentService;
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
	private FileImportService fileImportService;

	private Logger logger = LoggerFactory.getLogger(StudentController.class);

	/**
	 * Used to fetch student record by studentId
	 * 
	 * @param pathVars
	 * @return
	 */
	@GetMapping(value = "/{studentId}/{secretKey}")
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
	@GetMapping(value = "/{secretKey}")
	public List<Student> getAllStudents(@PathVariable Map<String, String> pathVars,
			@RequestParam Map<String, String> allRequestParams) {
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
		System.out.println("create calledddddd");
		student.setReprint_id("1");
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
	@PutMapping(value = "/{studentId}/{secretKey}")
	public ResponseEntity<Student> updateStudent(@PathVariable(value="studentId")Integer studentId, Map<String, String> pathVars,
			@RequestBody String student) throws JsonProcessingException, IOException {
		
		//studentId = Integer.valueOf(pathVars.get("studentId"));
		Student updatedStudent = studentService.updateStudent(student, studentId);
		return new ResponseEntity<Student>(updatedStudent, HttpStatus.OK);
	}
	
	
	@PutMapping(value = "/bulkupdate/{studentId}/{secretKey}")
	public ResponseEntity<Student> updateStudentReprint(
			@PathVariable(value = "studentId", required = true) String studentId[], @RequestParam String reprintId,
			Map<String, String> pathVars) throws JsonProcessingException, IOException {

		List<Integer> studentIdList = new ArrayList<Integer>();
		for (String studId : studentId) {
			studentIdList.add(Integer.parseInt(studId));
		}
		studentService.bulkupdateStudent(studentIdList, reprintId);
		return new ResponseEntity<Student>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/uploadattendence", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> uploadFile(@RequestParam(value = "file", required = true) MultipartFile file,
			HttpServletRequest request) throws IOException, IllegalAccessException {
		try {
			fileImportService.uploadStudentAttendance(file, request);
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<ApiResponse>(HttpStatus.OK);
	}

	@RequestMapping(value = "/update_opt_in_2019/uploadfile", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> updateUploadFile(
			@RequestParam(value = "file", required = true) MultipartFile file, HttpServletRequest request)throws IOException, IllegalAccessException, InvocationTargetException {
		fileImportService.uploadStudentAttendance(file, request);
		return new ResponseEntity<ApiResponse>(HttpStatus.OK);
	}

}
