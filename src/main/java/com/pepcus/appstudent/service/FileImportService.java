package com.pepcus.appstudent.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.pepcus.appstudent.entity.Student;
import com.pepcus.appstudent.entity.StudentUploadAttendance;

@Service
public class FileImportService {

	@Autowired
	private StudentService studentService;
	

	
	
	public void updateStudentOptIn(MultipartFile file, HttpServletRequest request) {
		File  serverFile=getServerFile(file, request);
		System.out.println("updateStudentOptIn method calledddd");
		List<Student> students = new ArrayList();
		String[] nextLine;
		try {
			// read file
			// CSVReader(fileReader, ';', '\'', 1) means
			// using separator ; and using single quote ' . Skip first line when
			// read
			// starttime = System.currentTimeMillis() / 1000;
			try (FileReader fileReader = new FileReader(serverFile);
					CSVReader reader = new CSVReader(fileReader, ';', '\'', 0);) {
				nextLine = reader.readNext();
				while ((nextLine = reader.readNext()) != null) {
					for (int i = 0; i < nextLine.length; i++) {
						System.out.println(nextLine[i]);
						if (!nextLine[i].trim().isEmpty()) {
							String[] tokens = nextLine[i].split(",");
							if (tokens.length > 0)
								if (nextLine.length > 0 && !tokens[0].trim().isEmpty()
										&& tokens[0].trim().length() > 0) {
									Student student = new Student();
									student.setId(Integer.parseInt(tokens[0].trim()));
									student.setOptIn2019(tokens[1].trim());
									students.add(student);
								}
						}
					}

				}
				studentService.updateStudentList(students);
			}
		} catch (IOException e) {
			System.out.println("error while reading csv and put to db : " + e.getMessage());
		}

	}	
	
	public void uploadStudentAttendance(MultipartFile file, HttpServletRequest request) throws IOException, IllegalAccessException, InvocationTargetException{
		System.out.println(" uploadStudentAttendance method calleddd");
		File serverFile=getServerFile(file, request);
		List<StudentUploadAttendance> studentUploadAttendanceList = new ArrayList();
		List<StudentUploadAttendance> stu = new ArrayList<>();
		List<Student> student = new ArrayList<Student>();

		try (Reader reader = Files.newBufferedReader(Paths.get(serverFile.getAbsolutePath()));) {
			HeaderColumnNameMappingStrategy<StudentUploadAttendance> strategy = new HeaderColumnNameMappingStrategy<StudentUploadAttendance>();
			strategy.setType(StudentUploadAttendance.class);			
			CsvToBean<StudentUploadAttendance> csvToBean = new CsvToBean<StudentUploadAttendance>();
			
			
			studentUploadAttendanceList = csvToBean.parse(strategy, reader);
			//DozerBeanMapper map = new DozerBeanMapper();
			//map.map(studentUploadAttendanceList,student);
			BeanUtils.copyProperties(student, studentUploadAttendanceList);
			
			
			for(StudentUploadAttendance sta: studentUploadAttendanceList){
				Student s = new Student();
				BeanUtils.copyProperties(s, sta);
				student.add(s);
			}
			
			studentService.bulkupdateStudentAttendance(student);	
		}
		
	}

	public static File getServerFile(MultipartFile file, HttpServletRequest request) {
		System.out.println("getServerFile method calledddd");
		if (file.isEmpty() || file == null) {
			// model.put("msg", "failed to upload file because its empty");
			// throw new IOException();
		}

		String rootPath = request.getSession().getServletContext().getRealPath("/");
		File dir = new File(rootPath + File.separator + "uploadedfile");
		if (!dir.exists()) {
			dir.mkdirs();
		}

		File serverFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());

		try {
			try (InputStream is = file.getInputStream();
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile))) {
				int i;
				// write file to server
				while ((i = is.read()) != -1) {
					stream.write(i);
				}
				stream.flush();
			}
		} catch (IOException e) {
			// model.put("msg", "failed to process file because : " +
			// e.getMessage());
			// throw new IOException();
		}
	return serverFile;
	}
	
	
}
