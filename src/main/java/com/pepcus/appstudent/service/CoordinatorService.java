package com.pepcus.appstudent.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.pepcus.appstudent.entity.Coordinator;
import com.pepcus.appstudent.entity.WrapperCoordinator;
import com.pepcus.appstudent.exception.BadRequestException;
import com.pepcus.appstudent.repository.CoordinatorRepository;
import com.pepcus.appstudent.response.ApiResponse;
import com.pepcus.appstudent.specifications.CoordinatorSpecification;
import com.pepcus.appstudent.util.ApplicationConstants;
import com.pepcus.appstudent.util.CommonUtil;
import com.pepcus.appstudent.util.ErrorMessageConstants;
import com.pepcus.appstudent.util.SMSUtil;
import com.pepcus.appstudent.validation.CoordinatorValidation;

@Service
public class CoordinatorService {

	@Autowired
	CoordinatorRepository coordinatorRepository;
	
	@Autowired
	SmsService smsService;

	public Coordinator createCoordinator(Coordinator coordinator) throws ParseException {
		List<Coordinator> coordinators = getCoordinatorList(coordinator.getFirstName(), coordinator.getLastName(), null , coordinator.getDob());
		if(CollectionUtils.isNotEmpty(coordinators)) {
			Integer duplicateCoordinatorId = coordinators.stream().findFirst().get().getId();
			throw new BadRequestException(
					ErrorMessageConstants.ALREADY_REGISTRATION + "{" + duplicateCoordinatorId + "}", 1000);
		}
		tranformRequest(coordinator);
		coordinator = saveAndSendSMStoCoordinate(coordinator);
		transformResponse(coordinator);
		return coordinator;
	}

	public Coordinator getCoordinator(Integer id) {
		Coordinator coordinator = coordinatorRepository.findById(id);
		if (coordinator == null) {
			throw new BadRequestException(ErrorMessageConstants.INVALID_ID + " {" + id + "}");
		}
		transformResponse(coordinator);
		return coordinator;
	}

	public List<Coordinator> getCoordinators(String firstName, String lastName, String primaryContactNumber, String dob) {
		List<Coordinator> coordinators = getCoordinatorList(firstName, lastName, primaryContactNumber, dob);
		if (CollectionUtils.isNotEmpty(coordinators)) {
			for (Coordinator coordinator : coordinators) {
				transformResponse(coordinator);
			}
		}
		return coordinators;
	}

	public Coordinator deleteCoordinator(Integer id) {
		Coordinator coordinator = coordinatorRepository.findById(id);
		if (coordinator == null) {
			throw new BadRequestException(ErrorMessageConstants.INVALID_ID + " {" + id + "}");
		}

		coordinatorRepository.delete(coordinator);
		return coordinator;
	}

	public Coordinator updateCoordinator(Integer id, Coordinator coordinatorRequest) throws ParseException {
		Coordinator coordinator = coordinatorRepository.findById(id);
		if (coordinator == null) {
			throw new BadRequestException(ErrorMessageConstants.INVALID_ID + " {" + id + "}");
		}
		tranformRequest(coordinatorRequest);
		ConvertToEntity(coordinator, coordinatorRequest);
		coordinator = coordinatorRepository.save(coordinator);
		transformResponse(coordinator);
		return coordinator;
	}

	public void tranformRequest(Coordinator coordinator) throws ParseException {
		if (CollectionUtils.isNotEmpty(coordinator.getAssignedDepartments())) {
			coordinator.setAssignedDepartment(StringUtils.join(coordinator.getAssignedDepartments(), ","));
		}
		if (CollectionUtils.isNotEmpty(coordinator.getIntrestedDepartments())) {
			coordinator.setIntrestedDepartment(StringUtils.join(coordinator.getIntrestedDepartments(), ","));
		}

		if (StringUtils.isNotEmpty(coordinator.getFirstName())) {
			String firstName = coordinator.getFirstName().toLowerCase();
			String firstLetter = firstName.substring(0, 1);
			coordinator.setFirstName(firstName.replaceFirst("[" + firstLetter + "]", firstLetter.toUpperCase()));

		}
		if (StringUtils.isNotEmpty(coordinator.getLastName())) {
			String lastName = coordinator.getLastName().toLowerCase();
			String lastLetter = coordinator.getLastName().substring(0, 1);
			coordinator.setLastName(lastName.replaceFirst("[" + lastLetter + "]", lastLetter.toUpperCase()));
		}
	}

	public Coordinator ConvertToEntity(Coordinator coordinator, Coordinator coordinatorRequest) {
		
		if (StringUtils.isNotEmpty(coordinatorRequest.getRemarks())) {
			coordinator.setRemarks(coordinatorRequest.getRemarks());
		}
		if (StringUtils.isNotEmpty(coordinatorRequest.getAddress())) {
			coordinator.setAddress(coordinatorRequest.getAddress());
		}
		if (coordinatorRequest.getIsPrimaryNumberOnWhatsapp() != null) {
			coordinator.setIsPrimaryNumberOnWhatsapp(coordinatorRequest.getIsPrimaryNumberOnWhatsapp());
		}
		if (StringUtils.isNotEmpty(coordinatorRequest.getAssignedDepartment())) {
			coordinator.setAssignedDepartment(coordinatorRequest.getAssignedDepartment());
		}

		if (StringUtils.isNotEmpty(coordinatorRequest.getIntrestedDepartment())) {
			coordinator.setIntrestedDepartment(coordinatorRequest.getIntrestedDepartment());
		}

		if (StringUtils.isNotEmpty(coordinatorRequest.getAlternateContactNumber())) {
			coordinator.setAlternateContactNumber(coordinatorRequest.getAlternateContactNumber());
		}

		if (StringUtils.isNotEmpty(coordinatorRequest.getPrimaryContactNumber())) {
			coordinator.setPrimaryContactNumber(coordinatorRequest.getPrimaryContactNumber());
		}

		if (StringUtils.isNotEmpty(coordinatorRequest.getArea())) {
			coordinator.setArea(coordinatorRequest.getArea());
		}

		if (coordinatorRequest.getBusNumber() != null) {
			coordinator.setBusNumber(coordinatorRequest.getBusNumber());
		}

		if (StringUtils.isNotEmpty(coordinatorRequest.getDob())) {
			coordinator.setDob(coordinatorRequest.getDob());
		}

		if (StringUtils.isNotEmpty(coordinatorRequest.getEmail())) {
			coordinator.setEmail(coordinatorRequest.getEmail());
		}

		if (StringUtils.isNotEmpty(coordinatorRequest.getFirstName())) {
			coordinator.setFirstName(coordinatorRequest.getFirstName());
		}

		if (StringUtils.isNotEmpty(coordinatorRequest.getLastName())) {
			coordinator.setLastName(coordinatorRequest.getLastName());
		}

		if (StringUtils.isNotEmpty(coordinatorRequest.getGender())) {
			coordinator.setGender(coordinatorRequest.getGender());
		}

		if (coordinatorRequest.getClassNumber() != null) {
			coordinator.setClassNumber(coordinatorRequest.getClassNumber());
		}

		if (coordinatorRequest.getIsActive() != null) {
			coordinator.setIsActive(coordinatorRequest.getIsActive());
		}
		return coordinator;
	}

	public void transformResponse(Coordinator coordinator) {
		if (StringUtils.isNotEmpty(coordinator.getAssignedDepartment())) {
			coordinator.setAssignedDepartments(Arrays.asList(coordinator.getAssignedDepartment().split(",")));
		}
		if (StringUtils.isNotEmpty(coordinator.getIntrestedDepartment())) {
			coordinator.setIntrestedDepartments(Arrays.asList(coordinator.getIntrestedDepartment().split(",")));
		}
	}
	
	public List<Coordinator> getCoordinatorList(String firstName, String lastName, String primaryContactNumber, String dob) {
		return coordinatorRepository.findAll(CoordinatorSpecification.getCoordinators(firstName, lastName, primaryContactNumber, dob));
	}
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public ApiResponse uploadCoordinators(MultipartFile file) throws ParseException {
		List<String> invalidRecordsInCSV = new ArrayList<String>();
		List<String> recordsAlreadyExist = new ArrayList<String>();
		List<Object> coordinators = CommonUtil.getBeanFromCSV(file, Coordinator.class);
		if (CollectionUtils.isEmpty(coordinators)) {
			throw new BadRequestException(ErrorMessageConstants.INVALID_CSV);
		}
		List<Coordinator> validCoordinatorRecords = new ArrayList<Coordinator>();
		for (int rowNumber = 0; rowNumber < coordinators.size(); rowNumber++) {
			Coordinator coordinator = (Coordinator) coordinators.get(rowNumber);
			tranformRequest(coordinator);
			try {
				CoordinatorValidation.validateCoordinatorRequest(coordinator);
				List<Coordinator> duplicateCoordinator = getCoordinatorList(coordinator.getFirstName(),
						coordinator.getLastName(), null, coordinator.getDob());
				if (CollectionUtils.isNotEmpty(duplicateCoordinator)) {
					recordsAlreadyExist.add("Row Number - "+rowNumber+ " Record already available with Id "+duplicateCoordinator.stream().findFirst().get().getId());
					continue;
				} else {
					validCoordinatorRecords.add(coordinator);
				}
			} catch (BadRequestException e) {
				invalidRecordsInCSV.add(e.getMessage() + " : Row Number - " + rowNumber);
				continue;
			}
		}
		//distict record if csv contain duplicate records
		List<Coordinator> coordinatorsList = validCoordinatorRecords.stream().map(WrapperCoordinator::new).distinct()
				.map(WrapperCoordinator::unwrapCoordinator).collect(Collectors.toList());
		List<Coordinator> successCoordinatorRecords = coordinatorsList.stream().map(coordinator -> saveAndSendSMStoCoordinate(coordinator)).collect(Collectors.toList());
		List<Integer> successRecordIds = successCoordinatorRecords.stream().map(coordinator -> coordinator.getId()).collect(Collectors.toList());
		return getBulkUploadApiResponse(successRecordIds, recordsAlreadyExist, invalidRecordsInCSV);
	}
	
	private Coordinator saveAndSendSMStoCoordinate(Coordinator coordinator) {
		String secretKey = CommonUtil.generateSecretKey();
		coordinator.setSecretKey(secretKey);
		
		Coordinator coordinatorDB =  coordinatorRepository.save(coordinator);
		if (smsService.isSMSFlagEnabled(ApplicationConstants.SMS_CREATE)) {
			SMSUtil.sendSMStoCoordinator(coordinatorDB);
		}
		return coordinatorDB;
	}
	
	private ApiResponse getBulkUploadApiResponse(List<Integer> successRecordIds, List<String> recordsAlreadyExist,
			List<String> invalidRecordsInCSV) {
		ApiResponse apiResponse = new ApiResponse();
		if (CollectionUtils.isNotEmpty(successRecordIds)) {
			apiResponse.setSuccessRecordIds(successRecordIds);
		}
		if (CollectionUtils.isNotEmpty(recordsAlreadyExist)) {
			apiResponse.setDuplicateRecords(recordsAlreadyExist);
		}
		if (CollectionUtils.isNotEmpty(invalidRecordsInCSV)) {
			apiResponse.setInvalidRecords(invalidRecordsInCSV);
		}
		return apiResponse;
	}

}
