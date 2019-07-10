package com.pepcus.appstudent.util;

import static com.pepcus.appstudent.util.ApplicationConstants.ATTENDANCE;
import static com.pepcus.appstudent.util.ApplicationConstants.ATTENDANCE_REQUIRED_HEADERS;
import static com.pepcus.appstudent.util.ApplicationConstants.COMMA_SEPARATOR;
import static com.pepcus.appstudent.util.ApplicationConstants.EMPTY_FILE;
import static com.pepcus.appstudent.util.ApplicationConstants.FILE_NOT_FOUND;
import static com.pepcus.appstudent.util.ApplicationConstants.HEADERS;
import static com.pepcus.appstudent.util.ApplicationConstants.INVALID_FILE_FORMAT;
import static com.pepcus.appstudent.util.ApplicationConstants.OPTIN;
import static com.pepcus.appstudent.util.ApplicationConstants.OPTIN_REQUIRED_HEADERS;
import static com.pepcus.appstudent.util.ApplicationConstants.REQUIRED_HEADERS;
import static com.pepcus.appstudent.util.ApplicationConstants.UNABLE_TO_READ_CSV;
import static com.pepcus.appstudent.util.ApplicationConstants.VALID_FILE_EXTENSION_IMPORT;
import static com.pepcus.appstudent.util.ApplicationConstants.FAILED_TO_GENERATE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
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

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanFilter;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.pepcus.appstudent.entity.StudentWrapper;
import com.pepcus.appstudent.exception.ApplicationException;
import com.pepcus.appstudent.exception.BadRequestException;

/**
 * Class is specific to keep utility methods for file import
 * 
 * @author Rahul.Panwar
 *
 */
@Component
public class FileImportUtil implements CsvToBeanFilter {

    @Override
    public boolean allowLine(String[] line) {
        Arrays.stream(line).map(String::trim).toArray(unused -> line);
        if (ArrayUtils.getLength(line) <= 1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Used to convert into StudentUploadAttendence bean
     * 
     * @return studentUploadAttendanceList
     */
    public static List<StudentWrapper> convertToStudentCSVBean(MultipartFile file, String flag) {
        List<StudentWrapper> studentUploadAttendanceList = null;
        BufferedReader brFileContent = null;
        try {
            FileImportUtil.validateFile(file, flag);
            brFileContent = new BufferedReader(new InputStreamReader(file.getInputStream()));
            studentUploadAttendanceList = new ArrayList<StudentWrapper>();
            HeaderColumnNameMappingStrategy<StudentWrapper> strategy = new HeaderColumnNameMappingStrategy<StudentWrapper>();
            strategy.setType(StudentWrapper.class);
            CsvToBean<StudentWrapper> csvToBean = new CsvToBean<StudentWrapper>();
            CsvToBeanFilter filter = new FileImportUtil();
            studentUploadAttendanceList = csvToBean.parse(strategy, brFileContent, filter);
            brFileContent.close();
        } catch (IOException e) {
            throw new BadRequestException(UNABLE_TO_READ_CSV);
        }
        return studentUploadAttendanceList;

    }

    /**
     * Used to check Attendance Headers,whether required headers in CSV file
     * present or not
     * 
     * @return boolean
     */
    private static boolean checkAttendanceHeaders(String[] headersInFile) {
        List<String> required = new ArrayList<String>();
        String[] requiredHeaders = ATTENDANCE_REQUIRED_HEADERS;
        required = Arrays.asList(requiredHeaders);
        for (String header : headersInFile) {
            if (!required.contains(header)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Used to check opt headers,whether required headers in CSV file
     * present or not
     * 
     * @return boolean
     */
    private static boolean checkoptInHeaders(String[] headersInFile) {
        List<String> required = new ArrayList<String>();
        String[] requiredHeaders = OPTIN_REQUIRED_HEADERS;
        required = Arrays.asList(requiredHeaders);
        for (String header : headersInFile) {
            if (!required.contains(header)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Used to check headers,whether required headers in CSV file present or not
     * 
     * @return boolean
     */
    private static boolean checkHeaders(String headers[], String flag) {
        boolean result = false;
        switch (flag) {
        case OPTIN:
            result = checkoptInHeaders(headers);
            break;

        case ATTENDANCE:
            result = checkAttendanceHeaders(headers);
            break;

        default:
            result = checkHeaders(headers);
            break;
        }

        return result;
    }

    /**
     * Used to get CSVData from multipart file and validate the file
     * 
     * @return completeRecord
     */
    public static List<String> getCSVData(MultipartFile file) {
        List<String> completeRecord = new ArrayList<>();

        if (file == null || file.getSize() < 0) {
            throw new BadRequestException(FILE_NOT_FOUND);
        }
        // Validate if file has valid extension
        if (!FilenameUtils.isExtension(file.getOriginalFilename(), VALID_FILE_EXTENSION_IMPORT)) {
            throw new BadRequestException(INVALID_FILE_FORMAT);
        }
        String line = "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            while ((line = br.readLine()) != null) {
                String singleRecord[] = line.trim().split(COMMA_SEPARATOR);
                if (singleRecord != null && singleRecord.length > 2 && !singleRecord.equals("")) {
                    completeRecord.add(line);
                }
            }
        } catch (IOException e) {
            throw new BadRequestException(UNABLE_TO_READ_CSV);
        }
        return completeRecord;
    }

    /**
     * Used to get duplicate csv data and generate new csv file containing
     * duplicate data
     * 
     * @return duplicateCSVData
     */
    public static File getDuplicateDataCSV(String[] headers, List<String[]> finalDuplicateList) {
        File duplicateCSVData = new File("StudentDuplicateData.csv");
        FileWriter outputfile;
        try {
            outputfile = new FileWriter(duplicateCSVData);
            CSVWriter csvWriter = new CSVWriter(outputfile);
            csvWriter.writeNext(headers);
            csvWriter.writeAll(finalDuplicateList);
            csvWriter.close();
        } catch (IOException e) {
            throw new BadRequestException(FAILED_TO_GENERATE);
        }
        return duplicateCSVData;
    }

    /**
     * Used to check headers,whether required headers in CSV file present or not
     * 
     * @return boolean
     */
    private static boolean checkHeaders(String[] headersInFile) {
        List<String> required = Arrays.asList(HEADERS);
        List<String> invalidHeaders = new ArrayList<String>();
        for (String header : headersInFile) {
            if (!required.contains(header)) {
                invalidHeaders.add(header);
            }
        }
        if (invalidHeaders.size() > 0 || !invalidHeaders.isEmpty()) {
            throw new BadRequestException("Columns" + invalidHeaders
                    + "didn’t match with database columns, Valid Headers: " + Arrays.toString(HEADERS));
        }
        return true;
    }

    /**
     * Used to validate file
     * 
     * @param file
     * @param flag
     */
    private static void validateFile(MultipartFile file, String flag) {

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

            if (!checkHeaders(headers, flag)) {
                if (flag.equalsIgnoreCase(OPTIN)) {
                    throw new BadRequestException(REQUIRED_HEADERS + Arrays.toString(OPTIN_REQUIRED_HEADERS));
                } else {
                    throw new BadRequestException(REQUIRED_HEADERS + Arrays.toString(ATTENDANCE_REQUIRED_HEADERS));
                }
            }
        } catch (IOException e) {
            throw new BadRequestException(UNABLE_TO_READ_CSV);
        }
    }

}