package com.pepcus.appstudent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.pepcus.appstudent.entity.SMSFlags;

public interface SmsRepository extends JpaRepository<SMSFlags, Integer>, JpaSpecificationExecutor<SMSFlags> { 

	 SMSFlags findByflagName(String flagName);
	 
}
