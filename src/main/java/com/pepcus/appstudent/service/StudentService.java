package com.pepcus.appstudent.service;

import static com.pepcus.appstudent.util.CommonUtil.TOTAL_RECORDS;
import static com.pepcus.appstudent.util.CommonUtil.convertDateToString;
import static com.pepcus.appstudent.util.CommonUtil.setRequestAttribute;
import static com.pepcus.appstudent.util.EntitySearchUtil.getEntitySearchSpecification;
import static com.pepcus.appstudent.exception.ApplicationException.*;
import static com.pepcus.appstudent.util.ApplicationConstants.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
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
	
	private List<Student> validateListStudent(List<Integer> ids) {
		List<Student> students = studentRepository.findByIdIn(ids);
		if (null == students) {
			throw new BadRequestException("student not found by studentId=" + ids);
		}
		return students;
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

		Student savedStudent = studentRepository.save(student);

		savedStudent.setLastModifiedDate(convertDateToString(savedStudent.getDateLastModifiedInDB()));
		savedStudent.setCreatedDate(convertDateToString(savedStudent.getDateCreatedInDB()));

		// send SMS only if isSendSMS = true
		if (isSendSMS) {
			// SMSUtil.sendSMS(savedStudent);
		}

		// This is required otherwise insertable=false field (remark) is not
		// synced with
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
		// studentInDB.setDateLastModifiedInDB(new Date());
		studentInDB.setLastModifiedDate(convertDateToString(studentInDB.getDateLastModifiedInDB()));
		return studentInDB;
	}

	
	// for updating reprintId in bulk
	public void bulkupdateStudent(List<Integer> studentIds,String reprintid) throws JsonProcessingException, IOException {
		List<Student> stds = validateListStudent(studentIds);
		List<Student> finalList=new ArrayList<Student>();
		Iterator<Student> it=stds.iterator();
		while (it.hasNext()) {
			Student students = (Student) it.next();						
			students.setReprintId(reprintid);
			finalList.add(students);
		}
		studentRepository.save(finalList);
	}
	
	
	public void bulkupdateStudentAttendance(List<Student> student) throws JsonProcessingException, IOException {
		List<Integer> studentIdList = new ArrayList<Integer>();
		for (Student studentObj : student) {
			studentIdList.add((studentObj.getId()));
		}
		
		List<Student> stds = validateListStudent(studentIdList);
		for(Student s : stds){
			BeanPropertyValueEqualsPredicate predicate = new BeanPropertyValueEqualsPredicate("id", s.getId());
			Student s1 = (Student) org.apache.commons.collections.CollectionUtils.find(student, predicate);
			if(s1.getDay1()!=null){
			s.setDay1(s1.getDay1());
			}
			if(s1.getDay2()!=null){
			s.setDay2(s1.getDay2());
			}
			if(s1.getDay3()!=null){
			s.setDay3(s1.getDay3());
			}
			if(s1.getDay4()!=null){
			s.setDay4(s1.getDay4());
			}
			if(s1.getDay5()!=null){
			s.setDay5(s1.getDay5());
			}
			if(s1.getDay6()!=null){
			s.setDay6(s1.getDay6());
			}
			if(s1.getDay7()!=null){
			s.setDay7(s1.getDay7());
			}
			if(s1.getOptIn2019()!=null && !s1.getOptIn2019().isEmpty()){
				s.setOptIn2019(s1.getOptIn2019());
				}
		}
		studentRepository.save(stds);
	}


	/**
	 * This function overwrites values from given json string in to given
	 * objectToUpdate
	 * 
	 * @param json
	 * @param objectToUpdate
	 * @return
	 * @throws IOException
	 */
	private <T> T update(String json, T objectToUpdate) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setDefaultMergeable(true); // This is required for deep
												// update. Available in
												// jackson-databind from 2.9
												// version
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

		// Get and set the total number of records
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

	/*@Transactional(propagation = Propagation.REQUIRED)
	public void uploadStudentAttendance(List<StudentUploadAttendance> studentlist) {

		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		Iterator itr = studentlist.iterator();
		while (itr.hasNext()) {
			StudentUploadAttendance student = (StudentUploadAttendance) itr.next();
			// create update
			CriteriaUpdate<Student> update = cb.createCriteriaUpdate(Student.class);
			// set the root class
			Root<Student> e = update.from(Student.class);
			// set update and where clause
			if(student.getDay1()!=null){
			update.set("day1", student.getDay1());
			}
			if(student.getDay2()!=null){
			update.set("day2", student.getDay2());
			}
			if(student.getDay3()!=null){
			update.set("day3", student.getDay3());
			}
			if(student.getDay4()!=null){
			update.set("day4", student.getDay4());
			}
			if(student.getDay5()!=null){
			update.set("day5", student.getDay5());
			}
			if(student.getDay6()!=null){
			update.set("day6", student.getDay6());
			}
			if(student.getDay7()!=null){
			update.set("day7", student.getDay7());
			}
			update.where(cb.equal(e.get("id"), (Integer.parseInt(student.getId()))));
			// perform update
			this.em.createQuery(update).executeUpdate();
		
		
		}

	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateStudentList(List<Student> li) {
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		Iterator itr = li.iterator();
		while (itr.hasNext()) {
			Student student = (Student) itr.next();
			// create update
			CriteriaUpdate<Student> update = cb.createCriteriaUpdate(Student.class);
			// set the root class
			Root<Student> e = update.from(Student.class);
			// set update and where clause
			update.set("optIn2019", student.getOptIn2019());
			update.where(cb.equal(e.get("id"), student.getId()));
			// perform update
			this.em.createQuery(update).executeUpdate();
		}
	}*/
}