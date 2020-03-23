package com.pepcus.appstudent.service;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pepcus.appstudent.entity.Coordinator;
import com.pepcus.appstudent.entity.CoordinatorAssignedDepartment;
import com.pepcus.appstudent.entity.CoordinatorInterestedDepartment;
import com.pepcus.appstudent.repository.CoordinatorAssignedDepartmentRepository;
import com.pepcus.appstudent.repository.CoordinatorInterestedDepartmentRepository;

@Service
public class CoordinatorDepartmentService {


	@Autowired
	CoordinatorAssignedDepartmentRepository coordinatorAssignedDepartmentRepository;

	@Autowired
	CoordinatorInterestedDepartmentRepository coordinatorInterestedDepartmentRepository;


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
	
}