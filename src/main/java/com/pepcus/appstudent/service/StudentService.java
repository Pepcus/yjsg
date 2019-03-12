package com.pepcus.appstudent.service;

import static com.pepcus.appstudent.util.ApplicationConstants.ISPRESENT;
import static com.pepcus.appstudent.util.CommonUtil.TOTAL_RECORDS;
import static com.pepcus.appstudent.util.CommonUtil.convertDateToString;
import static com.pepcus.appstudent.util.CommonUtil.setRequestAttribute;
import static com.pepcus.appstudent.util.EntitySearchUtil.getEntitySearchSpecification;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.pepcus.appstudent.entity.Student;
import com.pepcus.appstudent.entity.StudentUploadAttendance;
import com.pepcus.appstudent.exception.ApplicationException;
import com.pepcus.appstudent.exception.BadRequestException;
import com.pepcus.appstudent.repository.StudentRepository;
import com.pepcus.appstudent.response.ApiResponse;
import com.pepcus.appstudent.util.ApplicationConstants;
import com.pepcus.appstudent.util.FileImportUtil;
import com.pepcus.appstudent.util.SMSUtil;
import com.pepcus.appstudent.util.Sortbyname;

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

	@Autowired
	private SMSService smsService;
	
	@PersistenceContext
	private EntityManager em;

	@Value("${com.pepcus.appstudent.admin.sendWelcomeSMS}")
	private boolean isSendSMS;
	
	@Value("${com.pepcus.appstudent.admin.sendPresentSMS}")
	private boolean isSendPresentSMS;
	
	@Value("${com.pepcus.appstudent.admin.sendOptInSMS}")
	private boolean isSendOptInSMS;
	
	@Value("${com.pepcus.appstudent.admin.sendOptOutSMS}")
	private boolean isSendOptOutSMS;
	
	

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
			if (students==null || students.isEmpty()) {
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
			SMSUtil.sendSMS(savedStudent);
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
		String optIn2019=std.getOptIn2019();
		Student updatedStudent = update(student, std);
		Date currentDate = Calendar.getInstance().getTime();
		List<Student>studentList=new ArrayList<Student>();

		updatedStudent.setDateLastModifiedInDB(currentDate);

		Student studentInDB = studentRepository.save(updatedStudent);
		if (!optIn2019.equalsIgnoreCase(updatedStudent.getOptIn2019())) {
			if (isSendOptInSMS) {
				if (updatedStudent.getOptIn2019().equalsIgnoreCase("Y")) {
					studentList.add(updatedStudent);
					smsService.sendOptInSMS(studentList);
				}
			}
			if (isSendOptOutSMS) {
				if (updatedStudent.getOptIn2019().equalsIgnoreCase("N")) {
					studentList.add(updatedStudent);
					smsService.sendOptOutSMS(studentList);
				}
			}
		}

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
			for (Student students : studentList) {
				students.setOptIn2019(student.getOptIn2019());
			}
			studentRepository.save(studentList);
			// Send Opt SMS
			if (isSendOptInSMS) {
				smsService.sendOptInSMS(studentList);
				response.setSmsMessage("SMS sent Successfully for OptIn students");
			} else{
				response.setSmsMessage("SMS not sent.Please make sure that send OptInSMS feature is enabled");
			}
			if (isSendOptOutSMS) {
				smsService.sendOptOutSMS(studentList);
				response.setSmsMessage(response.getSmsMessage().concat(", SMS sent Successfully for OptOut students"));
			}else{
				response.setSmsMessage(response.getSmsMessage().concat(", Please make sure that send OptOutSMS feature is enabled"));
			}
		}else{
			throw new BadRequestException("Invalid data");
		}
		return response; 
	}

	
	private  ApiResponse populateResponse(Map<String, List<Integer>> validInvalidIdsMap) {
		ApiResponse response = new ApiResponse();
		if (!validInvalidIdsMap.get("invalid").isEmpty()) {
			response.setCode(String.valueOf(HttpStatus.MULTI_STATUS));
			response.setFailRecordIds(String.valueOf(validInvalidIdsMap.get("invalid")));
			int total=validInvalidIdsMap.get("invalid").size()+validInvalidIdsMap.get("valid").size();
			response.setSuccessRecordsIds(String.valueOf(validInvalidIdsMap.get("valid")));
			response.setTotalRecords(String.valueOf(total));
			if (null!=validInvalidIdsMap.get("recordNotExist") && !validInvalidIdsMap.get("recordNotExist").isEmpty()) {
			response.setIdNotExist("ID's"+validInvalidIdsMap.get("recordNotExist")+" doesn’t exist in database and not processed");
			}
			response.setMessage("Some records failed and some updated");
		}
		else{
			response.setCode(String.valueOf(HttpStatus.OK));
			int total=validInvalidIdsMap.get("valid").size();
			response.setTotalRecords(String.valueOf(total));
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
			throw new BadRequestException("Invalid data");
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
		if(isSendPresentSMS) {
			smsService.sendBulkSMS(updatedStudentList,ApplicationConstants.ATTENDANCE,day);
			response.setSmsMessage("SMS sent Successfully for Present students");
		}else response.setSmsMessage("SMS not sent.Please make sure that send SMS feature is 'On' or 'true'.");
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
			List<StudentUploadAttendance> studentUploadAttendanceList = FileImportUtil.convertToStudentCSVBean(file,flag);
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
			Map<String, List<Integer>> validVsInvalidMap = getInvalidIdsList(studentIdList, studentListDB);
			List<Integer> successRecordList=new ArrayList<Integer>();
			
			//getting inValidIdList whose data is not correct 
			Set<Integer> invalidDataIdList = onlyNotNullCopyProperty.getInvalidDataList();
			
			//removing invalidDataIdList from success list 
			if(!validVsInvalidMap.get(ApplicationConstants.VALID).isEmpty()){
				successRecordList=validVsInvalidMap.get(ApplicationConstants.VALID);
				successRecordList.removeAll(invalidDataIdList);
				
			}
			//converting Set<Integer> to List<Integer>
			List<Integer> getInvalidList=new ArrayList<Integer>(invalidDataIdList);
			getInvalidList.addAll(validVsInvalidMap.get(ApplicationConstants.INVALID));
			validVsInvalidMap.put(ApplicationConstants.VALID,successRecordList);
			validVsInvalidMap.put(ApplicationConstants.INVALID,getInvalidList);
			apiResponse = populateResponse(validVsInvalidMap);
			//removing invalidStudent object 
			studentListDB = removeInvalidDataFromList(studentListDB, invalidDataIdList);
			studentRepository.save(studentListDB);

			// Send Opt SMS
			if (isSendOptInSMS) {
				smsService.sendOptInSMS(studentListDB);
				apiResponse.setSmsMessage("SMS sent Successfully for OptIn students");
			} else{
				apiResponse.setSmsMessage("SMS not sent.Please make sure that send OptInSMS feature is enabled");
			}
			if (isSendOptOutSMS) {
				smsService.sendOptOutSMS(studentListDB);
				apiResponse.setSmsMessage(apiResponse.getSmsMessage().concat(", SMS sent Successfully for OptOut students"));
			}else{
				apiResponse.setSmsMessage(apiResponse.getSmsMessage().concat(", Please make sure that send OptOutSMS feature is enabled"));
			}

		
		}catch (NumberFormatException e) {
			throw new BadRequestException("Invalid data in 'id' column can not process further: "+e.getMessage());
		} 
		catch (InvocationTargetException | IllegalAccessException  e) {
			throw new BadRequestException("Failed to update records: "+e);
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
	
	/**
	 * Method to get Map of students invalid and valid list
	 * 
	 * @param studentIds // coming from request
	 * @param studentList // database valid student list which is available in db
	 * @return map
	 */
	private Map<String,List<Integer>> getInvalidIdsList(List<Integer> studentIds,List<Student>studentList){
		List<Integer> validIds = studentList.stream().map(std -> std.getId()).collect(Collectors.toList());
		if (!validIds.isEmpty()) {
			studentIds.removeAll(validIds);			
		}
		Map<String, List<Integer>>map=new HashMap<String, List<Integer>>();
		map.put("valid", validIds);
		map.put("invalid", studentIds);
		map.put("recordNotExist", studentIds);
		return map;
	}

	/**
	 * Method to reset print status
	 * 
	 * @return
	 */
	public void updatePrint() {
		studentRepository.resetPrintStatus();
	}

	/**
	 * Method to get duplicate studentds records
	 * 
	 * @param allRequestParams
	 * @return
	 */
	public File getDuplicateRecords(MultipartFile file) {
		List<String> originalRecords = FileImportUtil.getCSVData(file);
		String headers[] = null;
		try(BufferedReader brFileContent = new BufferedReader(new InputStreamReader(file.getInputStream()))){ 
			List<String> fileContents = brFileContent.lines().collect(Collectors.toList());
			headers = fileContents.get(0).split(ApplicationConstants.COMMA_SEPARATOR);
		}
		 catch (IOException e) {
			throw new ApplicationException("Failed to read CSV file..!");
		}
		
		//getting Duplicate records
		List<String[]> duplicateRecords=getDuplicateRecords(originalRecords); 

		List<String>duplicateList=getDuplicateRecordFromOriginal(duplicateRecords,originalRecords);
		
		// Sorting data by Student name and father name 
		Collections.sort(duplicateList, new Sortbyname());
		
		List<String[]> finalDuplicateList = new ArrayList<>();

		// adding single duplicate record to final list
		for (String duplicateRecord : duplicateList) {
			String duplicate[] = duplicateRecord.split(ApplicationConstants.COMMA_SEPARATOR);
			finalDuplicateList.add(duplicate);   
		}
		File duplicateCSVData =FileImportUtil.getDuplicateDataCSV(headers,finalDuplicateList); 
		return duplicateCSVData;
	}

	// taking out duplicate record from original record
	private List<String> getDuplicateRecordFromOriginal(List<String[]> duplicateRecords,List<String> originalRecords) {
		List<String> duplicateList =new ArrayList<>();
		for (String singleduplicateRecord[] : duplicateRecords) {
			for (String originalRecord : originalRecords) {
				String singleOriginalRecord[] = originalRecord.split(ApplicationConstants.COMMA_SEPARATOR);
				if (singleduplicateRecord[0].equals(singleOriginalRecord[0])) { //checking if duplicate record (s[0], id) equals to original data(original[0],id) & adding into List 
					duplicateList.add(originalRecord);
				}
			}
		}
		return duplicateList;
	}

	private List<String[]> getDuplicateRecords(List<String> originalRecords) {
		List<String[]> duplicateRecords = new ArrayList<>();
		for (String record : originalRecords) {
			String recordArray[] = record.split(ApplicationConstants.COMMA_SEPARATOR);
			String fullname = recordArray[1].trim() + recordArray[2].trim();
			fullname = fullname.replaceAll(ApplicationConstants.REGEX_FOR_SPACE, "");
			int count = 0;
			int flag = 0;
			for (String duplicate : originalRecords) {
				String recordArrayDup[] = duplicate.split(ApplicationConstants.COMMA_SEPARATOR);
				String fullnameDuplicate = recordArrayDup[1].trim() + recordArrayDup[2].trim();
				fullnameDuplicate = fullnameDuplicate.replaceAll(ApplicationConstants.REGEX_FOR_SPACE, "");  // removing additional space
				if (fullname.equalsIgnoreCase(fullnameDuplicate)) {
					count++;
					if (count > 1 && flag == 0) {
						flag++;
						duplicateRecords.add(record.split(ApplicationConstants.COMMA_SEPARATOR));
					}
				}
			}
		}
		return duplicateRecords;
	}
	
	public ApiResponse updateStudentAttendance(MultipartFile file, String flag,int day) {
		ApiResponse apiResponse = new ApiResponse();
		List<StudentUploadAttendance> studentUploadAttendanceList = FileImportUtil.convertToStudentCSVBean(file,flag);
		NullAwareBeanUtilsBean onlyNotNullCopyProperty = new NullAwareBeanUtilsBean();
		List<Integer> studentIdList = new ArrayList<Integer>();
		for (StudentUploadAttendance stuAttendance : studentUploadAttendanceList) {
			studentIdList.add(Integer.parseInt(stuAttendance.getId()));
		}
		
		List<Student> studentListDB = validateListStudent(studentIdList);
		studentListDB=getStudentList(studentListDB,day);
		Map<String, List<Integer>> validVsInvalidMap = getInvalidIdsList(studentIdList, studentListDB);
		List<Integer> successRecordList=new ArrayList<Integer>();
		
		//getting inValidIdList whose data is not correct 
		Set<Integer> invalidDataIdList = onlyNotNullCopyProperty.getInvalidDataList();
		
		//removing invalidDataIdList from success list 
		if(!validVsInvalidMap.get(ApplicationConstants.VALID).isEmpty()){
			successRecordList=validVsInvalidMap.get(ApplicationConstants.VALID);
			successRecordList.removeAll(invalidDataIdList);
			
		}
		//converting Set<Integer> to List<Integer>
		List<Integer> getInvalidList=new ArrayList<Integer>(invalidDataIdList);
		getInvalidList.addAll(validVsInvalidMap.get(ApplicationConstants.INVALID));
		validVsInvalidMap.put(ApplicationConstants.VALID,successRecordList);
		validVsInvalidMap.put(ApplicationConstants.INVALID,getInvalidList);
		/*int totalRecords=successRecordList.size()+getInvalidList.size();
		apiResponse.setTotalRecords(String.valueOf(totalRecords));*/
		apiResponse = populateResponse(validVsInvalidMap);
		//removing invalidStudent object 
		//studentListDB = removeInvalidDataFromList(studentListDB, invalidDataIdList);
		studentRepository.save(studentListDB);
		if(isSendPresentSMS)
		{
			smsService.sendBulkSMS(studentListDB,ApplicationConstants.ATTENDANCE,day);
			apiResponse.setSmsMessage("SMS sent Successfully for Present students");
			
		}else apiResponse.setSmsMessage("SMS not sent.Please make sure that send SMS feature is enabled ");
		return apiResponse;
	}

	
	private List<Student> getStudentList(List<Student> studentList, int day) {
		List<Student> updatedStudentList=new ArrayList<>(); 
		Iterator<Student> it =studentList.iterator();
		while (it.hasNext()) {
			Student students = (Student) it.next();
			switch (day) {
			case 1:
				students.setDay1(ISPRESENT);
				break;
			case 2:
				students.setDay2(ISPRESENT);
				break;
			case 3:
				students.setDay3(ISPRESENT);
				break;
			case 4:
				students.setDay4(ISPRESENT);
				break;
			case 5:
				students.setDay5(ISPRESENT);
				break;
			case 6:
				students.setDay6(ISPRESENT);
				break;
			case 7:
				students.setDay7(ISPRESENT);
				break;
			case 8:
				students.setDay8(ISPRESENT);
				break;
			}

			updatedStudentList.add(students);
		}
		return updatedStudentList;
	}

}
