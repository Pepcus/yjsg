package com.pepcus.appstudent.service;

import static com.pepcus.appstudent.util.CommonUtil.convertDateToString;

import java.util.Calendar;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pepcus.appstudent.entity.Person;
import com.pepcus.appstudent.entity.Student;
import com.pepcus.appstudent.exception.BadRequestException;
import com.pepcus.appstudent.repository.PersonRepository;

@Service
public class PersonService {

	
	@Autowired
	private PersonRepository personRepository;
		
	@PersistenceContext
	private EntityManager em;

	
	/**
	 * Method to create person record
	 * 
	 * @param person
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Person createPerson(Person person) {
		Date currentDate = Calendar.getInstance().getTime();
		person.setDateCreatedInDB(currentDate);
		person.setDateLastModifiedInDB(currentDate);

		Person savedPerson = personRepository.save(person);

		savedPerson.setLastModifiedDate(convertDateToString(savedPerson.getDateLastModifiedInDB()));
		savedPerson.setCreatedDate(convertDateToString(savedPerson.getDateCreatedInDB()));
		em.flush();
		em.refresh(savedPerson);

		return person;
	}
	

	public Person getPerson(Integer studentId) {
		Person savedPerson = validatePerson(studentId);
		if (null != savedPerson.getDateLastModifiedInDB()) {
			savedPerson.setLastModifiedDate(convertDateToString(savedPerson.getDateLastModifiedInDB()));
		}
		if (null != savedPerson.getDateCreatedInDB()) {
			savedPerson.setCreatedDate(convertDateToString(savedPerson.getDateCreatedInDB()));
		}
		return savedPerson;
	}
	
	private Person validatePerson(Integer personid) {
		Person person = personRepository.findOne(personid);
		if (null == person) {
			throw new BadRequestException("Person not found by personid=" + personid);
		}
		return person;
	}
}
