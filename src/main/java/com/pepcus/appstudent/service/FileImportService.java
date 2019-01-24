/*package com.pepcus.appstudent.service;

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

import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.collections.CollectionUtils;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.util.NullableWrapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.pepcus.appstudent.entity.Student;
import com.pepcus.appstudent.entity.StudentUploadAttendance;
import com.pepcus.appstudent.exception.BadRequestException;
import com.pepcus.appstudent.repository.StudentRepository;

@Service
public class FileImportService {

	@Autowired
	private StudentService studentService;
	
	@Autowired
	private StudentRepository studentRepository;
	
	public void uploadStudentAttendance(MultipartFile file, HttpServletRequest request) throws IOException, IllegalAccessException, InvocationTargetException{
		File serverFile=getServerFile(file, request);
		List<StudentUploadAttendance> studentUploadAttendanceList = new ArrayList<StudentUploadAttendance>();
		try (Reader reader = Files.newBufferedReader(Paths.get(serverFile.getAbsolutePath()));) {
			HeaderColumnNameMappingStrategy<StudentUploadAttendance> strategy = new HeaderColumnNameMappingStrategy<StudentUploadAttendance>();
			strategy.setType(StudentUploadAttendance.class);			
			CsvToBean<StudentUploadAttendance> csvToBean = new CsvToBean<StudentUploadAttendance>();
			studentUploadAttendanceList = csvToBean.parse(strategy, reader);
			studentService.updateStudentAttendance(studentUploadAttendanceList);	
		}
	}
// spend some time
	public static File getServerFile(MultipartFile file, HttpServletRequest request) {
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
	
	private List<Student> validateListStudent(List<Integer> ids) {
		List<Student> students = studentRepository.findByIdIn(ids);
		if (null == students) {
			throw new BadRequestException("student not found by studentId=" + ids);
		}
		return students;
	}
}
*/