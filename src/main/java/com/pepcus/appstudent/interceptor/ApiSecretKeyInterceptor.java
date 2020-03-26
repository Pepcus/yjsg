package com.pepcus.appstudent.interceptor;

import static com.pepcus.appstudent.validation.DataValidator.expect;
import static com.pepcus.appstudent.validation.DataValidator.integer;
import static com.pepcus.appstudent.validation.DataValidator.nonNegative;
import static com.pepcus.appstudent.validation.DataValidator.nonZero;
import static com.pepcus.appstudent.validation.DataValidator.validate;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.pepcus.appstudent.exception.AuthorizationFailedException;
import com.pepcus.appstudent.service.AuthorizationManager;

import lombok.AllArgsConstructor;

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

       
        String secretKey = key;
        if (StringUtils.isEmpty(secretKey)) {
            throw new AuthorizationFailedException("Unauthorized to access the service");
        }
        
		if (secretKey.equals(adminSecretKey)) { 
			// admin doesn't require individual security code for access
			return true;
		}
        
        Integer studentId = null;
        Integer coordinatorId = null;        
        String studentRequestId = pathVariables.get("studentId");
        String coordinatorRequestId= pathVariables.get("coordinatorId");
        
        
        if (!StringUtils.isEmpty(studentRequestId)) {
        	validate("studentId", studentRequestId, expect(integer,nonNegative, nonZero));
        	studentId = Integer.valueOf(studentRequestId);
		}
        
		if (!StringUtils.isEmpty(coordinatorRequestId)) {
			validate("coordinatorId", coordinatorRequestId, expect(integer, nonNegative, nonZero));
			coordinatorId = Integer.valueOf(coordinatorRequestId);
		}

		if (studentId == null && coordinatorId == null) {
			throw new AuthorizationFailedException("Unauthorized to access the service");
		}

        
        return authManager.checkAuthorization(studentId, coordinatorId, secretKey);
    }

}