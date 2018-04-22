package com.pepcus.appstudent.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.pepcus.appstudent.exception.AuthorizationFailedException;
import com.pepcus.appstudent.exception.BadRequestException;
import com.pepcus.appstudent.service.AuthorizationManager;

/**
 * This layer is used to intercept requests coming from client and 
 * send them to the server if they are valid. 
 * 
 * @author Shubham Solanki
 * @since 12-02-2018
 *
 */
@AllArgsConstructor
public class ApiSecretKeyInterceptor extends HandlerInterceptorAdapter {
	
	private String adminSecretKey;
	
	private AuthorizationManager authManager;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		if (request.getMethod().equals(RequestMethod.POST.name())) {
			return true;
		}
		
		Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		
		Integer studentId = null;
		String id = pathVariables.get("studentId");
		String secretKey = pathVariables.get("secretKey");
		
		if (StringUtils.isEmpty(secretKey)) {
            throw new AuthorizationFailedException("Unauthorized to access the service");
		}
		
		if (secretKey.equals(adminSecretKey)) { // admin doesn't require individual security code for access
			return true;
		} 
				
		if (StringUtils.isEmpty(id)) {
			throw new AuthorizationFailedException("Unauthorized to access the service");
		}
		
		try {
			studentId = Integer.valueOf(id);
		} catch (NumberFormatException e) {
			throw new BadRequestException(id + " is not a valid studentId");
		}
		return authManager.checkAuthorization(studentId, secretKey); 
	}

}