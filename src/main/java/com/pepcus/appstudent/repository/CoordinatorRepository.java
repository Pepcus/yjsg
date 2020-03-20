package com.pepcus.appstudent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.pepcus.appstudent.entity.Coordinator;

public interface CoordinatorRepository
		extends JpaRepository<Coordinator, Integer>, JpaSpecificationExecutor<Coordinator> {
	
	public Coordinator findById(Integer id);

	public Coordinator findByIdAndSecretKey(Integer id, String secretKey);
}
