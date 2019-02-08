package com.pepcus.appstudent.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.pepcus.appstudent.entity.Person;
import com.pepcus.appstudent.entity.Student;
import com.pepcus.appstudent.exception.ApplicationException;
import com.pepcus.appstudent.service.PersonService;

@RestController
@RequestMapping("/v1/events")
public class PersonController {

	@Autowired
	private PersonService personService ;

	/**
	 * 
	 * @param person
	 * @return
	 */
	@PostMapping
	public ResponseEntity<Person> createPerson(@RequestBody Person person) {
		Person savedPerson=null;
		if(person.getMembers()!=null && person.getName()!=null && (person.getMembers()>=0 && person.getMembers()<=6)){
		savedPerson = personService.createPerson(person);
		}else{
			throw new ApplicationException("Failed to register.Please contact 74470-56789");
		}
		return new ResponseEntity<Person>(savedPerson, HttpStatus.CREATED);
	}
	
	@GetMapping(value = "/{personId}")
	public ResponseEntity<Person> getPerson(@PathVariable Map<String, String> pathVars) {
		Integer personId = Integer.valueOf(pathVars.get("personId"));
		Person person = personService.getPerson(personId);
		return new ResponseEntity<Person>(person, HttpStatus.OK);
	}



}
