package com.pepcus.appstudent.util;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.beanutils.BeanUtilsBean;
import com.pepcus.appstudent.entity.Student;

public class NullAwareBeanUtilsBean extends BeanUtilsBean{

    public Set<Integer> invalidDataList = new LinkedHashSet<Integer>();

    public Set<Integer> getInvalidDataList() {
        return invalidDataList;
    }

    @Override
    public void copyProperty(Object dest, String name, Object value)
            throws IllegalAccessException, InvocationTargetException {
        if (value == null) {
            return;
        } else if (value != null && !name.trim().equalsIgnoreCase("id")
                && (!value.toString().equalsIgnoreCase("Y") && !value.toString().equalsIgnoreCase("N"))) {
            Student invalidStudent = (Student) dest;
            invalidDataList.add(invalidStudent.getId());
            return;
        }
        super.copyProperty(dest, name, value);
    }

}
