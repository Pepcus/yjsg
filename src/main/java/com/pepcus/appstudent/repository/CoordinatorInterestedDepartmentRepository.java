package com.pepcus.appstudent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.pepcus.appstudent.entity.Coordinator;
import com.pepcus.appstudent.entity.CoordinatorInterestedDepartment;

public interface CoordinatorInterestedDepartmentRepository extends JpaRepository<CoordinatorInterestedDepartment, Integer>,
		JpaSpecificationExecutor<CoordinatorInterestedDepartment> {

	List<CoordinatorInterestedDepartment> findByCoordinator(Coordinator coordinator);

}