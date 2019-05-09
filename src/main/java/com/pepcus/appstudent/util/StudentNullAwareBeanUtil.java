package com.pepcus.appstudent.util;

import static com.pepcus.appstudent.util.ApplicationConstants.MOBILE;
import static com.pepcus.appstudent.util.ApplicationConstants.ID;
import static com.pepcus.appstudent.util.ApplicationConstants.NULL;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang3.StringUtils;

import com.pepcus.appstudent.entity.Student;

public class StudentNullAwareBeanUtil extends BeanUtilsBean {

    public Set<Integer> invalidDataList = new LinkedHashSet<Integer>();

    public Set<Integer> getInvalidDataList() {
        return invalidDataList;
    }

    @Override
    public void copyProperty(Object dest, String name, Object value)
            throws IllegalAccessException, InvocationTargetException {
        if (name.trim().equalsIgnoreCase(ID) && value == null) {
            Student invalidStudent = (Student) dest;
            invalidDataList.add(invalidStudent.getId());
            return;
        } else if (value != null && !name.trim().equalsIgnoreCase(ID) && !name.trim().isEmpty()
                && (name.equalsIgnoreCase(MOBILE) && (value.toString().length() != 10))) {
            Student invalidStudent = (Student) dest;
            invalidDataList.add(invalidStudent.getId());
            return;
        } else if (!name.trim().equalsIgnoreCase(ID)
                && (value == null || value.equals(NULL) || StringUtils.isEmpty(value.toString()))) {
            return;
        }
        super.copyProperty(dest, name, value);
    }
}
