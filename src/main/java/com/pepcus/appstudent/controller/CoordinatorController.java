package com.pepcus.appstudent.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pepcus.appstudent.entity.Coordinator;
import com.pepcus.appstudent.entity.GmsStudent;
import com.pepcus.appstudent.response.ApiResponse;
import com.pepcus.appstudent.service.CoordinatorService;
import com.pepcus.appstudent.validation.CoordinatorValidation;

@RestController
@RequestMapping("v1/coordinator")
public class CoordinatorController {

	@Autowired
	CoordinatorService coordinatorservice;

	/**
	 * Used to create coordinator record
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping
	public ResponseEntity<Coordinator> createCoordinator(@RequestBody Coordinator request) throws ParseException {
		CoordinatorValidation.validateCreateCoordinatorRequest(request);
		Coordinator coordinator = coordinatorservice.createCoordinator(request);
		return new ResponseEntity<Coordinator>(coordinator, HttpStatus.OK);
	}

	/**
	 * Used to update existing coordinator record
	 * @param id
	 * @param request
	 * @return
	 * @throws ParseException
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Coordinator> updateCoordinator(@PathVariable Integer id, @RequestBody Coordinator request)
			throws ParseException {
		CoordinatorValidation.validateUpdateCoordinatorRequest(request);
		Coordinator coordinator = coordinatorservice.updateCoordinator(id, request);
		return new ResponseEntity<Coordinator>(coordinator, HttpStatus.OK);
	}

	/**
	 * Used to fetch coordinator record by id
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Coordinator> getCoordinator(@PathVariable Integer id) {
		Coordinator coordinator = coordinatorservice.getCoordinator(id);
		return new ResponseEntity<Coordinator>(coordinator, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<Coordinator>> getCoordinators(@RequestParam(value = "firstName", required = false) String firstName,
			@RequestParam(value = "lastName", required = false) String lastName,
			@RequestParam(value = "primaryContactNumber", required = false) String primaryContactNumber,
			@RequestParam(value = "dob", required = false) String dob) {
		CoordinatorValidation.validateGetCoordinatorRequest(firstName, lastName, primaryContactNumber, dob);
		List<Coordinator> coordinatorList = coordinatorservice.getCoordinators(firstName, lastName, primaryContactNumber, dob);
		return new ResponseEntity<List<Coordinator>>(coordinatorList, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public HttpStatus deleteCoordinator(@PathVariable Integer id) {
		return coordinatorservice.deleteCoordinator(id) ? HttpStatus.ACCEPTED : HttpStatus.BAD_REQUEST;
	}
	
	@PostMapping("/bulk-upload")
	public ResponseEntity<ApiResponse> uploadCoordinators(
			@RequestParam(value = "file", required = true) MultipartFile file) throws ParseException {
		return new ResponseEntity<ApiResponse>(coordinatorservice.uploadCoordinators(file), HttpStatus.CREATED);
	}

}
