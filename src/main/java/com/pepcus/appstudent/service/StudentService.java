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
import java.util.Arrays;
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
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import com.pepcus.appstudent.entity.DuplicateRegistration;
import com.pepcus.appstudent.entity.Student;
import com.pepcus.appstudent.entity.StudentWrapper;
import com.pepcus.appstudent.exception.ApplicationException;
import com.pepcus.appstudent.exception.BadRequestException;
import com.pepcus.appstudent.repository.DuplicateRegistrationRepository;
import com.pepcus.appstudent.repository.StudentRepository;
import com.pepcus.appstudent.response.ApiResponse;
import com.pepcus.appstudent.specifications.StudentSpecification;
import com.pepcus.appstudent.util.ApplicationConstants;
import com.pepcus.appstudent.util.FileImportUtil;
import com.pepcus.appstudent.util.NullAwareBeanUtilsBean;
import com.pepcus.appstudent.util.SMSUtil;
import com.pepcus.appstudent.util.Sortbyname;
import com.pepcus.appstudent.util.StudentNullAwareBeanUtil;

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
    private DuplicateRegistrationRepository duplicateRegistrationRepository;

    @Autowired
    private SmsService smsService;

    @PersistenceContext
    private EntityManager em;
    
    @Value("${admin.contact}")
    private String adminContact;

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

    private List<Student> getStudentsList(List<Integer> ids) {
        List<Student> students = studentRepository.findByIdIn(ids);
        if (students == null || students.isEmpty()) {
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
    public Student createStudent(Student student) {
    	Student savedStudent = null;
    	
        Date currentDate = Calendar.getInstance().getTime();
        student.setDateCreatedInDB(currentDate);
        student.setDateLastModifiedInDB(currentDate);

        // Generate secretKey for student
        String secretKey = generateSecretKey();
        student.setSecretKey(secretKey);
        Student duplicateStudent = student.isAllowDuplicate() ? null : validateDuplicateStudent(student);
        
        if (duplicateStudent != null) {
			// update optin if duplicate
			if (!duplicateStudent.isPartialMatch()) {
				duplicateStudent.setOptIn2020("Y");
				savedStudent = studentRepository.save(duplicateStudent);
			} else {
				DuplicateRegistration duplicateRegistration = getDuplicateRegistrationEntity(student, duplicateStudent);
				duplicateRegistration = duplicateRegistrationRepository.save(duplicateRegistration);
				if (smsService.isSMSFlagEnabled(ApplicationConstants.SMS_CREATE)) {
					SMSUtil.sendSMSForDuplicateRegistrationToAdmin(duplicateRegistration, adminContact);
					SMSUtil.sendSMSForDuplicateRegistrationToStudent(student);
				}
				
				throw new BadRequestException(ApplicationConstants.PARTIAL_DUPLICATE_SMS, 1001);
			}
		} else {
        	savedStudent = studentRepository.save(student);
        }
        
        if(savedStudent.getDateCreatedInDB()!=null) {
			savedStudent.setCreatedDate(convertDateToString(savedStudent.getDateCreatedInDB()));
        }
		if (savedStudent.getDateLastModifiedInDB() != null) {
			savedStudent.setLastModifiedDate(convertDateToString(savedStudent.getDateLastModifiedInDB()));
		}
		if (smsService.isSMSFlagEnabled(ApplicationConstants.SMS_CREATE)) {
			if (duplicateStudent != null) {
				smsService.sendAlreadyRegisterSMS(savedStudent);
			} else {
				SMSUtil.sendSMS(savedStudent);
			}
		}
		if (duplicateStudent != null) {
			throw new BadRequestException("Dear " + savedStudent.getName() + " (ID # " + savedStudent.getId() + "), "
					+ ApplicationConstants.EXACT_DUPLICATE + "(# " + savedStudent.getMobile() + ").", 1000);
		}
        return savedStudent;
    }

	private DuplicateRegistration getDuplicateRegistrationEntity(Student student, Student duplicateStudent) {
		DuplicateRegistration duplicateRegistration = new DuplicateRegistration();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String studentJsonString = objectMapper.writeValueAsString(student);
			duplicateRegistration.setDuplicateOfStudentId(duplicateStudent.getId());
			duplicateRegistration.setDuplicateStudentJson(studentJsonString);
		} catch (JsonProcessingException e) {
			return null;
		}
		return duplicateRegistration;
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
        String optIn2020 = std.getOptIn2020();
        Student updatedStudent = update(student, std);
        Date currentDate = Calendar.getInstance().getTime();
        List<Student> studentList = new ArrayList<Student>();

        updatedStudent.setDateLastModifiedInDB(currentDate);

        Student studentInDB = studentRepository.save(updatedStudent);
        if (!StringUtils.isEmpty(std.getOptIn2020()) && (optIn2020!=null && !optIn2020.equalsIgnoreCase(updatedStudent.getOptIn2020()))) {
            if (smsService.isSMSFlagEnabled(ApplicationConstants.SMS_OPTIN)) {
                if (updatedStudent.getOptIn2020().equalsIgnoreCase(ISPRESENT)) {
                    studentList.add(updatedStudent);
                    smsService.sendOptInSMS(studentList);
                }
            }
            if (smsService.isSMSFlagEnabled(ApplicationConstants.SMS_OPTOUT)) {
                if (updatedStudent.getOptIn2020().equalsIgnoreCase(ApplicationConstants.NO)) {
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
     * 
     * @param studentIds
     * @param student
     * @return
     */
    public ApiResponse updateStudentOptin(List<Integer> studentIds, Student student) {
        ApiResponse response = null;

        if (student.getOptIn2020() != null && (student.getOptIn2020().equalsIgnoreCase(ISPRESENT)
                || student.getOptIn2020().equalsIgnoreCase(ApplicationConstants.NO))) {
            List<Student> studentList = getStudentsList(studentIds);
            Map<String, List<Integer>> validVsInvalidMap = getInvalidIdsList(studentIds, studentList);
            response = populateResponse(validVsInvalidMap);
            for (Student students : studentList) {
                students.setOptIn2020(student.getOptIn2020());
            }
            studentRepository.save(studentList);
            // Send Opt SMS
            if (smsService.isSMSFlagEnabled(ApplicationConstants.SMS_OPTIN)) {
                smsService.sendOptInSMS(studentList);
                response.setSmsMessage(
                        ApplicationConstants.SMS_SENT_SUCCESSFULLY + ApplicationConstants.FOR_OPTIN_STUDENTS);
            } else {
                response.setSmsMessage(
                        ApplicationConstants.SMS_NOT_SENT + ApplicationConstants.SMS_OPTIN_FEATURE_DISABLE);
            }
            if (smsService.isSMSFlagEnabled(ApplicationConstants.SMS_OPTOUT)) {
                smsService.sendOptOutSMS(studentList);
                response.setSmsMessage(response.getSmsMessage().concat(
                        "," + ApplicationConstants.SMS_SENT_SUCCESSFULLY + ApplicationConstants.FOR_OPTOUT_STUDENTS));
            } else {
                response.setSmsMessage(
                        response.getSmsMessage().concat(", " + ApplicationConstants.SMS_OPTOUT_FEATURE_DISABLE));
            }
        } else {
            throw new BadRequestException(ApplicationConstants.INVALID_DATA);
        }
        return response;
    }

    private ApiResponse populateResponse(Map<String, List<Integer>> validInvalidIdsMap) {
        ApiResponse response = new ApiResponse();
        if (!validInvalidIdsMap.get(ApplicationConstants.INVALID).isEmpty()) {
            response.setCode(String.valueOf(HttpStatus.MULTI_STATUS));
            response.setFailRecordIds(String.valueOf(validInvalidIdsMap.get(ApplicationConstants.INVALID)));
            int total = validInvalidIdsMap.get(ApplicationConstants.INVALID).size()
                    + validInvalidIdsMap.get(ApplicationConstants.VALID).size();
            response.setSuccessRecordsIds(validInvalidIdsMap.get(ApplicationConstants.INVALID).isEmpty() ? "":String.valueOf(validInvalidIdsMap.get(ApplicationConstants.VALID)));
            response.setTotalRecords(String.valueOf(total));
            if (null != validInvalidIdsMap.get(ApplicationConstants.RECORD_NOT_EXIST)
                    && !validInvalidIdsMap.get(ApplicationConstants.RECORD_NOT_EXIST).isEmpty()) {
                response.setIdNotExist("ID's" + validInvalidIdsMap.get(ApplicationConstants.RECORD_NOT_EXIST)
                        + " doesn’t exist in database and not processed");
            }
            response.setMessage(ApplicationConstants.SOME_UPDATED_SOME_FAILED);
        } else {
            response.setCode(String.valueOf(HttpStatus.OK));
            int total = validInvalidIdsMap.get(ApplicationConstants.VALID).size();
            response.setTotalRecords(String.valueOf(total));
            response.setSuccessRecordsIds(validInvalidIdsMap.get(ApplicationConstants.INVALID).isEmpty() ? "":String.valueOf(validInvalidIdsMap.get(ApplicationConstants.VALID)));
            response.setMessage(ApplicationConstants.UPDATED_SUCCESSFULLY);
        }
        return response;
    }

    /**
     * Method to update student Print Status
     * 
     * @param studentIds
     * @param student
     * @return
     */
    public ApiResponse updateStudentPrintStatus(List<Integer> studentIds, Student student) {
        ApiResponse response = new ApiResponse();
        if (student.getPrintStatus() != null && student.getPrintStatus().equalsIgnoreCase(ISPRESENT)
                || student.getPrintStatus().equalsIgnoreCase(ApplicationConstants.NO)) {
            List<Student> studentList = getStudentsList(studentIds);

            Map<String, List<Integer>> validVsInvalidMap = getInvalidIdsList(studentIds, studentList);
            response = populateResponse(validVsInvalidMap);

            ListIterator<Student> iterator = studentList.listIterator();
            while (iterator.hasNext()) {
                Student students = (Student) iterator.next();
                students.setPrintStatus(student.getPrintStatus());
                iterator.add(students);
            }
            studentRepository.save(studentList);
        } else {
            throw new BadRequestException(ApplicationConstants.INVALID_DATA);
        }
        return response;
    }

    /**
     * Method to update student attendance
     * 
     * @param studentIds
     * @param isPresent
     * @param day
     * @return
     */
    public ApiResponse updateStudentAttendance(List<Integer> studentIds, String ispresent, int day) {
        ApiResponse response = new ApiResponse();
        List<Student> studentList = getStudentsList(studentIds);
        List<Student> updatedStudentList = new ArrayList<Student>();
        Map<String, List<Integer>> validVsInvalidMap = getInvalidIdsList(studentIds, studentList);
        response = populateResponse(validVsInvalidMap);

        for (Student students : studentList) {
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
                
           default :
           throw new BadRequestException(ApplicationConstants.DAY_INVALID);
            }

            updatedStudentList.add(students);
        }

        studentRepository.save(updatedStudentList);
        if (smsService.isSMSFlagEnabled(ApplicationConstants.SMS_PRESENT)) {
            smsService.sendBulkSMS(updatedStudentList, ApplicationConstants.ATTENDANCE, day);
            response.setSmsMessage(
                    ApplicationConstants.SMS_SENT_SUCCESSFULLY + ApplicationConstants.FOR_PRESENT_STUDENTS);
        } else
            response.setSmsMessage(ApplicationConstants.SMS_FEATURE_DISABLE);
        return response;
    }

    /**
     * Method to update student by file upload
     * 
     * @param file
     * @param flag
     * @return apiresponse
     */
    public ApiResponse updateStudent(MultipartFile file, String flag) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            List<StudentWrapper> studentUploadAttendanceList = FileImportUtil.convertToStudentCSVBean(file,
                    flag);
            NullAwareBeanUtilsBean onlyNotNullCopyProperty = new NullAwareBeanUtilsBean();
            List<Integer> studentIdList = new ArrayList<Integer>();
            for (StudentWrapper stuAttendance : studentUploadAttendanceList) {
                studentIdList.add(Integer.parseInt(stuAttendance.getId()));
            }

            List<Student> studentListDB = getStudentsList(studentIdList);
            copyBeanProperty(studentListDB,studentUploadAttendanceList,onlyNotNullCopyProperty);
            Map<String, List<Integer>> validVsInvalidMap = getInvalidIdsList(studentIdList, studentListDB);
            List<Integer> successRecordList = new ArrayList<Integer>();

            // getting inValidIdList whose data is not correct
            Set<Integer> invalidDataIdList = onlyNotNullCopyProperty.getInvalidDataList();

            // removing invalidDataIdList from success list
            if (!validVsInvalidMap.get(ApplicationConstants.VALID).isEmpty()) {
                successRecordList = validVsInvalidMap.get(ApplicationConstants.VALID);
                successRecordList.removeAll(invalidDataIdList);

            }
            // converting Set<Integer> to List<Integer>
            List<Integer> getInvalidList = new ArrayList<Integer>(invalidDataIdList);
            getInvalidList.addAll(validVsInvalidMap.get(ApplicationConstants.INVALID));
            validVsInvalidMap.put(ApplicationConstants.VALID, successRecordList);
            validVsInvalidMap.put(ApplicationConstants.INVALID, getInvalidList);
            apiResponse = populateResponse(validVsInvalidMap);
            // removing invalidStudent object
            studentListDB = removeInvalidDataFromList(studentListDB, invalidDataIdList);
            studentRepository.save(studentListDB);

            // Send Opt SMS
            if (smsService.isSMSFlagEnabled(ApplicationConstants.SMS_OPTIN)) {
                smsService.sendOptInSMS(studentListDB);
                apiResponse.setSmsMessage(
                        ApplicationConstants.SMS_SENT_SUCCESSFULLY + ApplicationConstants.FOR_OPTIN_STUDENTS);
            } else {
                apiResponse.setSmsMessage(
                        ApplicationConstants.SMS_NOT_SENT + ApplicationConstants.SMS_OPTIN_FEATURE_DISABLE);
            }
            if (smsService.isSMSFlagEnabled(ApplicationConstants.SMS_OPTOUT)) {
                smsService.sendOptOutSMS(studentListDB);
                apiResponse.setSmsMessage(apiResponse.getSmsMessage().concat(
                        ", " + ApplicationConstants.SMS_SENT_SUCCESSFULLY + ApplicationConstants.FOR_OPTOUT_STUDENTS));
            } else {
                apiResponse.setSmsMessage(
                        apiResponse.getSmsMessage().concat(", " + ApplicationConstants.SMS_OPTOUT_FEATURE_DISABLE));
            }

        } catch (NumberFormatException e) {
            throw new BadRequestException(ApplicationConstants.INVALID_DATA_IN_ID_COLUMN + e.getMessage());
        } 
        return apiResponse;
    }

    /**
     * Method to remove student invalid data
     * 
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
     * Method to get Map of students invalid and valid list
     * 
     * @param studentIds
     *            // coming from request
     * @param studentList
     *            // database valid student list which is available in db
     * @return map
     */
    private Map<String, List<Integer>> getInvalidIdsList(List<Integer> studentIds, List<Student> studentList) {
        List<Integer> validIds = studentList.stream().map(std -> std.getId()).collect(Collectors.toList());
        if (!validIds.isEmpty()) {
            studentIds.removeAll(validIds);
        }
        Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();
        map.put(ApplicationConstants.VALID, validIds);
        map.put(ApplicationConstants.INVALID, studentIds);
        map.put(ApplicationConstants.RECORD_NOT_EXIST, studentIds);
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
        try (BufferedReader brFileContent = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            List<String> fileContents = brFileContent.lines().collect(Collectors.toList());
            headers = fileContents.get(0).split(ApplicationConstants.COMMA_SEPARATOR);
        } catch (IOException e) {
            throw new ApplicationException(ApplicationConstants.UNABLE_TO_READ_CSV);
        }

        // getting Duplicate records
        List<String[]> duplicateRecords = getDuplicateRecords(originalRecords);

        List<String> duplicateList = getDuplicateRecordFromOriginal(duplicateRecords, originalRecords);

        // Sorting data by Student name and father name
        Collections.sort(duplicateList, new Sortbyname());

        List<String[]> finalDuplicateList = new ArrayList<>();

        // adding single duplicate record to final list
        for (String duplicateRecord : duplicateList) {
            String duplicate[] = duplicateRecord.split(ApplicationConstants.COMMA_SEPARATOR);
            finalDuplicateList.add(duplicate);
        }
        File duplicateCSVData = FileImportUtil.getDuplicateDataCSV(headers, finalDuplicateList);
        return duplicateCSVData;
    }

    /**
     * Method to get duplicate record from original record
     * 
     * @param duplicateRecords
     * @param originalRecords
     * @return
     */
    private List<String> getDuplicateRecordFromOriginal(List<String[]> duplicateRecords, List<String> originalRecords) {
        List<String> duplicateList = new ArrayList<>();
        for (String singleduplicateRecord[] : duplicateRecords) {
            for (String originalRecord : originalRecords) {
                String singleOriginalRecord[] = originalRecord.split(ApplicationConstants.COMMA_SEPARATOR);
                if (singleduplicateRecord[0].equals(singleOriginalRecord[0])) { // checking
                                                                                // if
                                                                                // duplicate
                                                                                // record
                                                                                // (s[0],
                                                                                // id)
                                                                                // equals
                                                                                // to
                                                                                // original
                                                                                // data(original[0],id)
                                                                                // &
                                                                                // adding
                                                                                // into
                                                                                // List
                    duplicateList.add(originalRecord);
                }
            }
        }
        return duplicateList;
    }

    /**
     * Method to get duplicate records
     * @param originalRecords
     * @return
     */
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
                fullnameDuplicate = fullnameDuplicate.replaceAll(ApplicationConstants.REGEX_FOR_SPACE, ""); // removing
                                                                                                            // additional
                                                                                                            // space
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

    /**
     * Method to update student attendance by csv
     * @param day
     * @param flag
     * @param file
     * @return
     */
    public ApiResponse updateStudentAttendance(MultipartFile file, String flag, int day) {
        ApiResponse apiResponse = new ApiResponse();
        List<StudentWrapper> studentUploadAttendanceList = FileImportUtil.convertToStudentCSVBean(file, flag);
        NullAwareBeanUtilsBean onlyNotNullCopyProperty = new NullAwareBeanUtilsBean();
        List<Integer> studentIdList = new ArrayList<Integer>();
        for (StudentWrapper stuAttendance : studentUploadAttendanceList) {
            studentIdList.add(Integer.parseInt(stuAttendance.getId()));
        }

        List<Student> studentListDB = getStudentsList(studentIdList);
        studentListDB = getStudentList(studentListDB, day);
        Map<String, List<Integer>> validVsInvalidMap = getInvalidIdsList(studentIdList, studentListDB);
        List<Integer> successRecordList = new ArrayList<Integer>();

        // getting inValidIdList whose data is not correct
        Set<Integer> invalidDataIdList = onlyNotNullCopyProperty.getInvalidDataList();

        // removing invalidDataIdList from success list
        if (!validVsInvalidMap.get(ApplicationConstants.VALID).isEmpty()) {
            successRecordList = validVsInvalidMap.get(ApplicationConstants.VALID);
            successRecordList.removeAll(invalidDataIdList);

        }
        // converting Set<Integer> to List<Integer>
        List<Integer> getInvalidList = new ArrayList<Integer>(invalidDataIdList);
        getInvalidList.addAll(validVsInvalidMap.get(ApplicationConstants.INVALID));
        validVsInvalidMap.put(ApplicationConstants.VALID, successRecordList);
        validVsInvalidMap.put(ApplicationConstants.INVALID, getInvalidList);
        apiResponse = populateResponse(validVsInvalidMap);
        // removing invalidStudent object
        studentRepository.save(studentListDB);
        if (smsService.isSMSFlagEnabled(ApplicationConstants.SMS_PRESENT)) {
            smsService.sendBulkSMS(studentListDB, ApplicationConstants.ATTENDANCE, day);
            apiResponse.setSmsMessage(
                    ApplicationConstants.SMS_SENT_SUCCESSFULLY + ApplicationConstants.FOR_PRESENT_STUDENTS);

        } else
            apiResponse.setSmsMessage(ApplicationConstants.SMS_FEATURE_DISABLE);
        return apiResponse;
    }

    
    /**
     * Method to set student attendance of particular day
     * @param day
     * @param studentList
     * @return
     */
    private List<Student> getStudentList(List<Student> studentList, int day) {
        List<Student> updatedStudentList = new ArrayList<>();
        Iterator<Student> it = studentList.iterator();
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
    
    
    /**
     * Method to update student in bulk by csv
     * @param file
     * @param flag
     * @return apiresponse
     */
    public ApiResponse updateStudentInBulk(MultipartFile file, String flag) {
        ApiResponse apiResponse = new ApiResponse();
            List<StudentWrapper> studentUploadAttendanceList = FileImportUtil.convertToStudentCSVBean(file,flag);
            StudentNullAwareBeanUtil onlyNotNullCopyProperty = new StudentNullAwareBeanUtil();
            List<Integer> studentIdList = new ArrayList<Integer>();
            for (StudentWrapper stuAttendance : studentUploadAttendanceList) {
                studentIdList.add(Integer.parseInt(stuAttendance.getId()));
            }

            List<Student> studentListDB = getStudentsList(studentIdList);
            
            Map<String, List<Integer>> validVsInvalidMap = getInvalidIdsList(studentIdList, studentListDB);

            if(validVsInvalidMap.get(ApplicationConstants.INVALID).size()>0){
                throw new BadRequestException("Id's"+validVsInvalidMap.get(ApplicationConstants.INVALID)+" not exist.Failed to processed");
            }
            copyBeanProperty(studentListDB,studentUploadAttendanceList,onlyNotNullCopyProperty);
            
            // getting inValidIdList whose data is not correct
            Set<Integer> invalidDataIdList = onlyNotNullCopyProperty.getInvalidDataList();
            if(invalidDataIdList.size()>0){
                throw new BadRequestException("Id's "+invalidDataIdList+" data is not correct.Failed to processed");
            }
            studentRepository.save(studentListDB);
            apiResponse = populateResponse(validVsInvalidMap);
        return apiResponse;
    }
    
    /**
     * Method to copy bean StudentUploadAttendance into Student
     * @param studentListDB
     * @param studentUploadAttendanceList
     * @param onlyNotNullCopyProperty
     */
    private void copyBeanProperty(List<Student> studentListDB,List<StudentWrapper>studentUploadAttendanceList,BeanUtilsBean onlyNotNullCopyProperty){
        for (Student studentDB : studentListDB) {
            BeanPropertyValueEqualsPredicate predicate = new BeanPropertyValueEqualsPredicate("id",String.valueOf(studentDB.getId()));
            StudentWrapper stuAttendance = (StudentWrapper) CollectionUtils.find(studentUploadAttendanceList, predicate);
            try {
                    onlyNotNullCopyProperty.copyProperties(studentDB, stuAttendance);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new BadRequestException(ApplicationConstants.FAILED_TO_UPDATE + e);
            }
        }
        
    }
    
 	private Student validateDuplicateStudent(Student student) {
		Student duplicateStudent = null;
		String studentName = student.getName().toLowerCase().trim();
		String fatherName = student.getFatherName().toLowerCase().trim();
		String fatherMobileNumber = student.getMobile().trim();
		List<Student> students = null;
		
		String compressStudentName = studentName.replaceAll("[aeiou]", "");
		String compressFatherName = fatherName.replaceAll("[aeiou]", "");
		String studentNameForSearch = "%"+studentName.replaceAll("[aeiou]", "%")+"%";
		String fatherNameForsearch = "%"+fatherName.replaceAll("[aeiou]", "%")+"%";
		// get 10 digit phone number
		if (student.getMobile() != null && student.getMobile().length() > 10) {
			int length = fatherMobileNumber.length();
			int extraNumberCount = length - 10;
			fatherMobileNumber = "%" + fatherMobileNumber.substring(extraNumberCount);
		}
		// check for exactly match
		students = getStudentsByFilterAttributes(studentName, fatherName, fatherMobileNumber);
		if(CollectionUtils.isNotEmpty(students)) {
			return students.stream().findFirst().get();
		}
		
	
		//check for exactly match 
		students = getStudentsByFilterAttributes(null, null, fatherMobileNumber);
		if (CollectionUtils.isNotEmpty(students)) {;
			duplicateStudent = students.stream().findFirst().get();
			for (Student studentDB : students) {
				if (studentDB.getName().toLowerCase().contains(studentName)
						|| studentName.contains(studentDB.getName().toLowerCase())) {
					return duplicateStudent;
				} else {
					String compressStudentNameDB = studentDB.getName().toLowerCase().replaceAll("[aeiou]", "");
					if (compressStudentNameDB.toLowerCase().contains(compressStudentName)
							|| compressStudentName.contains(compressStudentNameDB.toLowerCase())) {
						return duplicateStudent;
					}
				}
			}
		}
		// check partial match
		students = getStudentsByFilterAttributes(studentNameForSearch, fatherNameForsearch, null);
		if (CollectionUtils.isNotEmpty(students)) {
			student = students.stream().findFirst().get();
			String compressFatherNameDB = student.getFatherName().toLowerCase().replaceAll("[aeiou]", "");
			String compressStudentNameDB = student.getName().toLowerCase().replaceAll("[aeiou]", "");
			if((compressFatherNameDB.toLowerCase().contains(compressFatherName)
					|| compressFatherName.contains(compressFatherNameDB.toLowerCase())) && (compressStudentNameDB.toLowerCase().contains(compressStudentName)
							|| compressStudentName.contains(compressStudentNameDB.toLowerCase()))) {
				duplicateStudent = students.stream().findFirst().get();
				duplicateStudent.setPartialMatch(true);
				return duplicateStudent;
			}
			
		}
		return duplicateStudent;
	}
	
	private List<Student> getStudentsByFilterAttributes(String studentName, String fatherName, String fatherMobileNumber) {
		return studentRepository
				.findAll(StudentSpecification.getStudents(studentName, fatherName, fatherMobileNumber));
	}

}
