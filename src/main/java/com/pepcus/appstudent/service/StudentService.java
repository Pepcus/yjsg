package com.pepcus.appstudent.service;

import static com.pepcus.appstudent.util.CommonUtil.TOTAL_RECORDS;
import static com.pepcus.appstudent.util.CommonUtil.convertDateToString;
import static com.pepcus.appstudent.util.CommonUtil.setRequestAttribute;
import static com.pepcus.appstudent.util.EntitySearchUtil.getEntitySearchSpecification;
import static com.pepcus.appstudent.exception.ApplicationException.*;
import static com.pepcus.appstudent.util.ApplicationConstants.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.pepcus.appstudent.entity.Student;
import com.pepcus.appstudent.entity.StudentUploadAttendance;
import com.pepcus.appstudent.exception.APIErrorCodes;
import com.pepcus.appstudent.exception.ApplicationException;
import com.pepcus.appstudent.exception.BadRequestException;
import com.pepcus.appstudent.repository.StudentRepository;
import com.pepcus.appstudent.response.ApiResponse;
import com.pepcus.appstudent.util.FileImportUtil;

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
		//List<Integer> ids2 = students.stream().map(std -> std.getId()).collect(Collectors.toList());
		
		//ids.remove(ids2);
		//if (!org.springframework.util.CollectionUtils.isEmpty(ids2)) {
			if (students==null || students.isEmpty()) {
			throw new BadRequestException("student not found by studentId=" + students);
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
	 * Method to update student OptIn
	 * @param studentIds
	 * @param student
	 * @return
	 */
	public ApiResponse updateStudentOptin(List<Integer> studentIds, Student student) {
		ApiResponse response = null;
		
		if (student.getOptIn2019()!=null && (student.getOptIn2019().equalsIgnoreCase("Y") || student.getOptIn2019().equalsIgnoreCase("N"))) {
			
			List<Student> studentList = validateListStudent(studentIds);
			Map<String, List<Integer>> validVsInvalidMap = getInvalidIdsList(studentIds, studentList);
			response = populateResponse(validVsInvalidMap);
			
			ListIterator<Student> iterator = studentList.listIterator();
			while (iterator.hasNext()) {
				Student students = (Student) iterator.next();
				students.setOptIn2019(student.getOptIn2019());
				iterator.add(students);
			}
			studentRepository.save(studentList);
			
		}else{
			throw new ApplicationException("Invalid data");
		}
		return response; 
	}

	
	private ApiResponse populateResponse(Map<String, List<Integer>> validInvalidIdsMap) {
		ApiResponse response = new ApiResponse();
		if (!validInvalidIdsMap.get("invalid").isEmpty()) {
			response.setCode("207");
			response.setFailRecordIds(String.valueOf(validInvalidIdsMap.get("invalid")));
			response.setSuccessRecordsIds(String.valueOf(validInvalidIdsMap.get("valid")));
			response.setMessage("Some records failed and some updated");
		}
		else{
			response.setCode("200");
			response.setSuccessRecordsIds(String.valueOf(validInvalidIdsMap.get("valid")));
			response.setMessage("Updated Successfully");
		}
		return response;
	}

	

	/**
	 * Method to update student Print Status
	 * @param studentIds
	 * @param student
	 * @return
	 */
	public ApiResponse updateStudentPrintStatus(List<Integer> studentIds, Student student){
		ApiResponse response=new ApiResponse();
		if (student.getPrintStatus()!=null && student.getPrintStatus().equalsIgnoreCase("Y") || student.getPrintStatus().equalsIgnoreCase("N")) {
			List<Student> studentList = validateListStudent(studentIds);
			
			Map<String, List<Integer>> validVsInvalidMap = getInvalidIdsList(studentIds, studentList);
			response = populateResponse(validVsInvalidMap);
			
			ListIterator<Student> iterator = studentList.listIterator();
			while (iterator.hasNext()) {
				Student students = (Student) iterator.next();
				students.setPrintStatus(student.getPrintStatus());
				iterator.add(students);
			}
			studentRepository.save(studentList);
		}else{
			throw new ApplicationException("Invalid data");
		}
		return response;
	}

	/**
	 * Method to update student attendance
	 * @param studentIds
	 * @param isPresent
	 * @param day
	 * @return
	 */
	public ApiResponse updateStudentAttendance(List<Integer> studentIds, String ispresent, int day){
		ApiResponse response = new ApiResponse();
		if(day>=1 && day<=8){
			
			List<Student> studentList = validateListStudent(studentIds);
			List<Student> updatedStudentList = new ArrayList<Student>();
		

		Map<String, List<Integer>> validVsInvalidMap = getInvalidIdsList(studentIds, studentList);
		response = populateResponse(validVsInvalidMap);
		
		Iterator<Student> it = studentList.iterator();
		while (it.hasNext()) {
			Student students = (Student) it.next();
			switch (day) {
			case 1:
				students.setDay1(ispresent);
				break;
			case 2:
				students.setDay2(ispresent);
				break;
			case 3:
				students.setDay3(ispresent);
				break;
			case 4:
				students.setDay4(ispresent);
				break;
			case 5:
				students.setDay5(ispresent);
				break;
			case 6:
				students.setDay6(ispresent);
				break;
			case 7:
				students.setDay7(ispresent);
				break;
			case 8:
				students.setDay8(ispresent);
				break;
			}

			updatedStudentList.add(students);
		}
		
		studentRepository.save(updatedStudentList);
		}else{
			throw new ApplicationException("Invalid data");
		}
		return response;
	}

	/**
	 * Method to update student by file upload
	 * @param file
	 * @param flag
	 * @return response
	 */
	public ApiResponse updateStudent(MultipartFile file, String flag) {
		ApiResponse apiResponse = new ApiResponse();
		try {
			List<StudentUploadAttendance> studentUploadAttendanceList = FileImportUtil.convertToStudentCSVBean(file,
					flag);
			NullAwareBeanUtilsBean onlyNotNullCopyProperty = new NullAwareBeanUtilsBean();
			List<Integer> studentIdList = new ArrayList<Integer>();
			for (StudentUploadAttendance stuAttendance : studentUploadAttendanceList) {
				studentIdList.add(Integer.parseInt(stuAttendance.getId()));
			}
			
			List<Student> studentListDB = validateListStudent(studentIdList);
			
			for (Student studentDB : studentListDB) {
				BeanPropertyValueEqualsPredicate predicate = new BeanPropertyValueEqualsPredicate("id",
						String.valueOf(studentDB.getId()));
				StudentUploadAttendance stuAttendance = (StudentUploadAttendance) CollectionUtils
						.find(studentUploadAttendanceList, predicate);

				onlyNotNullCopyProperty.copyProperties(studentDB, stuAttendance);

			}
			Map<String, List<Integer>> validVsInvalidMap = getInvalidIdsList(studentIdList, studentListDB);
			
			ApiResponse response = populateResponse(validVsInvalidMap);
			List<Integer> li=null;
			Set<Integer> invalidDataIdList = onlyNotNullCopyProperty.getInvalidDataList();
			if(!validVsInvalidMap.get("valid").isEmpty()){
				 li=validVsInvalidMap.get("valid");
				li.removeAll(invalidDataIdList);
				System.out.println(li);
			}
			studentListDB = removeInvalidDataFromList(studentListDB, invalidDataIdList);
			studentRepository.save(studentListDB);
			
			if (!invalidDataIdList.isEmpty()) {
				apiResponse.setFailRecordIds(invalidDataIdList.toString() + response.getFailRecordIds());
				apiResponse.setTotalRecords(String.valueOf(studentUploadAttendanceList.size()));
				apiResponse.setSuccessRecordsIds(li+"");
				apiResponse.setMessage("Successfully updated some records but either id is not valid or data is not valid of failed Id's");
			}else{
				apiResponse.setSuccessRecordsIds(validVsInvalidMap.get("valid")+"");
				apiResponse.setFailRecordIds(response.getFailRecordIds());
				apiResponse.setTotalRecords(String.valueOf(studentUploadAttendanceList.size()));
				apiResponse.setMessage("Successfully updated some records but either id is not valid or data is not valid of failed Id's");
			}
		} catch (InvocationTargetException | IllegalAccessException e) {
			throw new ApplicationException("Failed to update record" + e);
		}
		return apiResponse;
	}

	/**
	 * Method to remove student invalid data
	 * @param studentListDB
	 * @param invalidDataIdList
	 * @return studentListDB
	 */
	private List<Student> removeInvalidDataFromList(List<Student> studentListDB, Set<Integer> invalidDataIdList) {
		for (Integer id : invalidDataIdList) {
			BeanPropertyValueEqualsPredicate predicate = new BeanPropertyValueEqualsPredicate("id", String.valueOf(id));
			Student stuAttendance = (Student) CollectionUtils.find(studentListDB, predicate);
			studentListDB.remove(stuAttendance);			
		}
		return studentListDB;
	}

	/**
	 * This function overwrites values from given json string in to given
	 * objectToUpdate
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
	
	private Map<String,List<Integer>> getInvalidIdsList(List<Integer> studentIds,List<Student>studentList){
		List<Integer> validIds = studentList.stream().map(std -> std.getId()).collect(Collectors.toList());
		if (!validIds.isEmpty()) {
			studentIds.removeAll(validIds);			
		}
		Map<String, List<Integer>>map=new HashMap<String, List<Integer>>();
		map.put("valid", validIds);
		map.put("invalid", studentIds);
		return map;
	}
	
}