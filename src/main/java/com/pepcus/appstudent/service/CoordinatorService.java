package com.pepcus.appstudent.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pepcus.appstudent.entity.Coordinator;
import com.pepcus.appstudent.exception.BadRequestException;
import com.pepcus.appstudent.repository.CoordinatorRepository;
import com.pepcus.appstudent.util.ErrorMessageConstants;

@Service
public class CoordinatorService {

	@Autowired
	CoordinatorRepository coordinatorRepository;

	public Coordinator createCoordinator(Coordinator coordinator) throws ParseException {
		tranformRequest(coordinator);
		coordinator = coordinatorRepository.save(coordinator);
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

	public List<Coordinator> getAll() {
		List<Coordinator> coordinators = coordinatorRepository.findAll();
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
		if (coordinator.getDob() != null) {
			String stringDate = coordinator.getDob();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date date = dateFormat.parse(stringDate);
			DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String strDate = dateFormat1.format(date);
			coordinator.setDob(strDate);
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

	public String dateFormatForJsonResponse(String stringDate) {
		String responseDate = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = dateFormat.parse(stringDate);
			DateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
			responseDate = dateFormat1.format(date);
			return responseDate;
		} catch (ParseException e) {
			return responseDate;
		}

	}

	public void transformResponse(Coordinator coordinator) {
		if (StringUtils.isNotEmpty(coordinator.getAssignedDepartment())) {
			coordinator.setAssignedDepartments(Arrays.asList(coordinator.getAssignedDepartment().split(",")));
		}
		if (StringUtils.isNotEmpty(coordinator.getIntrestedDepartment())) {
			coordinator.setIntrestedDepartments(Arrays.asList(coordinator.getIntrestedDepartment().split(",")));
		}
		coordinator.setDob(dateFormatForJsonResponse(coordinator.getDob()));
	}
}
