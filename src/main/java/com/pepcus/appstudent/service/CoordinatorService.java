package com.pepcus.appstudent.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pepcus.appstudent.convertor.CoordinatorEntityConvertor;
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
	
	
	/**
	 * Method to get Coordinator entity
	 * @param firstName
	 * @param lastName
	 * @param primaryContactNumber
	 * @param dob
	 * @return
	 */
	public List<Coordinator> getCoordinatorEntityList(String firstName, String lastName, String primaryContactNumber, String dob) {
		return coordinatorRepository.findAll(CoordinatorSpecification.getCoordinators(firstName, lastName, primaryContactNumber, dob));
	}
	
	/**
	 * Method to get Coordinator entity for id
	 * @param id
	 * @return
	 */
	private Coordinator getCoordinatorEntity(Integer id) {
		Coordinator coordinator = coordinatorRepository.findById(id);
		if (coordinator == null) {
			throw new BadRequestException(ErrorMessageConstants.INVALID_ID + " {" + id + "}");
		}
		return coordinator;
	}
	

	/**
	 * Method to create/register new Coordinator
	 * 
	 * @param request
	 */
	public Coordinator createCoordinator(Coordinator request) throws ParseException {
		List<Coordinator> coordinators = getCoordinatorEntityList(request.getFirstName(), request.getLastName(), null , request.getDob());
		if(CollectionUtils.isNotEmpty(coordinators)) {
			Integer duplicateCoordinatorId = coordinators.stream().findFirst().get().getId();
			throw new BadRequestException(
					ErrorMessageConstants.ALREADY_REGISTRATION + "{" + duplicateCoordinatorId + "}", 1000);
		}
		CoordinatorEntityConvertor.convertCoordinatorEntity(request);
		String secretKey = CommonUtil.generateSecretKey();
		request.setSecretKey(secretKey);
		
		Coordinator coordinator =  coordinatorRepository.save(request);
		if (smsService.isSMSFlagEnabled(ApplicationConstants.SMS_CREATE)) {
			//SMSUtil.sendSMStoCoordinator(coordinator);
		}
		return CoordinatorEntityConvertor.setDepartmentsInCoordinator(coordinator);
	}
	
	/**
	 * Method to update coordinator
	 * @param id
	 * @param coordinatorRequest
	 * @return
	 * @throws ParseException
	 */
	public Coordinator updateCoordinator(Integer id, Coordinator coordinatorRequest) throws ParseException {
		Coordinator coordinator = getCoordinatorEntity(id);
		CoordinatorEntityConvertor.convertCoordinatorEntity(coordinatorRequest);
		CoordinatorEntityConvertor.convertCoordinatorEntity(coordinator, coordinatorRequest);
		coordinator = coordinatorRepository.save(coordinator);
		return CoordinatorEntityConvertor.setDepartmentsInCoordinator(coordinator);
	}


	
	/**
	 * Method to get coordinator for id
	 * @param id
	 * @return
	 */
	public Coordinator getCoordinator(Integer id) {
		Coordinator coordinator = getCoordinatorEntity(id);
		return CoordinatorEntityConvertor.setDepartmentsInCoordinator(coordinator);
	}

	/**
	 * Method to get coordinator
	 * @param firstName
	 * @param lastName
	 * @param primaryContactNumber
	 * @param dob
	 * @return
	 */
	public List<Coordinator> getCoordinators(String firstName, String lastName, String primaryContactNumber, String dob) {
		List<Coordinator> coordinators = getCoordinatorEntityList(firstName, lastName, primaryContactNumber, dob);
		return CoordinatorEntityConvertor.setDepartmentsInCoordinators(coordinators);
	}

	/**
	 * Method to delete coordinator
	 * @param id
	 * @return
	 */
	public boolean deleteCoordinator(Integer id) {
		Coordinator coordinator = getCoordinatorEntity(id);
		try {
			coordinatorRepository.delete(coordinator);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	
	/**
	 * Method to upload & save coordinators 
	 * @param file
	 * @return
	 * @throws ParseException
	 */
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
			CoordinatorEntityConvertor.convertCoordinatorEntity(coordinator);
			try {
				CoordinatorValidation.validateCreateCoordinatorRequest(coordinator);
				List<Coordinator> duplicateCoordinator = getCoordinatorEntityList(coordinator.getFirstName(),
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
			//SMSUtil.sendSMStoCoordinator(coordinatorDB);
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
