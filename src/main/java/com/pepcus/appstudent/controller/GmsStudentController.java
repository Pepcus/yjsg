package com.pepcus.appstudent.controller;

import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RestController;

import com.pepcus.appstudent.entity.GmsStudent;
import com.pepcus.appstudent.service.GmsStudentService;
import com.pepcus.appstudent.validation.GmsStudentValidator;

/**
 * Controller class for Student GMS related services
 * 
 * @author Sandeep Vishwakarma
 * @since 05-03-2020
 *
 */
@RestController
@RequestMapping("/api/gms/students")
public class GmsStudentController {

	@Autowired
	GmsStudentService gmsStudentService;

	/**
	 * Method to return list of all document
	 * 
	 * @return
	 */
	@GetMapping
	public ResponseEntity<List<GmsStudent>> getStudents() {
		List<GmsStudent> gmsStudentList = gmsStudentService.getStudentList();
		return new ResponseEntity<List<GmsStudent>>(gmsStudentList, HttpStatus.OK);
	}

	/**
	 * Used to fetch student record by studentId
	 * 
	 * @param studentId
	 * @return
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<GmsStudent> getStudent(@PathVariable("studentId") Integer id) {
		GmsStudent gmsStudent = gmsStudentService.getStudent(id);
		return new ResponseEntity<GmsStudent>(gmsStudent, HttpStatus.OK);
	}

	/**
	 * Used to fetch student record by phone number
	 * 
	 * @param phoneNumber
	 * @return
	 */
	@GetMapping(value = "/phone/{phone}")
	public ResponseEntity<List<GmsStudent>> getStudents(@PathVariable("phone") String phoneNumber) {
		List<GmsStudent> gmsStudentList = gmsStudentService.getStudentsByMobileNumber(phoneNumber);
		return new ResponseEntity<List<GmsStudent>>(gmsStudentList, HttpStatus.OK);
	}

	/**
	 * Used to create student record
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping
	public ResponseEntity<GmsStudent> createStudent(@RequestBody GmsStudent request) {
		GmsStudentValidator.validateCreateStudentRequest(request);
		GmsStudent gmsStudentEntity = gmsStudentService.createStudent(request);
		return new ResponseEntity<GmsStudent>(gmsStudentEntity, HttpStatus.OK);
	}

	/**
	 * Used to update student record
	 * 
	 * @param id
	 * @param gmsStudent
	 * @return
	 */
	@PutMapping("/{id}")
	public ResponseEntity<GmsStudent> updateStudent(@PathVariable("id") Integer id, @RequestBody GmsStudent request) {
		GmsStudentValidator.validateUpdateStudentRequest(request);
		GmsStudent gmsStudentEntity = gmsStudentService.updateStudent(id, request);
		return new ResponseEntity<GmsStudent>(gmsStudentEntity, HttpStatus.OK);
	}

	/**
	 * Used to update student registration status
	 * @param id
	 * @param requestVariables
	 * @return
	 */
	@PatchMapping("/{id}")
	public ResponseEntity<GmsStudent> updateStudentRegistrationStatus(@PathVariable("id") Integer id,
			@RequestBody Map<String, String> requestVariables) {
		String registrationStatus = requestVariables.get("registrationStatus");
		GmsStudentValidator.validateStudentRegistrationStatus(registrationStatus);
		GmsStudent gmsStudentEntity = gmsStudentService.updateStudentStatus(id, registrationStatus);
		return new ResponseEntity<GmsStudent>(gmsStudentEntity, HttpStatus.OK);
	}

}
