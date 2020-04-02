package com.pepcus.appstudent.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pepcus.appstudent.entity.Coordinator;
import com.pepcus.appstudent.entity.Student;
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
	public boolean checkAuthorization(Integer studentId, Integer coordinatorId, String secretKey) {
        //Validate studentId and secretKey
		Student student = null;
		Coordinator coordinator = null;
		if(studentId != null){
			student = studentRepository.findByIdAndSecretKey(studentId, secretKey);
		} else if (coordinatorId != null) {
			coordinator = coordinatorRepository.findByIdAndSecretKey(coordinatorId, secretKey);	
		}
		
		
        if (student == null && coordinator == null) {  
            throw new AuthorizationFailedException("Unauthorized to access the service");
        }
        
        return true;
    }

}