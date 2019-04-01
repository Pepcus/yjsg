package com.pepcus.appstudent.util;

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
import com.pepcus.appstudent.entity.StudentUploadAttendance;
import com.pepcus.appstudent.exception.ApplicationException;
import com.pepcus.appstudent.exception.BadRequestException;

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
     * @return studentUploadAttendanceList
     */
    public static List<StudentUploadAttendance> convertToStudentCSVBean(MultipartFile file, String flag) {
        try {

            // Validate if file has valid extension
            if (!FilenameUtils.isExtension(file.getOriginalFilename(),
                    ApplicationConstants.VALID_FILE_EXTENSION_IMPORT)) {
                throw new BadRequestException("Upload is supported only for 'CSV' data files");
            }
            if (file == null || file.getSize() < 0) {
                throw new BadRequestException("File not found..! Please select a file");
            }

            BufferedReader br = null;
            BufferedReader brFileContent = null;
            brFileContent = new BufferedReader(new InputStreamReader(file.getInputStream()));
            br = new BufferedReader(new InputStreamReader(file.getInputStream()));

            List<String> fileContents = br.lines().collect(Collectors.toList());
            if (fileContents == null || fileContents.isEmpty() || fileContents.size() < 2) {
                throw new ApplicationException("There is no Record in the file");
            }

            String headerLine = fileContents.get(0);
            String[] headers = headerLine.split(ApplicationConstants.COMMA_SEPARATOR);
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
                if (flag.equals(ApplicationConstants.OPTIN)) {
                    throw new BadRequestException("Uploaded file should contain column as '"
                            + Arrays.toString(ApplicationConstants.OPTIN_REQUIRED_HEADERS) + "'");
                } else {
                    throw new BadRequestException("Uploaded file should contain column as '"
                            + Arrays.toString(ApplicationConstants.ATTENDANCE_REQUIRED_HEADERS) + "'");
                }
            }
        } catch (NullPointerException | IOException e) {
            throw new BadRequestException("Please select a file or your file is not valid file");
        }
    }

    /**
     * Used to check Attendance Headers,whether required headers in CSV file present or not 
     * @return boolean
     */
    private static boolean checkAttendanceHeaders(String[] headersInFile) {
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

    /**
     * Used to check opt headers,whether required headers in CSV file present or not 
     * @return boolean
     */
    private static boolean checkoptInHeaders(String[] headersInFile) {
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

    /**
     * Used to check headers,whether required headers in CSV file present or not 
     * @return boolean
     */
    private static boolean checkHeaders(String headers[], String flag) {
        boolean result = false;
        if (flag.equalsIgnoreCase(ApplicationConstants.OPTIN)) {
            result = checkoptInHeaders(headers);
        } else if (flag.equalsIgnoreCase(ApplicationConstants.ATTENDANCE)) {
            result = checkAttendanceHeaders(headers);
        }
        return result;
    }

    /**
     * Used to get CSVData from multipart file and validate the file 
     * @return completeRecord
     */
    public static List<String> getCSVData(MultipartFile file) {
        List<String> completeRecord = new ArrayList<>();

        // Validate if file has valid extension
        if (!FilenameUtils.isExtension(file.getOriginalFilename(), ApplicationConstants.VALID_FILE_EXTENSION_IMPORT)) {
            throw new BadRequestException("Invalid file..! Please upload csv file only ");
        }
        if (file == null || file.getSize() < 0) {
            throw new BadRequestException("File not found..! Please select a file");
        }
        String line = "";
        try {
            BufferedReader br = null;
            br = new BufferedReader(new InputStreamReader(file.getInputStream()));
            while ((line = br.readLine()) != null) {
                String singleRecord[] = line.trim().split(ApplicationConstants.COMMA_SEPARATOR);
                if (singleRecord != null && singleRecord.length > 2 && !singleRecord.equals("")) {
                    completeRecord.add(line);
                }
            }
        } catch (IOException e) {
            throw new BadRequestException("File is not correct, Not able to read data");
        }
        return completeRecord;
    }

    /**
     * Used to get duplicate csv data and generate new csv file containing duplicate data  
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
            throw new ApplicationException("Failed to write duplicate data");
        }
        return duplicateCSVData;
    }

}