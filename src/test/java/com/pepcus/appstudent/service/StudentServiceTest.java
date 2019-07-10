package com.pepcus.appstudent.service;

import static com.pepcus.appstudent.util.APItestDataUtil.getStudentData;
import static com.pepcus.appstudent.util.APItestDataUtil.getStudentIds;
import static com.pepcus.appstudent.util.APItestDataUtil.getStudentJsonData;
import static com.pepcus.appstudent.util.APItestDataUtil.getStudentList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pepcus.appstudent.entity.Student;
import com.pepcus.appstudent.exception.BadRequestException;
import com.pepcus.appstudent.repository.StudentRepository;
import com.pepcus.appstudent.response.ApiResponse;
import com.pepcus.appstudent.util.ApplicationConstants;

/**
 * class use for Junit Test of Student service
 * 
 * @author Rahul.Panwar
 * @since 2019-07-08
 */
@RunWith(MockitoJUnitRunner.class)
public class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SmsService smsService;

    @Mock
    private EntityManager em;

    /**
     * Test getStudent when id is not exist in db
     */
    @Test(expected = BadRequestException.class)
    public void getStudentWithInvalidStudentIdTest() {

        Integer studentId = 1;
        when(studentRepository.findOne(studentId)).thenReturn(null);

        studentService.getStudent(studentId);
    }

    /**
     * Test getStudent when student id is exist in db
     */
    @Test
    public void getStudentsWithValidStudentIdTest() {

        Integer studentId = 1;
        when(studentRepository.findOne(studentId)).thenReturn(getStudentData());

        Student student = studentService.getStudent(studentId);

        assertEquals(1, student.getId().intValue());
        assertEquals("Ram", student.getName());
        assertEquals("18", student.getAge());
        assertEquals("indore", student.getAddress());
    }

    /**
     * Test createStudent with valid student data
     */
    @Test
    public void createStudentTest() {

        when(studentRepository.save(Mockito.any(Student.class))).thenReturn(getStudentData());
        when(smsService.isSMSFlagEnabled("smsCreate")).thenReturn(false);

        Student student = studentService.createStudent(getStudentData());

        assertEquals(1, student.getId().intValue());
        assertEquals("Ram", student.getName());
        assertEquals("18", student.getAge());
        assertEquals("indore", student.getAddress());
    }

    /**
     * Test updateStudent when student not exist in db
     * 
     * @throws JsonProcessingException
     * @throws IOException
     */
    @Test(expected = BadRequestException.class)
    public void updateStudentWithInValidDataTest() throws JsonProcessingException, IOException {
        String student = getStudentJsonData();
        when(studentRepository.findOne(Mockito.any(Integer.class))).thenReturn(null);
        when(smsService.isSMSFlagEnabled("smsOptIn")).thenReturn(false);
        when(smsService.isSMSFlagEnabled("smsOptOut")).thenReturn(false);
        studentService.updateStudent(student, new Integer(1));
    }

    /**
     * Test updateStudent when vstudent not exist in db
     * 
     * @throws JsonProcessingException
     * @throws IOException
     */
    @Test
    public void updateStudentWithValidDataTest() throws JsonProcessingException, IOException {
        Student student = getStudentData();
        student.setName("pawan");
        student.setAddress("bhopal");
        String studentString = new ObjectMapper().writeValueAsString(student);
        Integer studentId = 1;
        when(studentRepository.findOne(studentId)).thenReturn(getStudentData());
        when(studentRepository.save(Mockito.any(Student.class))).thenReturn(student);
        when(smsService.isSMSFlagEnabled("smsOptIn")).thenReturn(false);
        when(smsService.isSMSFlagEnabled("smsOptOut")).thenReturn(false);

        Student udatedStudent = studentService.updateStudent(studentString, studentId);

        assertEquals("pawan", udatedStudent.getName());
        assertEquals("bhopal", udatedStudent.getAddress());
    }

    /**
     * Test updateStudentOptin when opt in 2019 not set
     */
    @Test(expected = BadRequestException.class)
    public void updateStudentOptinWithInvalidOptIn2019Test() {
        Student student = getStudentData();
        student.setOptIn2019(null);
        List<Integer> studentIds = new ArrayList<>();
        studentService.updateStudentOptin(studentIds, student);
    }

    /**
     * Test updateStudentOptin when student list empty in db *
     */
    @Test(expected = BadRequestException.class)
    public void updateStudentOptinWithEmptyStudentListTest() {
        Student student = getStudentData();
        List<Integer> ids = getStudentIds();
        when(studentRepository.findByIdIn(ids)).thenReturn(null);
        List<Integer> studentIds = new ArrayList<>();
        studentService.updateStudentOptin(studentIds, student);
    }

    /**
     * Test with valid student data
     */
    @Test
    public void updateStudentOptinWithValidTest() {
        Student student = getStudentData();
        List<Integer> ids = getStudentIds();
        when(studentRepository.findByIdIn(ids)).thenReturn(getStudentList());
        when(smsService.isSMSFlagEnabled("smsOptIn")).thenReturn(false);
        when(smsService.isSMSFlagEnabled(ApplicationConstants.SMS_OPTOUT)).thenReturn(false);
        ApiResponse apiResponse = studentService.updateStudentOptin(ids, student);

        assertEquals(HttpStatus.OK.toString(), apiResponse.getCode());
        assertEquals("Updated Successfully", apiResponse.getMessage());
    }

    /**
     * Test updateStudentPrintStatus with valid student data
     */
    @Test
    public void updateStudentPrintStatusTest() {
        Student student = getStudentData();
        List<Integer> ids = getStudentIds();
        when(studentRepository.findByIdIn(ids)).thenReturn(getStudentList());
        when(smsService.isSMSFlagEnabled("smsOptIn")).thenReturn(false);
        when(smsService.isSMSFlagEnabled(ApplicationConstants.SMS_OPTOUT)).thenReturn(false);
        ApiResponse apiResponse = studentService.updateStudentPrintStatus(ids, student);

        assertEquals(HttpStatus.OK.toString(), apiResponse.getCode());
        assertEquals("Updated Successfully", apiResponse.getMessage());
    }

    /**
     * Test updateStudentAttendance when day is invalid
     */
    @Test(expected = BadRequestException.class)
    public void updateStudentAttendanceWithInvalidDayTest() {
        List<Integer> studentIds = getStudentIds();
        when(studentRepository.findByIdIn(studentIds)).thenReturn(getStudentList());
        studentService.updateStudentAttendance(studentIds, "Y", 9);
    }

    /**
     * Test updateStudentAttendance with valid data
     */
    @Test
    public void updateStudentAttendanceWithValidDataTest() {
        List<Integer> studentIds = getStudentIds();
        when(studentRepository.findByIdIn(studentIds)).thenReturn(getStudentList());
        when(smsService.isSMSFlagEnabled("smsPresent")).thenReturn(false);
        ApiResponse apiResponse = studentService.updateStudentAttendance(studentIds, "Y", 8);

        assertEquals(HttpStatus.OK.toString(), apiResponse.getCode());
        assertEquals("Updated Successfully", apiResponse.getMessage());
    }
}
