package com.pepcus.appstudent.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static com.pepcus.appstudent.util.APItestDataUtil.getPersonData;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.pepcus.appstudent.entity.Person;
import com.pepcus.appstudent.exception.BadRequestException;
import com.pepcus.appstudent.repository.PersonRepository;

/**
 * class use for Junit Test of Person service
 * 
 * @author Rahul.Panwar
 * @since 2019-07-09
 */
@RunWith(MockitoJUnitRunner.class)
public class PersonServiceTest {
    
    @InjectMocks
    PersonService personService;
    
    @Mock
    PersonRepository personRepository;

    /**
     * Test getPerson when personid is not exist in db
     */
    @Test(expected = BadRequestException.class)
    public void getPersonWithInvalidPersonIdTest() {
        Integer personid = 1;
        when(personRepository.findOne(personid)).thenReturn(null);
        
        personService.getPerson(personid);
    }

    /**
     * Test getPerson when personid is exist in db
     */
    @Test
    public void getPersonWithValidPersonIdTest() {
        Integer personid = 1;
        when(personRepository.findOne(personid)).thenReturn(getPersonData());
        
        Person person = personService.getPerson(personid);
        
        assertEquals(1, person.getId().intValue());
        assertEquals("Ram", person.getName());
    }
    
    /**
     * Test createPerson with valid data
     */
    @Test
    public void createPersonTest() {
        when(personRepository.save(Mockito.any(Person.class))).thenReturn(getPersonData());
        Person person = personService.createPerson(getPersonData());
        
        assertEquals(1, person.getId().intValue());
        assertEquals("Ram", person.getName());
        
    }
}
