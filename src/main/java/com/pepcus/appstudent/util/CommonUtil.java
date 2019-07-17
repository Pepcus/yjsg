package com.pepcus.appstudent.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Class to keep common utility methods
 * @author Shubham Solanki
 * @since 12-02-2018
 *
 */
public class CommonUtil {

    public static final String TOTAL_RECORDS = "RECORDS";

    /**
     * Method to convert Date to String
     * @param date
     * @return string
     */
    public static String convertDateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }

    /**
     * To set request attributes
     * @param attributeName
     * @param attributeValue
     */
    public static void setRequestAttribute(String attributeName, Object attributeValue) {
        HttpServletRequest request = getRequest();
        if (request != null) {
            request.setAttribute(attributeName, attributeValue);
        }
    }

    /**
     * To fetch attribute value from request for given attribute name
     * @param attributeName
     * @return Object
     */
    public static Object getRequestAttribute(String attributeName) {
        HttpServletRequest request = getRequest();
        Object attrVal = null;
        if (request != null) {
            return request.getAttribute(attributeName);
        }
        return attrVal;
    }

    /**
     * Fetch request object from RequestContextHolder
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes servletReqAttr = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (servletReqAttr != null) {
            return servletReqAttr.getRequest();
        }
        return null;
    }

}
