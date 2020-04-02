package com.pepcus.appstudent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.pepcus.appstudent.entity.Coordinator;
import com.pepcus.appstudent.entity.CoordinatorAssignedDepartment;

public interface CoordinatorAssignedDepartmentRepository extends JpaRepository<CoordinatorAssignedDepartment, Integer>,
		JpaSpecificationExecutor<CoordinatorAssignedDepartment> {

	List<CoordinatorAssignedDepartment> findByCoordinator(Coordinator coordinator);

}