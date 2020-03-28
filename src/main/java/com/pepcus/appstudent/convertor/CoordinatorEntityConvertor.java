package com.pepcus.appstudent.convertor;

import static com.pepcus.appstudent.util.CommonUtil.convertDateToString;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.pepcus.appstudent.entity.Coordinator;
import com.pepcus.appstudent.entity.CoordinatorAssignedDepartment;
import com.pepcus.appstudent.entity.CoordinatorInterestedDepartment;
import com.pepcus.appstudent.entity.Department;
import com.pepcus.appstudent.entity.DepartmentValue;
import com.pepcus.appstudent.service.DepartmentService;
import com.pepcus.appstudent.util.ApplicationConstants;

public class CoordinatorEntityConvertor {

	// ###################### Convert Request to Entity ######################

	public static Coordinator convertCoordinatorEntity(Coordinator request) {
		Coordinator coordinatorEntity = new Coordinator();

		Date currentDate = Calendar.getInstance().getTime();
		coordinatorEntity.setDateCreatedInDB(currentDate);
		coordinatorEntity.setDateLastModifiedInDB(currentDate);
		if (request.getIsActive() == null || StringUtils.isBlank(request.getIsActive())) {
			coordinatorEntity.setIsActive(ApplicationConstants.VAL_TRUE);
		}
		return convertCoordinatorEntity(coordinatorEntity, request);
	}

	public static Coordinator convertCoordinatorEntity(Coordinator coordinatorEntity, Coordinator request) {
		coordinatorEntity.setDateLastModifiedInDB(Calendar.getInstance().getTime());

		if (Optional.ofNullable(request.getFirstName()).isPresent()) {
			String firstName = request.getFirstName().toLowerCase();
			String firstLetter = firstName.substring(0, 1);
			coordinatorEntity.setFirstName(firstName.replaceFirst("[" + firstLetter + "]", firstLetter.toUpperCase()));
		}

		if (Optional.ofNullable(request.getLastName()).isPresent()) {
			String lastName = request.getLastName().toLowerCase();
			String lastLetter = lastName.substring(0, 1);
			coordinatorEntity.setLastName(lastName.replaceFirst("[" + lastLetter + "]", lastLetter.toUpperCase()));
		}

		if (Optional.ofNullable(request.getDob()).isPresent()) {
			coordinatorEntity.setDob(request.getDob());
		}

		if (Optional.ofNullable(request.getGender()).isPresent()) {
			coordinatorEntity.setGender(request.getGender());
		}

		if (Optional.ofNullable(request.getWhatsappNumber()).isPresent()) {
			coordinatorEntity.setWhatsappNumber(request.getWhatsappNumber());
		}

		if (Optional.ofNullable(request.getIsActive()).isPresent()) {
			String isActive = request.getIsActive().equalsIgnoreCase(ApplicationConstants.VAL_TRUE)
					? ApplicationConstants.VAL_TRUE : ApplicationConstants.VAL_FALSE;
			coordinatorEntity.setIsActive(isActive);
		}

		if (Optional.ofNullable(request.getEmail()).isPresent()) {
			String value = (StringUtils.isNotBlank(request.getEmail())) ? request.getEmail() : null;
			coordinatorEntity.setEmail(value);
		}

		if (Optional.ofNullable(request.getAlternateNumber()).isPresent()) {
			String value = (StringUtils.isNotBlank(request.getAlternateNumber())) ? request.getAlternateNumber() : null;
			coordinatorEntity.setAlternateNumber(value);
		}

		if (Optional.ofNullable(request.getAddress()).isPresent()) {
			String value = (StringUtils.isNotBlank(request.getAddress())) ? request.getAddress() : null;
			coordinatorEntity.setAddress(value);
		}

		if (Optional.ofNullable(request.getArea()).isPresent()) {
			String value = (StringUtils.isNotBlank(request.getArea())) ? request.getArea() : null;
			coordinatorEntity.setArea(value);
		}

		if (Optional.ofNullable(request.getRemarks()).isPresent()) {
			String value = (StringUtils.isNotBlank(request.getRemarks())) ? request.getRemarks() : null;
			coordinatorEntity.setRemarks(value);
		}

		return coordinatorEntity;
	}

	public static Set<CoordinatorInterestedDepartment> convertToCoordinatorInterestedDepartmentsEntitySet(
			Coordinator coordinatorEntity, Set<CoordinatorInterestedDepartment> requestedInterestedDepartmentList,
			Map<Integer, Department> departmentMap) {
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
			Coordinator coordinatorEntity, Map<Integer, Department> departmentMap,
			CoordinatorInterestedDepartment requestedInterestedDepartment) {

		CoordinatorInterestedDepartment coordinatorInterestedDepartmentEntity = new CoordinatorInterestedDepartment();
		coordinatorInterestedDepartmentEntity.setCoordinator(coordinatorEntity);
		coordinatorInterestedDepartmentEntity
				.setDepartment(departmentMap.get(requestedInterestedDepartment.getId()));
		return coordinatorInterestedDepartmentEntity;
	}

	public static Set<CoordinatorAssignedDepartment> convertToCoordinatorAssignedDepartmentsEntitySet(
			Coordinator coordinatorEntity, Set<CoordinatorAssignedDepartment> requestedAssignedDepartmentList,
			Map<Integer, Department> departmentMap) {
		Set<CoordinatorAssignedDepartment> coordinatorAssignedDepartmentEntitySet = new HashSet<>();
		if (CollectionUtils.isNotEmpty(requestedAssignedDepartmentList)) {
			// convert requested list to entity list
			for (CoordinatorAssignedDepartment requestedAssignedDepartment : requestedAssignedDepartmentList) {
				Department department = departmentMap.get(requestedAssignedDepartment.getId());
				Set<DepartmentValue> requestedDepartmentValueSet = requestedAssignedDepartment.getDepartmentValues();

				if (CollectionUtils.isEmpty(requestedDepartmentValueSet)) {
					coordinatorAssignedDepartmentEntitySet
							.add(convertToCoordinatorAssignedDepartmentEntity(coordinatorEntity, department, null));
				} else {
					Map<Integer, DepartmentValue> departmentValueMap = DepartmentService
							.getDepartmentValueMap(department);
					for (DepartmentValue requestedDepartmentValue : requestedDepartmentValueSet) {
						
						coordinatorAssignedDepartmentEntitySet
								.add(convertToCoordinatorAssignedDepartmentEntity(coordinatorEntity, department,
										departmentValueMap.get(requestedDepartmentValue.getId())));
					}
				}
			}
		}
		return coordinatorAssignedDepartmentEntitySet;
	}

	private static CoordinatorAssignedDepartment convertToCoordinatorAssignedDepartmentEntity(
			Coordinator coordinatorEntity, Department department, DepartmentValue departmentValue) {
		CoordinatorAssignedDepartment coordinatorAssignedDepartmentEntity = new CoordinatorAssignedDepartment();
		coordinatorAssignedDepartmentEntity.setCoordinator(coordinatorEntity);
		coordinatorAssignedDepartmentEntity.setDepartment(department);
		coordinatorAssignedDepartmentEntity.setDepartmentValue(departmentValue);
		return coordinatorAssignedDepartmentEntity;
	}

	public static void convertAndSetInterestedDepartmentsInEntity(Map<Integer, Department> departmentMap,
			Coordinator coordinatorEntity, Set<CoordinatorInterestedDepartment> interestedDepartmentsSet) {
		// Set coordinator interested departments
		Set<CoordinatorInterestedDepartment> interestedDepartmentEntitySet = convertToCoordinatorInterestedDepartmentsEntitySet(
				coordinatorEntity, interestedDepartmentsSet, departmentMap);
		coordinatorEntity.setInterestedDepartments(interestedDepartmentEntitySet);

	}

	public static void convertAndSetAssignedDepartmentInEntity(Map<Integer, Department> departmentMap,
			Coordinator coordinatorEntity, Set<CoordinatorAssignedDepartment> assignedDepartmentsRequest) {
		// Set coordinator assigned departments
		Set<CoordinatorAssignedDepartment> assignedDepartmentEntitySet = convertToCoordinatorAssignedDepartmentsEntitySet(
				coordinatorEntity, assignedDepartmentsRequest, departmentMap);
		coordinatorEntity.setAssignedDepartments(assignedDepartmentEntitySet);
	}

	
	// ###################### Convert Entity to Response ######################

	public static Coordinator setDateAndDepartmentsInCoordinator(Coordinator coordinator) {
		setCoordinatorAssignedDepartment(coordinator);
		setCoordinatorInterestedDepartments(coordinator);

		if (null != coordinator.getDateLastModifiedInDB()) {
			coordinator.setLastModifiedDate(convertDateToString(coordinator.getDateLastModifiedInDB()));
		}
		if (null != coordinator.getDateCreatedInDB()) {
			coordinator.setCreatedDate(convertDateToString(coordinator.getDateCreatedInDB()));
		}

		return coordinator;
	}

	private static void setCoordinatorInterestedDepartments(Coordinator coordinator) {
		if (CollectionUtils.isNotEmpty(coordinator.getInterestedDepartments())) {
			for (CoordinatorInterestedDepartment coordinatorInterestedDepartment : coordinator
					.getInterestedDepartments()) {
				Department coordinatorDepartment = coordinatorInterestedDepartment.getDepartment();
				coordinatorInterestedDepartment.setId(coordinatorDepartment.getId());
				coordinatorInterestedDepartment
						.setInternalName(coordinatorDepartment.getInternalName());
				coordinatorInterestedDepartment
						.setDisplayName(coordinatorDepartment.getDisplayName());
				coordinatorInterestedDepartment.setDepartmentValueType(coordinatorDepartment.getDepartmentValueType());
			}
		}
	}

	private static void setCoordinatorAssignedDepartment(Coordinator coordinator) {
		Set<CoordinatorAssignedDepartment> coordinatorAssignedDepartments = coordinator.getAssignedDepartments();
		Map<Integer, Department> coordinatorDepartmentMap = new HashMap<>();

		if (CollectionUtils.isNotEmpty(coordinatorAssignedDepartments)) {
			for (CoordinatorAssignedDepartment coordinatorAssignedDepartment : coordinatorAssignedDepartments) {
				Department coordinatorDepartment = coordinatorAssignedDepartment.getDepartment();
				Integer departmentId = coordinatorDepartment.getId();
				if (!coordinatorDepartmentMap.containsKey(departmentId)) {
					coordinatorDepartment.setDepartmentValues(new HashSet<>());
					coordinatorDepartmentMap.put(coordinatorDepartment.getId(), coordinatorDepartment);
				}

				if (coordinatorAssignedDepartment.getDepartmentValue() != null) {
					coordinatorDepartmentMap.get(departmentId).getDepartmentValues()
							.add(coordinatorAssignedDepartment.getDepartmentValue());
				}
			}

			Set<CoordinatorAssignedDepartment> reformedCoordinatorAssignedDepartments = reformedCoordinatorAssignedDepartment(
					coordinatorAssignedDepartments, coordinatorDepartmentMap);
			coordinator.setAssignedDepartments(reformedCoordinatorAssignedDepartments);
		}
	}

	private static Set<CoordinatorAssignedDepartment> reformedCoordinatorAssignedDepartment(
			Set<CoordinatorAssignedDepartment> coordinatorAssignedDepartments,
			Map<Integer, Department> coordinatorDepartmentMap) {
		Set<CoordinatorAssignedDepartment> reformedCoordinatorAssignedDepartments = new HashSet<>();
		for (CoordinatorAssignedDepartment coordinatorAssignedDepartment : coordinatorAssignedDepartments) {
			Integer departmentId = coordinatorAssignedDepartment.getDepartment().getId();
			Department coordinatorDepartment = coordinatorDepartmentMap.get(departmentId);
			if (coordinatorDepartment != null) {
				CoordinatorAssignedDepartment reformedCoordinatorAssignedDepartment = new CoordinatorAssignedDepartment();
				reformedCoordinatorAssignedDepartment.setId(departmentId);
				reformedCoordinatorAssignedDepartment.setInternalName(coordinatorDepartment.getInternalName());
				reformedCoordinatorAssignedDepartment.setDisplayName(coordinatorDepartment.getDisplayName());
				reformedCoordinatorAssignedDepartment.setDepartmentValueType(coordinatorDepartment.getDepartmentValueType());
				reformedCoordinatorAssignedDepartment.setDepartmentValues(coordinatorDepartment.getDepartmentValues());
				reformedCoordinatorAssignedDepartments.add(reformedCoordinatorAssignedDepartment);
				coordinatorDepartmentMap.remove(departmentId);
			}
		}
		return reformedCoordinatorAssignedDepartments;
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
