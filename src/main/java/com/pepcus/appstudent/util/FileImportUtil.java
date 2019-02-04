package com.pepcus.appstudent.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanFilter;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.pepcus.appstudent.entity.StudentUploadAttendance;
import com.pepcus.appstudent.exception.*;

@Component
public class FileImportUtil implements CsvToBeanFilter {

	@Override
	public boolean allowLine(String[] line) {
		Arrays.stream(line).map(String::trim).toArray(unused -> line);
		if (ArrayUtils.getLength(line)<=1) {
			return false;
		}else {
			return true;
		}
	}

	public static List<StudentUploadAttendance> convertToStudentCSVBean(MultipartFile file, String flag) {
		try {
			
			 // Validate if file has valid extension
	        if (!FilenameUtils.isExtension(file.getOriginalFilename(),"csv")) {
	        	throw new ApplicationException("Invalid file..! Please upload csv file only ");
	        }
			if (file == null) {
				throw new ApplicationException("File not found..! Please select a file");
			}
			
			
			BufferedReader br = null;
			BufferedReader brFileContent=null;
			brFileContent = new BufferedReader(new InputStreamReader(file.getInputStream()));
			br=new BufferedReader(new InputStreamReader(file.getInputStream()));
			
			List<String> fileContents = br.lines().collect(Collectors.toList());
			if (fileContents == null || fileContents.isEmpty() || fileContents.size() < 2) {
				throw new ApplicationException("There is no Record in the file");
			}
			
			String headerLine = fileContents.get(0);
			String[] headers = headerLine.split(",");
			if (checkHeaders(headers, flag)) {
				List<StudentUploadAttendance> studentUploadAttendanceList = new ArrayList<StudentUploadAttendance>();
				HeaderColumnNameMappingStrategy<StudentUploadAttendance> strategy = new HeaderColumnNameMappingStrategy<StudentUploadAttendance>();
				strategy.setType(StudentUploadAttendance.class);
				CsvToBean<StudentUploadAttendance> csvToBean = new CsvToBean<StudentUploadAttendance>();
				CsvToBeanFilter filter = new FileImportUtil();
				studentUploadAttendanceList = csvToBean.parse(strategy, brFileContent, filter);
				br.close();
				brFileContent.close();
				return studentUploadAttendanceList;

			} else {
				throw new ApplicationException("Headers in CSV File are not correct");
			}

		} catch (NullPointerException | IOException e) {
			throw new ApplicationException("Your file is not valid");
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

	public static boolean checkHeaders(String headers[], String flag) {
		boolean result = false;
		if (flag.equalsIgnoreCase("optin")) {
			result = checkoptInHeaders(headers);
		} else if (flag.equalsIgnoreCase("attendance")) {
			result = checkAttendanceHeaders(headers);
		}
		return result;
	}
}
