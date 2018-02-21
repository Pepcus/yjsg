package com.pepcus.appstudent.service;

import static com.pepcus.appstudent.util.CommonUtil.convertDateToString;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.pepcus.appstudent.entity.Student;
import com.pepcus.appstudent.exception.BadRequestException;
import com.pepcus.appstudent.repository.StudentRepository;

/**
 * This is a service layer which generates response 
 * 
 * @author Shubham Solanki
 * @since 12-02-2018
 *
 */
@Service
public class StudentService {

	@Autowired
	private StudentRepository studentRepository;

	/**
	 * Method to get student details
	 * 
	 * @param studentId
	 * @return
	 */
	public Student getStudent(Integer studentId) {
		Student student = validateStudent(studentId);
		
		// convert created date to string
		String createdDate = convertDateToString(student.getDateCreated());
		
		// Set created date for student
		student.setCreatedDate(createdDate);
		
		// convert last modified date to string
		String lastModifiedDate = convertDateToString(student.getDateLastModified());
		
		// Set last modified date for student
		student.setLastModifiedDate(lastModifiedDate); 
		return student;
	}

	/**
	 * Method used to check whether student exists or not
	 * 
	 * @param studentId
	 * @return
	 */
	private Student validateStudent(Integer studentId) {
		Student student = studentRepository.findOne(studentId);
		if (null == student) {
			throw new BadRequestException("student not found by studentId=" + studentId);
		}
		return student;
	}

	/**
	 * Method to create student record
	 * 
	 * @param student
	 * @return
	 */
	public Student createStudent(Student student) {
		Date currentDate = Calendar.getInstance().getTime();
		student.setDateCreated(currentDate); 
		student.setDateLastModified(currentDate); 
		Student savedStudent =  studentRepository.save(student);
		
		// convert created date to string
		String createdDate = convertDateToString(savedStudent.getDateCreated());
		
		// Set created date for student
		savedStudent.setCreatedDate(createdDate);
		
		// convert last modified date to string
		String lastModifiedDate = convertDateToString(savedStudent.getDateLastModified());
		
		// Set last modified date for student
		savedStudent.setLastModifiedDate(lastModifiedDate);
		
		// Generate secretKey from studentId
		String secretKey = generateSecretKey(savedStudent.getId());
		savedStudent.setSecretKey(secretKey); 
		return studentRepository.save(savedStudent);
	}
	
	/**
	 * Method to generate secretKey by studentId
	 * 
	 * @param studentId
	 * @return
	 */
	public String generateSecretKey(Integer studentId) {
		Hashids hashids = new Hashids("YJSG");
		String secretKey = hashids.encode(studentId);
		return secretKey;
	}

	/**
	 * Method to update student details
	 * 
	 * @param student
	 * @param studentId
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public Student updateStudent(String student, Integer studentId) throws JsonProcessingException, IOException {
		Student std = validateStudent(studentId);
		Student updatedStudent = update(student, std);
		Date currentDate = Calendar.getInstance().getTime();
		updatedStudent.setDateLastModified(currentDate);  
		Student studentInDB = studentRepository.save(updatedStudent);
		
		// convert created date to string
		String createdDate = convertDateToString(studentInDB.getDateCreated());
		
		// Set created date for student
		studentInDB.setCreatedDate(createdDate);
		
		// convert last modified date to string
		String lastModifiedDate = convertDateToString(studentInDB.getDateLastModified());
		
		// Set last modified date for student
		studentInDB.setLastModifiedDate(lastModifiedDate);
		return studentInDB;
	}
	
	/**
     * This function overwrites values from given json string in to given objectToUpdate
     * 
     * @param json
     * @param objectToUpdate
     * @return
     * @throws IOException
     */
    private <T> T update(String json, T objectToUpdate) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDefaultMergeable(true); // This is required for deep update. Available in jackson-databind from 2.9 version
        ObjectReader updater = objectMapper.readerForUpdating(objectToUpdate);

        return updater.readValue(json);
    }
	
}