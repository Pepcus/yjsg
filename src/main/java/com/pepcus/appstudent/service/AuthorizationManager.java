package com.pepcus.appstudent.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.pepcus.appstudent.exception.AuthorizationFailedException;
import com.pepcus.appstudent.repository.CoordinatorRepository;
import com.pepcus.appstudent.repository.StudentRepository;

/**
 * This layer is used to validate secretKey from DB
 * 
 * @author Shubham Solanki
 * @since 12-02-2018
 *
 */
@Service
public class AuthorizationManager {
	
	@Autowired
	private StudentRepository studentRepository;
	@Autowired
	private CoordinatorRepository coordinatorRepository;
	
	/**
	 * Method to check valid studentId and secret key 
	 * 
	 * @param secretKey
	 * @return
	 */
	public boolean checkAuthorization(Integer coordinatorId, Integer studentId, String secretKey) {
        //Validate studentId and secretKey
        if (null == studentRepository.findByIdAndSecretKey(studentId, secretKey) && null == coordinatorRepository.findByIdAndSecretKey(coordinatorId, secretKey)) {  
            throw new AuthorizationFailedException("Unauthorized to access the service");
        }
        
        return true;
    }

}