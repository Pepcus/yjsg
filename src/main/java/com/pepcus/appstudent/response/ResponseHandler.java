package com.pepcus.appstudent.response;

import static com.pepcus.appstudent.util.CommonUtil.TOTAL_RECORDS;
import static com.pepcus.appstudent.util.CommonUtil.getRequestAttribute;

import java.util.List;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.pepcus.appstudent.entity.Student;
import com.pepcus.appstudent.exception.ApiErrorResponse;

/**
 * Global response handler for all APIs
 * 
 * @author Shubham Solanki
 * @since 12-02-2018
 *
 */
@ControllerAdvice("com.pepcus.appstudent.controller")
public class ResponseHandler implements ResponseBodyAdvice<Object> {

	/**
	 * 
	 * @param methodParameter
	 * @param request
	 * @return
	 */
	@Override
	public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> request) {
		return true;
	}

	/**
	 * 
	 * @param body
	 * @param returnType
	 * @param selectedContentType
	 * @param selectedConverterType
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		if (body instanceof ApiErrorResponse || body instanceof Exception) {
			return body;
		}

		ServletServerHttpRequest httpRequest = (ServletServerHttpRequest) request;
		ServletServerHttpResponse httpResponse = (ServletServerHttpResponse) response;

		ApiResponse apiResponse = new ApiResponse();

		int statusCode = httpResponse.getServletResponse().getStatus();
		apiResponse.setCode(String.valueOf(statusCode));
		apiResponse.setStatus(HttpStatus.valueOf(statusCode).name());

		if (httpRequest.getMethod().equals(HttpMethod.POST) && body instanceof Student) {
			apiResponse.setMessage("Student successfully created");
			apiResponse.setStudent((Student) body);
		}

		if (httpRequest.getMethod().equals(HttpMethod.PUT) && body instanceof Student) {
			apiResponse.setMessage("Student successfully updated");
			apiResponse.setStudent((Student) body);
		}

		if (httpRequest.getMethod().equals(HttpMethod.PATCH) && body instanceof ApiResponse) {
			return body;
		}

		if (httpRequest.getMethod().equals(HttpMethod.GET)) {
			if (body instanceof Student) {
				apiResponse.setStudent((Student) body);
			} else if (body instanceof List) {
				if ((List) body == null || ((List) body).isEmpty()) {
					apiResponse.setMessage("No records found for request.");
				} else {
					Object totalRecObj = getRequestAttribute(TOTAL_RECORDS);
					if (totalRecObj != null) {
						apiResponse.setTotalRecords(String.valueOf(totalRecObj));
					}
					apiResponse.setStudents((List) body);
				}
			}
		}
		return apiResponse;
	}

}