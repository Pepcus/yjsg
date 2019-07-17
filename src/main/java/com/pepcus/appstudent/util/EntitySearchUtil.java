package com.pepcus.appstudent.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.omg.CORBA.portable.ApplicationException;
import org.springframework.data.jpa.domain.Specification;

import com.pepcus.appstudent.entity.Student;
import com.pepcus.appstudent.exception.BadRequestException;
import com.pepcus.appstudent.service.EntitySearchSpecification;

/**
 * Class is specific to keep utility methods for Student Search feature.
 * @author Shubham Solanki
 * @since 2018-18-04
 *
 */
public class EntitySearchUtil {

    /**
     * To validate given Class has field with fieldName or not
     * @param <T>
     * @param kclass
     * @param fieldName
     * @return boolean
     * @throws SecurityException
     * @throws NoSuchFieldException
     */
    public static <T> boolean classHasField(Class<T> kclass, String fieldName) {
        try {
            Field field = kclass.getDeclaredField(fieldName);
            if (field == null) {
                return false;
            }
        } catch (NoSuchFieldException | SecurityException ex) {
            return false;
        }

        return true;
    }

    /**
     * To check type of field for given parameters and validate it is
     * java.lang.String or not
     * @param <T>
     * @param kclass
     * @param fieldName
     * @param fieldType
     * @return boolean
     */
    public static <T> boolean isFieldOfType(Class<T> kclass, String fieldName, Class<?> fieldType) {
        Field field = null;
        try {
            field = kclass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException | SecurityException ex) {
            return false;
        }
        if (field.getType().isAssignableFrom(fieldType)) {
            return true;
        }

        return false;
    }

    /**
     * To check type of field for given parameters and validate it is
     * java.lang.String or not
     * @param <T>
     * @param kclass
     * @param fieldName
     * @return boolean
     */
    public static <T> boolean isStringField(Class<T> kclass, String fieldName) {
        return isFieldOfType(kclass, fieldName, String.class);
    }

    /**
     * To filter request parameters on field Name
     * @param <T>
     * @param allRequestParams
     * @param kclass
     * @return <T>
     */
    public static <T> Map<String, String> extractParametersForFilterRecords(Map<String, String> allRequestParams,
            Class<T> kclass) {

        Map<String, String> filteredParameters = new HashMap<String, String>();

        for (Entry<String, String> entry : allRequestParams.entrySet()) {
            if (!classHasField(kclass, entry.getKey())) {
                throw new BadRequestException("Request parameter " + entry.getKey()
                        + " is invalid to filter records of entity " + kclass.getSimpleName());
            }
            filteredParameters.put(entry.getKey(), entry.getValue());
        }

        return filteredParameters;
    }

    /**
     * Create Entity Search Specification It will give priority over
     * requestParameters on searchSpec
     * @param searchSpec
     * @param requestParameters
     * @return <T>
     * @throws ApplicationException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> Specification<T> getEntitySearchSpecification(Map<String, String> requestParameters,
            Class<T> kclass, Student entity) {

        if (requestParameters != null) {
            Map<String, String> requestParametersForFilterRecords = EntitySearchUtil
                    .extractParametersForFilterRecords(requestParameters, kclass);
            if (requestParametersForFilterRecords != null && !requestParametersForFilterRecords.isEmpty()) {
                return new EntitySearchSpecification(requestParametersForFilterRecords, entity);
            }
        }

        return new EntitySearchSpecification(entity);
    }

}
