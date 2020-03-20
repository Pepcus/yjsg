package com.pepcus.appstudent.specifications;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.pepcus.appstudent.entity.Coordinator;

public class CoordinatorSpecification implements Specification<Coordinator> {

	String firstName = null;
	String lastName = null;
	String whatsappNumber = null;
	String dob = null;
	Collection<Integer> coordinatorIdList = null;

	public CoordinatorSpecification(Collection<Integer> coordinatorIdList, Map<String, String> searchParams) {
		super();
		for (Entry<String, String> searchParam : searchParams.entrySet()) {
			String value = searchParam.getValue();

			if (searchParam.getKey().equals("firstName")) {
				this.firstName = value;
				continue;
			}
			if (searchParam.getKey().equals("lastName")) {
				this.lastName = value;
				continue;
			}
			if (searchParam.getKey().equals("whatsappNumber")) {
				this.whatsappNumber = value;
				continue;
			}
			if (searchParam.getKey().equals("dob")) {
				this.dob = value;
				continue;
			}
			this.coordinatorIdList = coordinatorIdList;
		}
	}

	public CoordinatorSpecification(String firstName, String lastName, String dob) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.dob = dob;
	}

	@Override
	public Predicate toPredicate(Root<Coordinator> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		List<Predicate> predicates = new ArrayList<>();

		if (StringUtils.isNotEmpty(firstName)) {
			predicates.add(cb.like(cb.lower(root.get("firstName")), firstName.toLowerCase()));
		}
		if (StringUtils.isNotEmpty(lastName)) {
			predicates.add(cb.like(cb.lower(root.get("lastName")), lastName.toLowerCase()));
		}
		if (StringUtils.isNotEmpty(whatsappNumber)) {
			predicates.add(cb.equal(root.get("whatsappNumber"), whatsappNumber));
		}
		if (StringUtils.isNotEmpty(dob)) {
			predicates.add(cb.equal(root.get("dob"), dob));
		}

		if (CollectionUtils.isNotEmpty(coordinatorIdList)) {
			predicates.add(root.get("id").in(coordinatorIdList));
			// predicates.add(root.get("assignedDepartments").get("department").get("internalName").in(assignedDepartmentNameList));
		}
		return cb.and(predicates.toArray(new Predicate[predicates.size()]));
	}
};
