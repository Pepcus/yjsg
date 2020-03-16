package com.pepcus.appstudent.convertor;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.pepcus.appstudent.entity.Coordinator;

public class CoordinatorEntityConvertor {

	public static void convertCoordinatorEntity(Coordinator coordinator) throws ParseException {
		if (CollectionUtils.isNotEmpty(coordinator.getAssignedDepartments())) {
			coordinator.setAssignedDepartment(StringUtils.join(coordinator.getAssignedDepartments(), ","));
		}
		if (CollectionUtils.isNotEmpty(coordinator.getInterestedDepartments())) {
			coordinator.setInterestedDepartment(StringUtils.join(coordinator.getInterestedDepartments(), ","));
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

	public static Coordinator convertCoordinatorEntity(Coordinator coordinator, Coordinator coordinatorRequest) {
		
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

		if (StringUtils.isNotEmpty(coordinatorRequest.getInterestedDepartment())) {
			coordinator.setInterestedDepartment(coordinatorRequest.getInterestedDepartment());
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
	
	
	public static List<Coordinator> setDepartmentsInCoordinators(List<Coordinator> coordinators) {
		if (coordinators != null && !coordinators.isEmpty()) {
			for (Coordinator coordinator : coordinators) {
				setDepartmentsInCoordinator(coordinator);
			}
		}
		return coordinators;
	}
	
	
	public static Coordinator setDepartmentsInCoordinator(Coordinator coordinator) {
		if (StringUtils.isNotEmpty(coordinator.getAssignedDepartment())) {
			coordinator.setAssignedDepartments(Arrays.asList(coordinator.getAssignedDepartment().split(",")));
		}
		if (StringUtils.isNotEmpty(coordinator.getInterestedDepartment())) {
			coordinator.setInterestedDepartments(Arrays.asList(coordinator.getInterestedDepartment().split(",")));
		}
		return coordinator;
	}
}
