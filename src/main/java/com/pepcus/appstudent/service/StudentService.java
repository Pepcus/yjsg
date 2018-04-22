package com.pepcus.appstudent.service;

import static com.pepcus.appstudent.util.CommonUtil.TOTAL_RECORDS;
import static com.pepcus.appstudent.util.CommonUtil.convertDateToString;
import static com.pepcus.appstudent.util.CommonUtil.setRequestAttribute;
import static com.pepcus.appstudent.util.EntitySearchUtil.getEntitySearchSpecification;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.pepcus.appstudent.entity.Student;
import com.pepcus.appstudent.exception.BadRequestException;
import com.pepcus.appstudent.repository.StudentRepository;
import com.pepcus.appstudent.util.SMSUtil;

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
	
	@PersistenceContext
	private EntityManager em;
	
	@Value("${com.pepcus.appstudent.admin.sendSMS}")
	private boolean isSendSMS;

	/**
	 * Method to get student details
	 * 
	 * @param studentId
	 * @return
	 */
	public Student getStudent(Integer studentId) {
		Student savedStudent = validateStudent(studentId);
		if (null != savedStudent.getDateLastModifiedInDB()) {
			savedStudent.setLastModifiedDate(convertDateToString(savedStudent.getDateLastModifiedInDB()));
		}
		if (null != savedStudent.getDateCreatedInDB()) {
			savedStudent.setCreatedDate(convertDateToString(savedStudent.getDateCreatedInDB()));
		}
		return savedStudent;
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
	@Transactional(propagation = Propagation.REQUIRED)
	public Student createStudent(Student student) {
		Date currentDate = Calendar.getInstance().getTime();
		student.setDateCreatedInDB(currentDate); 
		student.setDateLastModifiedInDB(currentDate);
		
		// Generate secretKey for student
		String secretKey = generateSecretKey();
		student.setSecretKey(secretKey);
				
		Student savedStudent =  studentRepository.save(student);

		savedStudent.setLastModifiedDate(convertDateToString(savedStudent.getDateLastModifiedInDB()));
		savedStudent.setCreatedDate(convertDateToString(savedStudent.getDateCreatedInDB()));
	
		//send SMS only if isSendSMS = true
		if (isSendSMS) {
			SMSUtil.sendSMS(savedStudent);
		}
	
		// This is required otherwise insertable=false field (remark) is not synced with 
        // database when remark field is passed in payload .
        em.flush();
        em.refresh(savedStudent);
        
		return savedStudent;
	}
	


	/**
	 * Method to generate secretKey for student
	 * 
	 * @return
	 */
	public String generateSecretKey() {
		Random random = new Random();
		return String.format("%04d", random.nextInt(10000));
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
		updatedStudent.setDateLastModifiedInDB(currentDate);  
		Student studentInDB = studentRepository.save(updatedStudent);
		
		if (null != studentInDB.getDateCreatedInDB()) {
			studentInDB.setCreatedDate(convertDateToString(studentInDB.getDateCreatedInDB()));
		}
				
		//studentInDB.setDateLastModifiedInDB(new Date());
		studentInDB.setLastModifiedDate(convertDateToString(studentInDB.getDateLastModifiedInDB()));
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

    /**
     * Method to get all students based on specific search criteria 
     * 
     * @param allRequestParams
     * @return
     */
	public List<Student> getAllStudents(Map<String, String> allRequestParams) {
		Specification<Student> spec = getEntitySearchSpecification(allRequestParams, Student.class, new Student());

		//Get and set the total number of records
        setRequestAttribute(TOTAL_RECORDS, studentRepository.count(spec));
        
		List<Student> students = studentRepository.findAll(spec);
		students.forEach(student -> {
			if (null != student.getDateLastModifiedInDB()) {
				student.setLastModifiedDate(convertDateToString(student.getDateLastModifiedInDB()));
			}
			if (null != student.getDateCreatedInDB()) {
				student.setCreatedDate(convertDateToString(student.getDateCreatedInDB()));
			}
		});
		
		return students;
	}
	
}