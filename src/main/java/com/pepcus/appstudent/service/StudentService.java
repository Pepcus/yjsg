package com.pepcus.appstudent.service;

import static com.pepcus.appstudent.util.CommonUtil.TOTAL_RECORDS;
import static com.pepcus.appstudent.util.CommonUtil.convertDateToString;
import static com.pepcus.appstudent.util.CommonUtil.setRequestAttribute;
import static com.pepcus.appstudent.util.EntitySearchUtil.getEntitySearchSpecification;
import static com.pepcus.appstudent.exception.ApplicationException.*;
import static com.pepcus.appstudent.util.ApplicationConstants.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
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
import org.apache.commons.collections.CollectionUtils;
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
import com.pepcus.appstudent.entity.StudentUploadAttendance;
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

	
	/**
	 * Method to update student reprintid
	 * 
	 * @param studentIds
	 * @param reprintid
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public void updateStudentOptin(List<Integer> studentIds,Student student) throws JsonProcessingException, IOException {
		List<Student> studentList = validateListStudent(studentIds);
		List<Student> studentUpdatedList=new ArrayList<Student>();
		Iterator<Student> iterator=studentList.iterator();
		while (iterator.hasNext()) {
			Student students = (Student) iterator.next();
				students.setOptIn2019(student.getOptIn2019());
			studentUpdatedList.add(students);
		}
		studentRepository.save(studentUpdatedList);
	}
	
	
	public void updateStudentPrintStatus(List<Integer> studentIds,Student student) throws JsonProcessingException, IOException {
		List<Student> studentList = validateListStudent(studentIds);
		List<Student> studentUpdatedList=new ArrayList<Student>();
		Iterator<Student> iterator=studentList.iterator();
		while (iterator.hasNext()) {
			Student students = (Student) iterator.next();
				students.setPrintStatus(student.getPrintStatus());
			studentUpdatedList.add(students);
		}
		studentRepository.save(studentUpdatedList);
	}
	
	
	
	public void updateStudentTodaysAttendance(List<Integer> studentIds,String ispresent,int day) throws JsonProcessingException, IOException {
		List<Student> studentdList = validateListStudent(studentIds);
		List<Student> updatedStudentList=new ArrayList<Student>();
		//int differenceInDays=0;
		//differenceInDays=getDayDifference();
		//differenceInDays++;
		Iterator<Student> it=studentdList.iterator();
		while (it.hasNext()) {
			Student students = (Student) it.next();						
			switch (day) {
			case 1: students.setDay1(ispresent);
			break;
			case 2: students.setDay2(ispresent);
			break;
			case 3: students.setDay3(ispresent);
			break;
			case 4: students.setDay4(ispresent);
			break;
			case 5: students.setDay5(ispresent);
			break;
			case 6: students.setDay6(ispresent);
			break;
			case 7: students.setDay7(ispresent);
			break;
			}
			
			updatedStudentList.add(students);
		}
		studentRepository.save(updatedStudentList);
	}
	
	
	/**
	 * Method to update student atttendance in day1,day2..
	 * 
	 * @param List Of Students to be update from CSV
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void updateStudentAttendance(List<StudentUploadAttendance> studentUploadAttendanceList) throws JsonProcessingException, IOException, IllegalAccessException, InvocationTargetException {

		NullAwareBeanUtilsBean onlyNotNullCopyProperty = new NullAwareBeanUtilsBean();
		List<Integer> studentIdList = new ArrayList<Integer>();
		for (StudentUploadAttendance stuAttendance : studentUploadAttendanceList) {
			studentIdList.add(Integer.parseInt(stuAttendance.getId()));
		}
		List<Student> studentListDB = validateListStudent(studentIdList);
		for (Student studentDB : studentListDB) {
			BeanPropertyValueEqualsPredicate predicate = new BeanPropertyValueEqualsPredicate("id",String.valueOf(studentDB.getId()));
			StudentUploadAttendance stuAttendance = (StudentUploadAttendance) CollectionUtils.find(studentUploadAttendanceList, predicate);
			onlyNotNullCopyProperty.copyProperties(studentDB, stuAttendance);
		}
		studentRepository.save(studentListDB);
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

	
	/**
	 * Method to get difference in between today's date and day1 date
	 * @return differenceDays
	 */
	public static int getDayDifference() {
		String Day1="14/01/2019";
		int differenceDays = 0;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date day1DateInDateType,todaysDateInDateType;
		String todaysDate = dateFormat.format(new Date());
		try {
			day1DateInDateType = dateFormat.parse(Day1);
			todaysDateInDateType = dateFormat.parse(todaysDate);
			int diff = (int)todaysDateInDateType.getTime() - (int)day1DateInDateType.getTime();
			differenceDays = diff / (24 * 60 * 60 * 1000);
			System.out.print(differenceDays + " days ");
			if(differenceDays>=7 || differenceDays<0){
				differenceDays=0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return differenceDays;
	}
}