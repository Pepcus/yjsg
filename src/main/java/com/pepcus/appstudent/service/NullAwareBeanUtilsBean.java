package com.pepcus.appstudent.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;

import com.pepcus.appstudent.entity.Student;

public class NullAwareBeanUtilsBean extends BeanUtilsBean{

	public List<Integer> invalidDataList = new ArrayList<Integer>();
	
	
    public List<Integer> getInvalidDataList() {
		return invalidDataList;
	}


	@Override
    public void copyProperty(Object dest, String name, Object value)
            throws IllegalAccessException, InvocationTargetException {
        if(value==null){
        	return;
        }
        else if (value !=null && !name.equals("id") && (!value.equals("Y") && !value.equals("N"))){
        	Student invalidStudent = (Student) dest;
        	invalidDataList.add(invalidStudent.getId());
        	return;
        }
        super.copyProperty(dest, name, value);
    }

}
