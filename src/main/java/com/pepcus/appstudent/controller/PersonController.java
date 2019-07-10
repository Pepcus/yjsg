package com.pepcus.appstudent.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pepcus.appstudent.entity.Person;
import com.pepcus.appstudent.service.PersonService;

/**
 * This is a controller for handling/delegating requests to person.
 * 
 * @author
 *
 */
@RestController
@RequestMapping("/v1/events")
public class PersonController {

    @Autowired
    private PersonService personService;

    /**
     * Used to create Person record
     * 
     * @param person
     * @return
     */
    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        Person savedPerson = personService.createPerson(person);
        return new ResponseEntity<Person>(savedPerson, HttpStatus.CREATED);
    }

    /**
     * Used to get person record by personId
     * 
     * @param pathVars
     * @return
     **/
    @GetMapping(value = "/{personId}")
    public ResponseEntity<Person> getPerson(@PathVariable Map<String, String> pathVars) {
        Integer personId = Integer.valueOf(pathVars.get("personId"));
        Person person = personService.getPerson(personId);
        return new ResponseEntity<Person>(person, HttpStatus.OK);
    }

}
