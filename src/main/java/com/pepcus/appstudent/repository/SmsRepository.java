package com.pepcus.appstudent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.pepcus.appstudent.entity.SMSFlags;

/**
 SMS Repository for manipulation of SMS data
 * 
 */
public interface SmsRepository extends JpaRepository<SMSFlags, Integer>, JpaSpecificationExecutor<SMSFlags> {

    SMSFlags findByflagName(String flagName);

}
