package com.pepcus.appstudent.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.pepcus.appstudent.convertor.CoordinatorEntityConvertor;
import com.pepcus.appstudent.entity.Coordinator;
import com.pepcus.appstudent.entity.Department;
import com.pepcus.appstudent.exception.BadRequestException;
import com.pepcus.appstudent.exception.ResourceNotFoundException;
import com.pepcus.appstudent.repository.CoordinatorRepository;
import com.pepcus.appstudent.specifications.CoordinatorSpecification;
import com.pepcus.appstudent.util.CommonUtil;
import com.pepcus.appstudent.util.ErrorMessageConstants;

@Service
public class CoordinatorService {

	@Autowired
	CoordinatorRepository coordinatorRepository;
	
	@Autowired
	DepartmentService departmentService;

	@Autowired
	CoordinatorDepartmentService coordinatorDepartmentService;

	/**
	 * Method to persist coordinator entity
	 * 
	 * @param coordinatorEntity
	 * @return
	 */
	private Coordinator persistCoordinatorEntity(Coordinator coordinatorEntity) {
		return coordinatorRepository.save(coordinatorEntity);
	}

	/**
	 * Method to get coordinator entity by id
	 * 
	 * @param id
	 * @return
	 */
	private Coordinator getCoordinatorEntity(Integer id) {
		Coordinator coordinatorEntity = coordinatorRepository.findOne(id);
		if (null == coordinatorEntity) {
			throw new ResourceNotFoundException("No Coordinator found for id : " + id);
		}
		return coordinatorEntity;
	}

	private List<Coordinator> getCoordinatorEntityList(String firstName, String lastName, String dob) {
		return coordinatorRepository.findAll(new CoordinatorSpecification(firstName, lastName, dob));
	}

	private List<Coordinator> getCoordinatorEntityList() {
		return coordinatorRepository.findAll();
	}

	/**
	 * Method to create/register new coordinator
	 * 
	 * @param request
	 */
	public Coordinator createCoordinator(Coordinator request) {

		// check coordinator for duplicate
		List<Coordinator> coordinators = getCoordinatorEntityList(request.getFirstName(), request.getLastName(),
				request.getDob());
		if (CollectionUtils.isNotEmpty(coordinators)) {
			Integer duplicateCoordinatorId = coordinators.stream().findFirst().get().getId();
			throw new BadRequestException(
					ErrorMessageConstants.ALREADY_REGISTRATION + "{" + duplicateCoordinatorId + "}", 1000);
		}

		Map<Integer, Department> departmentMap = departmentService.getDepartmentMap();
		Coordinator coordinatorEntity = CoordinatorEntityConvertor.convertCoordinatorEntity(request);
		String secretKey = CommonUtil.generateSecretKey();
		coordinatorEntity.setSecretKey(secretKey);

		coordinatorEntity = persistCoordinatorEntity(coordinatorEntity);
		
		if(CollectionUtils.isNotEmpty(request.getInterestedDepartments())){
			CoordinatorEntityConvertor.convertAndSetInterestedDepartmentsInEntity(departmentMap, coordinatorEntity, request.getInterestedDepartments());
			coordinatorDepartmentService.persistInterestedDepartmentEntitySet(coordinatorEntity.getInterestedDepartments());
		}
		
		if(CollectionUtils.isNotEmpty(request.getAssignedDepartments())){
			CoordinatorEntityConvertor.convertAndSetAssignedDepartmentInEntity(departmentMap, coordinatorEntity, request.getAssignedDepartments());
			coordinatorDepartmentService.persistAssignedDepartmentEntitySet(coordinatorEntity.getAssignedDepartments());
		}
		
		return CoordinatorEntityConvertor.setDateAndDepartmentsInCoordinator(coordinatorEntity);
	}

	/**
	 * Method to get coordinator details/information by id
	 * 
	 * @param id
	 * @return
	 */
	public Coordinator getCoordinator(Integer id) {
		Coordinator coordinatorEntity = getCoordinatorEntity(id);
		return CoordinatorEntityConvertor.setDateAndDepartmentsInCoordinator(coordinatorEntity);
	}

	/**
	 * Method to get coordinator
	 * 
	 * @param allRequestParams
	 * @return
	 */
	public List<Coordinator> getCoordinators(Map<String,String> allRequestParams) {

		List<Coordinator> coordinators = new ArrayList<Coordinator>();

		if (allRequestParams.isEmpty()) {
			coordinators = getCoordinatorEntityList();
		} else {
			Specification<Coordinator> spec = new CoordinatorSpecification(allRequestParams);
			coordinators = coordinatorRepository.findAll(spec);
		}

		return CoordinatorEntityConvertor.setDateAndDepartmentsInCoordinators(coordinators);
	}

	/**
	 * Method to update coordinator record
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	public Coordinator updateCoordinator(Integer id, Coordinator request) {

		// Get coordinator entity by id
		Coordinator coordinatorEntity = getCoordinatorEntity(id);

		// check coordinator for duplicate
		List<Coordinator> coordinators = getCoordinatorEntityList(request.getFirstName(), request.getLastName(),
				request.getDob());
		if (CollectionUtils.isNotEmpty(coordinators)) {
			Integer duplicateCoordinatorId = coordinators.stream().findFirst().get().getId();
			if (duplicateCoordinatorId.intValue() != coordinatorEntity.getId().intValue()) {
				throw new BadRequestException(
						ErrorMessageConstants.ALREADY_REGISTRATION + "{" + duplicateCoordinatorId + "}", 1000);
			}
		}

		Map<Integer, Department> departmentMap = departmentService.getDepartmentMap();
		coordinatorEntity = CoordinatorEntityConvertor.convertCoordinatorEntity(coordinatorEntity, request);
		coordinatorEntity = persistCoordinatorEntity(coordinatorEntity);
		
		
		if(request.getInterestedDepartments() != null){
			coordinatorDepartmentService.deleteCoordinatorInterestedDepartments(coordinatorEntity);
			CoordinatorEntityConvertor.convertAndSetInterestedDepartmentsInEntity(departmentMap, coordinatorEntity, request.getInterestedDepartments());
			coordinatorDepartmentService.persistInterestedDepartmentEntitySet(coordinatorEntity.getInterestedDepartments());
		}
		
		if(request.getAssignedDepartments() != null){
			coordinatorDepartmentService.deleteCoordinatorAssignedDepartments(coordinatorEntity);
			CoordinatorEntityConvertor.convertAndSetAssignedDepartmentInEntity(departmentMap, coordinatorEntity, request.getAssignedDepartments());
			coordinatorDepartmentService.persistAssignedDepartmentEntitySet(coordinatorEntity.getAssignedDepartments());
		}
		
		return CoordinatorEntityConvertor.setDateAndDepartmentsInCoordinator(coordinatorEntity);
	}

	/**
	 * Method to delete coordinator by id
	 * @param id
	 * @return
	 */
	public boolean deleteCoordinator(Integer id) {
		Coordinator coordinatorEntity = getCoordinatorEntity(id);
		try {
			coordinatorDepartmentService.deleteCoordinatorInterestedDepartments(coordinatorEntity);
			coordinatorDepartmentService.deleteCoordinatorAssignedDepartments(coordinatorEntity);
			coordinatorRepository.delete(coordinatorEntity);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
