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
 * This layer is used to intercept requests coming from client and send them to
 * the server if they are valid.
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
        String key = request.getHeader("secretKey");
        Map<String, String> pathVariables = (Map<String, String>) request
                .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        Integer studentId = null;
        Integer coordinatorId = null;
        String id = pathVariables.get("studentId");
        String secretKey = key;
        String coordinatorRequestId= pathVariables.get("id");
        
        if(!StringUtils.isEmpty(coordinatorRequestId)) {
        	 try {
        		 coordinatorId = Integer.valueOf(coordinatorRequestId);
             } catch (NumberFormatException e) {
                 throw new BadRequestException(id + " is not a valid coordinator Id");
             }
        }

        if (StringUtils.isEmpty(secretKey)) {
            throw new AuthorizationFailedException("Unauthorized to access the service");
        }

        if (secretKey.equals(adminSecretKey)) { // admin doesn't require
                                                // individual security code for
                                                // access
            return true;
        }

        if (StringUtils.isEmpty(id) && StringUtils.isEmpty(coordinatorId)) {
            throw new AuthorizationFailedException("Unauthorized to access the service");
        }
		if (!StringUtils.isEmpty(id)) {
			try {
				studentId = Integer.valueOf(id);
			} catch (NumberFormatException e) {
				throw new BadRequestException(id + " is not a valid studentId");
			}
		}
        return authManager.checkAuthorization(coordinatorId, studentId, secretKey);
    }

}