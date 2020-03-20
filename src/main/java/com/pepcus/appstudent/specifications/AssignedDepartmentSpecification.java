package com.pepcus.appstudent.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;

import com.pepcus.appstudent.entity.CoordinatorAssignedDepartment;
import com.pepcus.appstudent.entity.Department;

public class AssignedDepartmentSpecification {

	private AssignedDepartmentSpecification() {
	}

	public static Specification<CoordinatorAssignedDepartment> getCoordinatorAssignedDepartments(
			List<Department> departments) {

		return new Specification<CoordinatorAssignedDepartment>() {
			@Override
			public Predicate toPredicate(Root<CoordinatorAssignedDepartment> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(departments)) {
					predicates.add(root.get("department").in(departments));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};

	}

}
