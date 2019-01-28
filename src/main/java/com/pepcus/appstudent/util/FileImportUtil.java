package com.pepcus.appstudent.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.pepcus.appstudent.entity.StudentUploadAttendance;
import com.pepcus.appstudent.service.StudentService;

import lombok.extern.apachecommons.CommonsLog;

import com.pepcus.appstudent.exception.*;

@Component
public class FileImportUtil {

	public static List<StudentUploadAttendance> convertToStudentCSVBean(MultipartFile file,String flag) {
		try {
			if (file == null) {
				ApiErrorResponse errorResponse=new ApiErrorResponse();
				//errorResponse.setMessage(message);
				errorResponse.setMessage("File not found");
				errorResponse.setStatus(1010);
				throw ApplicationException.createBadRequest(APIErrorCodes.NO_RECORDS_FOUND_FOR_IMPORT,"File not found");
			}

			Reader reader = null;
			BufferedReader br = null;
			reader = new InputStreamReader(file.getInputStream());
			br = new BufferedReader(new InputStreamReader(file.getInputStream()));
			List<String> fileContents = br.lines().collect(Collectors.toList());
			String headerLine = fileContents.get(0);
			String[] headers = headerLine.split(",");
			if (fileContents == null || fileContents.isEmpty() || fileContents.size() < 2) {
				throw new ApplicationException("There is no Record in the file");
			}
			
			if (checkHeaders(headers,flag)) {
				List<StudentUploadAttendance> studentUploadAttendanceList = new ArrayList<StudentUploadAttendance>();
				HeaderColumnNameMappingStrategy<StudentUploadAttendance> strategy = new HeaderColumnNameMappingStrategy<StudentUploadAttendance>();
				strategy.setType(StudentUploadAttendance.class);
				CsvToBean<StudentUploadAttendance> csvToBean = new CsvToBean<StudentUploadAttendance>();
				studentUploadAttendanceList = csvToBean.parse(strategy, reader);
				return studentUploadAttendanceList;
			}else {
				throw new ApplicationException("Headers in CSV File are not correct");
			}
		} catch (IOException | NullPointerException e) {
			throw new ApplicationException("YOUR FILE IS NOT VALID");
		}
	}

	public static boolean checkAttendanceHeaders(String[] headersInFile) {
		List<String> required = new ArrayList<String>();
		String[] requiredHeaders = ApplicationConstants.ATTENDANCE_REQUIRED_HEADERS;
		required = Arrays.asList(requiredHeaders);
		for (String header : headersInFile) {
			if (!required.contains(header)) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean checkoptInHeaders(String[] headersInFile) {
		List<String> required = new ArrayList<String>();
		String[] requiredHeaders = ApplicationConstants.OPTIN_REQUIRED_HEADERS;
		required = Arrays.asList(requiredHeaders);
		for (String header : headersInFile) {
			if (!required.contains(header)) {
				return false;
			}
		}
		return true;
	}

	
	public static boolean checkHeaders(String headers[],String flag){
		boolean result=false;
		if(flag.equalsIgnoreCase("optin")){
			result=checkoptInHeaders(headers);
		}
		else if(flag.equalsIgnoreCase("attendance")){
			result=checkAttendanceHeaders(headers);
		}
		return result;
	}
}
