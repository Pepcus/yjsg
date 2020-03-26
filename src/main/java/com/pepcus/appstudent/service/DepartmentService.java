package com.pepcus.appstudent.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pepcus.appstudent.entity.Department;
import com.pepcus.appstudent.entity.DepartmentValue;
import com.pepcus.appstudent.repository.DepartmentRepository;

@Service
public class DepartmentService {

	@Autowired
	DepartmentRepository departmentRepository;


	/**
	 * Method to return department list
	 * 
	 * @return
	 */
	public List<Department> getDepartments() {
		return departmentRepository.findAll();
	}

	
	/**
	 * Method to load map of Department
	 * 
	 * @return
	 */
	public Map<Integer, Department> getDepartmentMap() {
		List<Department> departmentList = departmentRepository.findAll();
		Map<Integer, Department> departmentMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(departmentList)) {
			departmentList.forEach(department -> {
				departmentMap.put(department.getId(), department);
			});
		}
		return departmentMap;
	}
	
	/**
	 * Method to load map of Department value for given department
	 * 
	 * @return
	 */
	public static Map<Integer, DepartmentValue> getDepartmentValueMap(Department department) {
		Set<DepartmentValue> departmentValueList = department.getDepartmentValues();
		Map<Integer, DepartmentValue> departmentValueMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(departmentValueList)) {
			departmentValueList.forEach(departmentValue -> {
				departmentValueMap.put(departmentValue.getId(), departmentValue);
			});
		}
		return departmentValueMap;
	}
}
