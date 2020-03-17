package com.pepcus.appstudent.util;

import static com.pepcus.appstudent.util.ApplicationConstants.COMMA_SEPARATOR;
import static com.pepcus.appstudent.util.ApplicationConstants.EMPTY_FILE;
import static com.pepcus.appstudent.util.ApplicationConstants.FILE_NOT_FOUND;
import static com.pepcus.appstudent.util.ApplicationConstants.INVALID_FILE_FORMAT;
import static com.pepcus.appstudent.util.ApplicationConstants.REQUIRED_HEADERS;
import static com.pepcus.appstudent.util.ApplicationConstants.UNABLE_TO_READ_CSV;
import static com.pepcus.appstudent.util.ApplicationConstants.VALID_FILE_EXTENSION_IMPORT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanFilter;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.pepcus.appstudent.exception.BadRequestException;

public class CsvFileUtil {
	
	/**
	 * Method to convert CSV file to List of Object
	 * @param file
	 * @param flag
	 * @param objectClass
	 * @param expectedHeaders
	 * @return
	 */
	public static <T> List<T> convertToCsvBean(MultipartFile file, String flag, Class<?> objectClass,
			String[] expectedHeaders) {
		List<T> requestedObjectList = null;
		BufferedReader brFileContent = null;
		try {
			validateFile(file, flag, expectedHeaders, false);
			brFileContent = new BufferedReader(new InputStreamReader(file.getInputStream()));
			requestedObjectList = new ArrayList<T>();
			HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<T>();
			strategy.setType((Class<? extends T>) objectClass);
			CsvToBean<T> csvToBean = new CsvToBean<T>();
			CsvToBeanFilter filter = new FileImportUtil();
			requestedObjectList = csvToBean.parse(strategy, brFileContent, filter);
			brFileContent.close();
		} catch (IOException e) {
			throw new BadRequestException(UNABLE_TO_READ_CSV);
		}
		return requestedObjectList;
	}

	/**
	 * Method to validate Csv file
	 * @param file
	 * @param flag
	 * @param expectedHeaders
	 * @param validateHeader
	 */
	private static void validateFile(MultipartFile file, String flag, String[] expectedHeaders,
			boolean validateHeader) {
		if (file == null || file.getSize() < 0) {
			throw new BadRequestException(FILE_NOT_FOUND);
		}
		if (!FilenameUtils.isExtension(file.getOriginalFilename(), VALID_FILE_EXTENSION_IMPORT)) {
			throw new BadRequestException(INVALID_FILE_FORMAT);
		}

		try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
			List<String> fileContents = br.lines().collect(Collectors.toList());
			if (fileContents == null || fileContents.isEmpty() || fileContents.size() < 2) {
				throw new BadRequestException(EMPTY_FILE);
			}
			String headerLine = fileContents.get(0);
			String[] headers = headerLine.split(COMMA_SEPARATOR);

			if (validateHeader && !checkHeaders(expectedHeaders, headers, flag)) {
					throw new BadRequestException(REQUIRED_HEADERS + Arrays.toString(expectedHeaders));
			}

		} catch (IOException e) {
			throw new BadRequestException(UNABLE_TO_READ_CSV);
		}
	}

	/**
	 * Method to check Header
	 * @param expectedHeaders
	 * @param headers
	 * @param flag
	 * @return
	 */
	private static boolean checkHeaders(String[] expectedHeaders, String[] headers, String flag) {
		boolean result = false;
		switch (flag) {
		default:
			result = checkExpectedHeaders(expectedHeaders, headers);
			break;
		}
		return result;
	}

	/**
	 * Method to validate expected headers
	 * @param expectedHeaders
	 * @param headersInFile
	 * @return
	 */
	private static boolean checkExpectedHeaders(String[] expectedHeaders, String[] headersInFile) {
		List<String> required = new ArrayList<String>();
		required = Arrays.asList(expectedHeaders);
		for (String header : headersInFile) {
			if (!required.contains(header)) {
				return false;
			}
		}
		return true;
	}

	
}