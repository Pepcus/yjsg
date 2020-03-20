package com.pepcus.appstudent.convertor;

import static com.pepcus.appstudent.util.CommonUtil.convertDateToString;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.pepcus.appstudent.entity.Coordinator;
import com.pepcus.appstudent.entity.CoordinatorAssignedDepartment;
import com.pepcus.appstudent.entity.CoordinatorInterestedDepartment;
import com.pepcus.appstudent.entity.Department;
import com.pepcus.appstudent.util.ApplicationConstants;

public class CoordinatorEntityConvertor {

	public static Coordinator convertCoordinatorEntity(Coordinator request) {
		Coordinator coordinatorEntity = new Coordinator();

		Date currentDate = Calendar.getInstance().getTime();
		coordinatorEntity.setDateCreatedInDB(currentDate);
		coordinatorEntity.setDateLastModifiedInDB(currentDate);
		if (request.getIsActive() == null) {
			coordinatorEntity.setIsActive(ApplicationConstants.VAL_FALSE);
		}
		return convertCoordinatorEntity(coordinatorEntity, request);
	}

	public static Coordinator convertCoordinatorEntity(Coordinator coordinatorEntity, Coordinator request) {
		coordinatorEntity.setDateLastModifiedInDB(Calendar.getInstance().getTime());
		if (StringUtils.isNotEmpty(request.getFirstName())) {
			String firstName = request.getFirstName().toLowerCase();
			String firstLetter = firstName.substring(0, 1);
			coordinatorEntity.setFirstName(firstName.replaceFirst("[" + firstLetter + "]", firstLetter.toUpperCase()));
		}

		if (StringUtils.isNotEmpty(request.getLastName())) {
			String lastName = request.getLastName().toLowerCase();
			String lastLetter = lastName.substring(0, 1);
			coordinatorEntity.setLastName(lastName.replaceFirst("[" + lastLetter + "]", lastLetter.toUpperCase()));
		}

		if (StringUtils.isNotEmpty(request.getDob())) {
			coordinatorEntity.setDob(request.getDob());
		}

		if (StringUtils.isNotEmpty(request.getGender())) {
			coordinatorEntity.setGender(request.getGender());
		}

		if (StringUtils.isNotEmpty(request.getEmail())) {
			coordinatorEntity.setEmail(request.getEmail());
		}

		if (request.getWhatsappNumber() != null) {
			coordinatorEntity.setWhatsappNumber(request.getWhatsappNumber());
		}

		if (StringUtils.isNotEmpty(request.getAlternateNumber())) {
			coordinatorEntity.setAlternateNumber(request.getAlternateNumber());
		}

		if (StringUtils.isNotEmpty(request.getAddress())) {
			coordinatorEntity.setAddress(request.getAddress());
		}

		if (StringUtils.isNotEmpty(request.getArea())) {
			coordinatorEntity.setArea(request.getArea());
		}

		if (StringUtils.isNotEmpty(request.getRemarks())) {
			coordinatorEntity.setRemarks(request.getRemarks());
		}

		if (request.getIsActive() != null) {
			String isActive = request.getIsActive().equalsIgnoreCase(ApplicationConstants.VAL_TRUE)
					? ApplicationConstants.VAL_TRUE : ApplicationConstants.VAL_FALSE;
			coordinatorEntity.setIsActive(isActive);
		}

		return coordinatorEntity;
	}

	public static Set<CoordinatorInterestedDepartment> convertToCoordinatorInterestedDepartmentsEntitySet(
			Coordinator coordinatorEntity, Set<CoordinatorInterestedDepartment> requestedInterestedDepartmentList,
			Map<String, Department> departmentMap) {
		Set<CoordinatorInterestedDepartment> coordinatorInterestedDepartmentEntitySet = new HashSet<>();
		if (CollectionUtils.isNotEmpty(requestedInterestedDepartmentList)) {
			// convert requested list to entity list
			for (CoordinatorInterestedDepartment requestedInterestedDepartment : requestedInterestedDepartmentList) {
				coordinatorInterestedDepartmentEntitySet.add(convertToCoordinatorInterestedDepartmentEntity(
						coordinatorEntity, departmentMap, requestedInterestedDepartment));
			}
		}
		return coordinatorInterestedDepartmentEntitySet;
	}

	private static CoordinatorInterestedDepartment convertToCoordinatorInterestedDepartmentEntity(
			Coordinator coordinatorEntity, Map<String, Department> departmentMap,
			CoordinatorInterestedDepartment requestedInterestedDepartment) {

		CoordinatorInterestedDepartment coordinatorInterestedDepartmentEntity = new CoordinatorInterestedDepartment();
		coordinatorInterestedDepartmentEntity.setCoordinator(coordinatorEntity);
		coordinatorInterestedDepartmentEntity
				.setDepartment(departmentMap.get(requestedInterestedDepartment.getInternalName()));
		return coordinatorInterestedDepartmentEntity;
	}

	public static Set<CoordinatorAssignedDepartment> convertToCoordinatorAssignedDepartmentsEntitySet(
			Coordinator coordinatorEntity, Set<CoordinatorAssignedDepartment> requestedAssignedDepartmentList,
			Map<String, Department> departmentMap) {
		Set<CoordinatorAssignedDepartment> coordinatorAssignedDepartmentEntitySet = new HashSet<>();
		if (CollectionUtils.isNotEmpty(requestedAssignedDepartmentList)) {
			// convert requested list to entity list
			for (CoordinatorAssignedDepartment requestedAssignedDepartment : requestedAssignedDepartmentList) {
				coordinatorAssignedDepartmentEntitySet.add(convertToCoordinatorAssignedDepartmentEntity(
						coordinatorEntity, departmentMap, requestedAssignedDepartment));
			}
		}
		return coordinatorAssignedDepartmentEntitySet;
	}

	private static CoordinatorAssignedDepartment convertToCoordinatorAssignedDepartmentEntity(
			Coordinator coordinatorEntity, Map<String, Department> departmentMap,
			CoordinatorAssignedDepartment requestedAssignedDepartment) {

		CoordinatorAssignedDepartment coordinatorAssignedDepartmentEntity = new CoordinatorAssignedDepartment();
		coordinatorAssignedDepartmentEntity.setCoordinator(coordinatorEntity);
		coordinatorAssignedDepartmentEntity
				.setDepartment(departmentMap.get(requestedAssignedDepartment.getInternalName()));
		coordinatorAssignedDepartmentEntity.setValue(requestedAssignedDepartment.getValue());
		return coordinatorAssignedDepartmentEntity;
	}

	public static void convertAndSetInterestedDepartmentsInEntity(Map<String, Department> departmentMap,
			Coordinator coordinatorEntity, Set<CoordinatorInterestedDepartment> interestedDepartmentsSet) {
		// Set coordinator interested departments
		Set<CoordinatorInterestedDepartment> interestedDepartmentEntitySet = convertToCoordinatorInterestedDepartmentsEntitySet(
				coordinatorEntity, interestedDepartmentsSet, departmentMap);
		coordinatorEntity.setInterestedDepartments(interestedDepartmentEntitySet);

	}

	public static void convertAndSetAssignedDepartmentInEntity(Map<String, Department> departmentMap,
			Coordinator coordinatorEntity, Set<CoordinatorAssignedDepartment> assignedDepartmentsRequest) {
		// Set coordinator assigned departments
		Set<CoordinatorAssignedDepartment> assignedDepartmentEntitySet = convertToCoordinatorAssignedDepartmentsEntitySet(
				coordinatorEntity, assignedDepartmentsRequest, departmentMap);
		coordinatorEntity.setAssignedDepartments(assignedDepartmentEntitySet);
	}

	public static Coordinator setDateAndDepartmentsInCoordinator(Coordinator coordinator) {

		if (CollectionUtils.isNotEmpty(coordinator.getAssignedDepartments())) {
			for (CoordinatorAssignedDepartment coordinatorAssignedDepartment : coordinator.getAssignedDepartments()) {
				coordinatorAssignedDepartment
						.setInternalName(coordinatorAssignedDepartment.getDepartment().getInternalName());
				coordinatorAssignedDepartment
						.setDisplayName(coordinatorAssignedDepartment.getDepartment().getDisplayName());
			}
		}

		if (CollectionUtils.isNotEmpty(coordinator.getInterestedDepartments())) {
			for (CoordinatorInterestedDepartment coordinatorInterestedDepartment : coordinator
					.getInterestedDepartments()) {
				coordinatorInterestedDepartment
						.setInternalName(coordinatorInterestedDepartment.getDepartment().getInternalName());
				coordinatorInterestedDepartment
						.setDisplayName(coordinatorInterestedDepartment.getDepartment().getDisplayName());

			}
		}

		if (null != coordinator.getDateLastModifiedInDB()) {
			coordinator.setLastModifiedDate(convertDateToString(coordinator.getDateLastModifiedInDB()));
		}
		if (null != coordinator.getDateCreatedInDB()) {
			coordinator.setCreatedDate(convertDateToString(coordinator.getDateCreatedInDB()));
		}

		return coordinator;
	}

	public static List<Coordinator> setDateAndDepartmentsInCoordinators(List<Coordinator> coordinators) {
		if (CollectionUtils.isNotEmpty(coordinators)) {
			for (Coordinator coordinator : coordinators) {
				setDateAndDepartmentsInCoordinator(coordinator);
			}
		}
		return coordinators;
	}

}
