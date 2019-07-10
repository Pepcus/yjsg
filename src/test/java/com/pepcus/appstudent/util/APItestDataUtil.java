package com.pepcus.appstudent.util;

import static com.pepcus.appstudent.util.APItestDataUtil.getStudentData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.core.style.StylerUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pepcus.appstudent.entity.Person;
import com.pepcus.appstudent.entity.Student;

public class APItestDataUtil {
    /**
     * Method used to get single Student data
     * 
     * @author Rahul.Panwar
     * @return Student
     */
    public static Student getStudentData() {
        Student student = new Student();
        student.setId(1);
        student.setName("Ram");
        student.setFatherName("Mohan");
        student.setAge("18");
        student.setAddress("indore");
        student.setDateCreatedInDB(new Date());
        student.setDateLastModifiedInDB(new Date());
        student.setOptIn2019("N");
        student.setPrintStatus("N");
        return student;
    }
    
    /**
     * Method to get Student data as string
     * 
     * @return String
     * @throws JsonProcessingException
     */
    public static String getStudentJsonData() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(getStudentData());
    }
    
    /**
     * Method to get Student list
     * 
     * @author Rahul.Panwar
     * @return
     */
    public static List<Student>getStudentList() {
        Student student1 = new Student();
        student1.setId(1);
        student1.setName("Ram");
        
        Student student2 = new Student();
        student2.setId(2);
        student2.setName("rahul");
        
        Student student3 = new Student();
        student3.setId(3);
        student3.setName("mohan");
        
        return new ArrayList<>(Arrays.asList(new Student[] {student1, student2, student3}));
    }
    
    /**
     * Method to get list of Student ids
     * 
     * @author Rahul.Panwar
     * @return List<Integer>
     */
    public static List<Integer> getStudentIds() {
        return new ArrayList<>(Arrays.asList(new Integer[] {1, 2, 3}));
    }
    
    /**
     * Method used to get person data
     * @author Rahul.Panwar
     * @return
     */
    public static Person getPersonData() {
        Person person = new Person();
        person.setId(1);
        person.setName("Ram");
        person.setDateCreatedInDB(new Date());
        person.setDateLastModifiedInDB(new Date());
        return person;    
    }

}
