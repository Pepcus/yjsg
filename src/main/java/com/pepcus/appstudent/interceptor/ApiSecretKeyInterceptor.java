package com.pepcus.appstudent.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.pepcus.appstudent.service.AuthorizationManager;

import lombok.AllArgsConstructor;

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
		
		Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		
		Integer studentId = null;
		String id = pathVariables.get("studentId");
		String secretKey = pathVariables.get("secretKey");
		
		if (!StringUtils.isEmpty(id)) {
			studentId = Integer.valueOf(id);
		}
		
		if (adminSecretKey.equals(secretKey)) {
			return true;
		}
		
		if (request.getMethod().equals(RequestMethod.POST.name())) {
			return true;
		}
		
		return authManager.checkAuthorization(studentId, secretKey); 
	}

}