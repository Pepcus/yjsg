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
import com.pepcus.appstudent.entity.Coordinator;
import com.pepcus.appstudent.service.CoordinatorService;
import com.pepcus.appstudent.validation.CoordinatorValidation;

@RestController
@RequestMapping("v1/coordinator")
public class CoordinatorController {

	@Autowired
	CoordinatorService coordinatorservice;

	@PostMapping
	public Coordinator createCoordinator(@RequestBody Coordinator coordinator) throws ParseException {
		CoordinatorValidation.validateCoordinatorRequest(coordinator);
		return coordinatorservice.createCoordinator(coordinator);
	}

	@PutMapping("/{id}")
	public Coordinator updateCoordinator(@PathVariable Integer id, @RequestBody Coordinator coordinator)
			throws ParseException {
		CoordinatorValidation.validateCoordinatorUpdateRequest(coordinator);
		return coordinatorservice.updateCoordinator(id, coordinator);
	}

	@GetMapping("/{id}")
	public Coordinator getCoordinator(@PathVariable Integer id) {
		return coordinatorservice.getCoordinator(id);
	}

	@GetMapping
	public List<Coordinator> getCoordinators(@RequestParam(value = "firstName", required = false) String firstName,
			@RequestParam(value = "lastName", required = false) String lastName,
			@RequestParam(value = "primaryContactNumber", required = false) String primaryContactNumber,
			@RequestParam(value = "dob", required = false) String dob) {
		return coordinatorservice.getCoordinators(firstName, lastName, primaryContactNumber, dob);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Coordinator> deleteCoordinator(@PathVariable Integer id) {
		Coordinator deleteCoordinator = coordinatorservice.deleteCoordinator(id);
		return new ResponseEntity<Coordinator>(deleteCoordinator, HttpStatus.ACCEPTED);
	}

}
