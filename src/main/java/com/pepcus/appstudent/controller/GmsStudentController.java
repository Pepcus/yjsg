package com.pepcus.appstudent.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.pepcus.appstudent.entity.GmsStudent;
import com.pepcus.appstudent.response.ApiResponse;
import com.pepcus.appstudent.service.GmsStudentService;
import com.pepcus.appstudent.util.ApplicationConstants;
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
	 * Used to create student record
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping
	public ResponseEntity<GmsStudent> createStudent(@RequestBody GmsStudent request) {
		GmsStudentValidator.validateCreateStudentRequest(request);
		GmsStudent gmsStudent = gmsStudentService.createStudent(request);
		return new ResponseEntity<GmsStudent>(gmsStudent, HttpStatus.OK);
	}

	/**
	 * Used to update student record
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@PutMapping("/{id}")
	public ResponseEntity<GmsStudent> updateStudent(@PathVariable("id") Integer id, @RequestBody GmsStudent request) {
		GmsStudentValidator.validateUpdateStudentRequest(request);
		GmsStudent gmsStudent = gmsStudentService.updateStudent(id, request);
		return new ResponseEntity<GmsStudent>(gmsStudent, HttpStatus.OK);
	}

	/**
	 * Used to update student registration status
	 * @param id
	 * @param requestVariables
	 * @return
	 */
	@PatchMapping("/registration-status/{id}")
	public ResponseEntity<GmsStudent> updateStudentRegistrationStatus(@PathVariable("id") Integer id,
			@RequestBody Map<String, String> requestVariables) {
		String registrationStatus = requestVariables.get("registrationStatus");
		GmsStudentValidator.validateStudentRegistrationStatus(registrationStatus);
		GmsStudent gmsStudent = gmsStudentService.updateStudentRegistrationStatus(id, registrationStatus);
		return new ResponseEntity<GmsStudent>(gmsStudent, HttpStatus.OK);
	}
	
	/**
	 * Used to update student registration status
	 * @param id
	 * @param requestVariables
	 * @return
	 */
	@PatchMapping("/payment-status/{id}")
	public ResponseEntity<GmsStudent> updateStudentPaymentStatus(@PathVariable("id") Integer id,
			@RequestBody Map<String, String> requestVariables) {
		String paymentStatus = requestVariables.get("paymentStatus");
		GmsStudentValidator.validateStudentPaymentStatus(paymentStatus);
		GmsStudent gmsStudent = gmsStudentService.updateStudentPaymentStatus(id, paymentStatus);
		return new ResponseEntity<GmsStudent>(gmsStudent, HttpStatus.OK);
	}

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
	public ResponseEntity<GmsStudent> getStudent(@PathVariable("id") Integer id) {
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
	 * Method to export all student data as CSV file
	 * @param response
	 * @throws Exception
	 */
	@GetMapping("/export")
	public void exportStudents(HttpServletResponse response) throws Exception {

		// set file name and content type
		String filename = "students.csv";

		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

		// create a csv writer
		StatefulBeanToCsv<GmsStudent> writer = new StatefulBeanToCsvBuilder<GmsStudent>(response.getWriter())
				.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR)
				.withOrderedResults(true).build();

		// write all users to csv file
		List<GmsStudent> gmsStudentList = gmsStudentService.getStudentList();
		writer.write(gmsStudentList);
	}
	
    /**
     * Used to update students record by CSV File
     * 
     * @param MultipartFile
     * @return response
     */
    @RequestMapping(value = "/upload/db-update", method = RequestMethod.PUT)
    public ResponseEntity<ApiResponse> updateStudent(@RequestParam(value = "file", required = false) MultipartFile file) {
        ApiResponse response = gmsStudentService.updateStudentInBulk(file, ApplicationConstants.BULK_UPDATE);
        return new ResponseEntity<ApiResponse>(response, HttpStatus.MULTI_STATUS);
    }

}
