package com.pepcus.appstudent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.pepcus.appstudent.entity.Student;

/**
 * Repository for student entity
 * 
 * @author Shubham Solanki
 * @since 12-02-2018
 *
 */
public interface StudentRepository extends JpaRepository<Student, Integer>, JpaSpecificationExecutor<Student> {
	
	public Student findByIdAndSecretKey(Integer id, String secretKey);

}
