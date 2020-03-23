package com.pepcus.appstudent.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pepcus.appstudent.entity.Department;
import com.pepcus.appstudent.service.DepartmentService;

@RestController
@RequestMapping("v1/departments")
public class DepartmentController {


	@Autowired
	DepartmentService departmentService;

	

	/**
	 * Used to fetch department list
	 * @return
	 */
	@GetMapping
	public ResponseEntity<List<Department>> getDepartments() {
		List<Department> departmentList = departmentService.getDepartments();
		return new ResponseEntity<List<Department>>(departmentList, HttpStatus.OK);
	}

}
