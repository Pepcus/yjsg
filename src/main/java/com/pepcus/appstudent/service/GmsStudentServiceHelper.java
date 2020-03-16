package com.pepcus.appstudent.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.HttpStatus;

import com.pepcus.appstudent.entity.GmsStudent;
import com.pepcus.appstudent.entity.GmsStudentWrapper;
import com.pepcus.appstudent.exception.BadRequestException;
import com.pepcus.appstudent.response.ApiResponse;
import com.pepcus.appstudent.util.ApplicationConstants;

/**
 * Helper class of GmsStudentService
 *
 */
public class GmsStudentServiceHelper {

	protected static void copyBeanProperty(List<GmsStudent> studentListDB, List<GmsStudentWrapper> studentUploadList,
			BeanUtilsBean onlyNotNullCopyProperty) {
		for (GmsStudent studentDB : studentListDB) {
			BeanPropertyValueEqualsPredicate predicate = new BeanPropertyValueEqualsPredicate("id",
					String.valueOf(studentDB.getId()));
			GmsStudentWrapper stuAttendance = (GmsStudentWrapper) CollectionUtils.find(studentUploadList, predicate);
			try {
				onlyNotNullCopyProperty.copyProperties(studentDB, stuAttendance);
			} catch (InvocationTargetException | IllegalAccessException e) {
				throw new BadRequestException(ApplicationConstants.FAILED_TO_UPDATE + e);
			}
		}
	}

	protected Map<String, List<Integer>> getInvalidIdsList(List<Integer> studentIds, List<GmsStudent> studentList) {
		List<Integer> validIds = studentList.stream().map(std -> std.getId()).collect(Collectors.toList());
		if (!validIds.isEmpty()) {
			studentIds.removeAll(validIds);
		}
		Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();
		map.put(ApplicationConstants.VALID, validIds);
		map.put(ApplicationConstants.INVALID, studentIds);
		map.put(ApplicationConstants.RECORD_NOT_EXIST, studentIds);
		return map;
	}
	
	protected List<Integer> getStudentIdsFromFile(List<GmsStudentWrapper> uploadedGmsStudentList) {
		List<Integer> studentIdList = new ArrayList<Integer>();
		for (GmsStudentWrapper student : uploadedGmsStudentList) {
			studentIdList.add(Integer.parseInt(student.getId()));
		}
		return studentIdList;
	}
	
	
	protected static ApiResponse populateUpdateStudentsInBulkResponse(Map<String, List<Integer>> validInvalidIdsMap) {
		ApiResponse response = new ApiResponse();
		if (!validInvalidIdsMap.get(ApplicationConstants.INVALID).isEmpty()) {
			response.setCode(String.valueOf(HttpStatus.MULTI_STATUS));
			response.setFailRecordIds(String.valueOf(validInvalidIdsMap.get(ApplicationConstants.INVALID)));
			int total = validInvalidIdsMap.get(ApplicationConstants.INVALID).size()
					+ validInvalidIdsMap.get(ApplicationConstants.VALID).size();
			response.setSuccessRecordsIds(validInvalidIdsMap.get(ApplicationConstants.INVALID).isEmpty() ? ""
					: String.valueOf(validInvalidIdsMap.get(ApplicationConstants.VALID)));
			response.setTotalRecords(String.valueOf(total));
			if (null != validInvalidIdsMap.get(ApplicationConstants.RECORD_NOT_EXIST)
					&& !validInvalidIdsMap.get(ApplicationConstants.RECORD_NOT_EXIST).isEmpty()) {
				response.setIdNotExist("ID's" + validInvalidIdsMap.get(ApplicationConstants.RECORD_NOT_EXIST)
						+ " doesn’t exist in database and not processed");
			}
			response.setMessage(ApplicationConstants.SOME_UPDATED_SOME_FAILED);
		} else {
			response.setCode(String.valueOf(HttpStatus.OK));
			int total = validInvalidIdsMap.get(ApplicationConstants.VALID).size();
			response.setTotalRecords(String.valueOf(total));
			response.setSuccessRecordsIds(validInvalidIdsMap.get(ApplicationConstants.INVALID).isEmpty() ? ""
					: String.valueOf(validInvalidIdsMap.get(ApplicationConstants.VALID)));
			response.setMessage(ApplicationConstants.UPDATED_SUCCESSFULLY);
		}
		return response;
	}
}