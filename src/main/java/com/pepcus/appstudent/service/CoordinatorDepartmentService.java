package com.pepcus.appstudent.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pepcus.appstudent.entity.Coordinator;
import com.pepcus.appstudent.entity.CoordinatorAssignedDepartment;
import com.pepcus.appstudent.entity.CoordinatorInterestedDepartment;
import com.pepcus.appstudent.entity.Department;
import com.pepcus.appstudent.repository.CoordinatorAssignedDepartmentRepository;
import com.pepcus.appstudent.repository.CoordinatorInterestedDepartmentRepository;
import com.pepcus.appstudent.repository.DepartmentRepository;
import com.pepcus.appstudent.specifications.AssignedDepartmentSpecification;
import com.pepcus.appstudent.specifications.DepartmentSpecification;
import com.pepcus.appstudent.specifications.InterestedDepartmentSpecification;

@Service
public class CoordinatorDepartmentService {

	@Autowired
	DepartmentRepository departmentRepository;

	@Autowired
	CoordinatorAssignedDepartmentRepository coordinatorAssignedDepartmentRepository;

	@Autowired
	CoordinatorInterestedDepartmentRepository coordinatorInterestedDepartmentRepository;

	/**
	 * Method to load map of Department
	 * 
	 * @return
	 */
	public Map<String, Department> getDepartmentMap() {
		List<Department> departmentList = departmentRepository.findAll();
		Map<String, Department> departmentMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		if (CollectionUtils.isNotEmpty(departmentList)) {
			departmentList.forEach(department -> {
				departmentMap.put(department.getInternalName(), department);
			});
		}
		return departmentMap;
	}

	public List<Department> getDepartmentEntityList(List<String> internalNameList) {
		return departmentRepository.findAll(DepartmentSpecification.getDepartments(internalNameList));
	}

	public void persistAssignedDepartmentEntitySet(
			Set<CoordinatorAssignedDepartment> coordinatorAssignedDepartmentEntitySet) {
		if(CollectionUtils.isNotEmpty(coordinatorAssignedDepartmentEntitySet)){
			coordinatorAssignedDepartmentRepository.save(coordinatorAssignedDepartmentEntitySet);
		}
	}
	
	
	public void deleteCoordinatorAssignedDepartments(Coordinator coordinator) {
		List<CoordinatorAssignedDepartment> coordinatorAssignedDepartments = coordinatorAssignedDepartmentRepository
				.findByCoordinator(coordinator);
		if (CollectionUtils.isNotEmpty(coordinatorAssignedDepartments)) {
			coordinator.getAssignedDepartments().clear();
			coordinatorAssignedDepartmentRepository.delete(coordinatorAssignedDepartments);
		}
	}

	public void persistInterestedDepartmentEntitySet(
			Set<CoordinatorInterestedDepartment> coordinatorInterestedDepartmentEntitySet) {
		if (CollectionUtils.isNotEmpty(coordinatorInterestedDepartmentEntitySet)) {
			coordinatorInterestedDepartmentRepository.save(coordinatorInterestedDepartmentEntitySet);
		}
	}

	public void deleteCoordinatorInterestedDepartments(Coordinator coordinator) {
		List<CoordinatorInterestedDepartment> coordinatorInterestedDepartments = coordinatorInterestedDepartmentRepository
				.findByCoordinator(coordinator);
		if (CollectionUtils.isNotEmpty(coordinatorInterestedDepartments)) {
			coordinator.getInterestedDepartments().clear();
			coordinatorInterestedDepartmentRepository.delete(coordinatorInterestedDepartments);
		}
	}
	
	public List<CoordinatorAssignedDepartment> getCoordinatorAssignedDepartments(
			List<Department> departments) {
		return coordinatorAssignedDepartmentRepository
				.findAll(AssignedDepartmentSpecification.getCoordinatorAssignedDepartments(departments));
	}
	
	public List<CoordinatorInterestedDepartment> getCoordinatorInterestedDepartments(
			List<Department> departments) {
		return coordinatorInterestedDepartmentRepository
				.findAll(InterestedDepartmentSpecification.getCoordinatorInterestedDepartments(departments));
	}
	
	public Set<Integer> getAssignedDepartmentsCoordinatorIds(List<String> internalNameList) {
		List<Department> departmentList = getDepartmentEntityList(internalNameList);
		List<CoordinatorAssignedDepartment> coordinatorAssignedDepartments = getCoordinatorAssignedDepartments(
				departmentList);
		Set<Integer> ids = new HashSet<Integer>();
		if(CollectionUtils.isNotEmpty(departmentList)){
			ids = coordinatorAssignedDepartments.stream().map(CoordinatorAssignedDepartment::getCoordinator)
					.map(Coordinator::getId).collect(Collectors.toSet());	
		}
		return ids;
	}
	
	
	public Set<Integer> getInterestedDepartmentsCoordinatorIds(List<String> internalNameList) {
		List<Department> departmentList = getDepartmentEntityList(internalNameList);
		List<CoordinatorInterestedDepartment> coordinatorInterestedDepartments = getCoordinatorInterestedDepartments(
				departmentList);
		Set<Integer> ids = new HashSet<Integer>();
		if(CollectionUtils.isNotEmpty(departmentList)){
			ids = coordinatorInterestedDepartments.stream().map(CoordinatorInterestedDepartment::getCoordinator)
					.map(Coordinator::getId).collect(Collectors.toSet());
		}
		return ids;
	}
	
	public Set<Integer> getCoordinatorIdsByDepartments(String assignedDepartments, String interestedDepartments) {
		Set<Integer> coordinatorIds = new HashSet<>();
		if (StringUtils.isNotBlank(assignedDepartments)) {
			List<String> internalNameList = Arrays.asList(assignedDepartments);
			coordinatorIds
					.addAll(getAssignedDepartmentsCoordinatorIds(internalNameList));
		}

		if (StringUtils.isNotBlank(interestedDepartments)) {
			List<String> internalNameList = Arrays.asList(interestedDepartments);
			coordinatorIds
					.addAll(getInterestedDepartmentsCoordinatorIds(internalNameList));
		}
		return coordinatorIds;
	}
}
