package com.pepcus.appstudent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.pepcus.appstudent.interceptor.ApiSecretKeyInterceptor;
import com.pepcus.appstudent.service.AuthorizationManager;

/**
 * Application Configuration class 
 * 
 * @author Shubham Solanki
 * @since 12-02-2018
 *
 */
@Configuration
@EnableWebMvc
public class AppConfig extends WebMvcConfigurerAdapter {
	
	@Value("${com.pepcus.appstudent.admin.secret_key}")
	private String adminSecretKey;
	
	
	@Autowired
	private AuthorizationManager authManager;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new ApiSecretKeyInterceptor(adminSecretKey, authManager))
			.addPathPatterns("/v1/**");
	}

}
