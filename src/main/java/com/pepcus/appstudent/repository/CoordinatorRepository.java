package com.pepcus.appstudent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pepcus.appstudent.entity.Coordinator;

public interface CoordinatorRepository extends JpaRepository<Coordinator , Integer>{
public Coordinator findById(Integer id);
public List<Coordinator> findAll();
public Coordinator findByPrimaryContactNumberOrAlternateContactNumber(String primaryContactNumber, String alternateContactNumber);
}
