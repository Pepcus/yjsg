package com.pepcus.appstudent.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pepcus.appstudent.entity.DuplicateRegistration;

public interface DuplicateRegistrationRepository extends JpaRepository<DuplicateRegistration, Integer>{

}
