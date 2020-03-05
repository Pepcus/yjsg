package com.pepcus.appstudent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.pepcus.appstudent.entity.GmsStudent;

/**
 * Repository for student gms entity
 * 
 * @author Sandeep Vishwakarma
 * @since 05-03-2020
 *
 */
public interface GmsStudentRepository extends JpaRepository<GmsStudent, Integer>, JpaSpecificationExecutor<GmsStudent> {

}
