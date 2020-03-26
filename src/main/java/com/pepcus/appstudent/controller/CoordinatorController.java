package com.pepcus.appstudent.controller;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

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

import com.pepcus.appstudent.entity.Coordinator;
import com.pepcus.appstudent.service.CoordinatorService;
import com.pepcus.appstudent.validation.CoordinatorValidator;

@RestController
@RequestMapping("v1/coordinators")
public class CoordinatorController {

	@Autowired
	CoordinatorValidator coordinatorValidator;

	@Autowired
	CoordinatorService coordinatorService;

	/**
	 * Used to create coordinator record
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping
	public ResponseEntity<Coordinator> createCoordinator(@RequestBody Coordinator request) throws ParseException {
		coordinatorValidator.validateCreateCoordinatorRequest(request);
		Coordinator coordinator = coordinatorService.createCoordinator(request);
		return new ResponseEntity<Coordinator>(coordinator, HttpStatus.OK);
	}

	/**
	 * Used to update coordinator record by id
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@PutMapping("/{coordinatorId}")
	public ResponseEntity<Coordinator> updateStudent(@PathVariable("coordinatorId") Integer id, @RequestBody Coordinator request) {
		coordinatorValidator.validateUpdateCoordinatorRequest(request);
		Coordinator coordinator = coordinatorService.updateCoordinator(id, request);
		return new ResponseEntity<Coordinator>(coordinator, HttpStatus.OK);
	}
	
	/**
	 * Used to delete coordinator record  by id
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{coordinatorId}")
	public HttpStatus deleteCoordinator(@PathVariable("coordinatorId") Integer id) {
		return coordinatorService.deleteCoordinator(id) ? HttpStatus.ACCEPTED : HttpStatus.BAD_REQUEST;
	}

	/**
	 * Used to fetch coordinator record by id
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/{coordinatorId}")
	public ResponseEntity<Coordinator> getCoordinator(@PathVariable("coordinatorId") Integer id) {
		Coordinator coordinator = coordinatorService.getCoordinator(id);
		return new ResponseEntity<Coordinator>(coordinator, HttpStatus.OK);
	}

	/**
	 * Used to fetch coordinator list
	 * 
	 * @param firstName
	 * @param lastName
	 * @param whatsappnumber
	 * @param dob
	 * @return
	 */
	@GetMapping
	public ResponseEntity<List<Coordinator>> getCoordinator(@RequestParam Map<String, String> allRequestParams) {
		List<Coordinator> coordinatorList = coordinatorService.getCoordinators(allRequestParams);
		return new ResponseEntity<List<Coordinator>>(coordinatorList, HttpStatus.OK);
	}

}