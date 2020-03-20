package com.pepcus.appstudent.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;

import com.pepcus.appstudent.entity.CoordinatorInterestedDepartment;
import com.pepcus.appstudent.entity.Department;

public class InterestedDepartmentSpecification {

	private InterestedDepartmentSpecification() {
	}

	public static Specification<CoordinatorInterestedDepartment> getCoordinatorInterestedDepartments(
			List<Department> departments) {

		return new Specification<CoordinatorInterestedDepartment>() {
			@Override
			public Predicate toPredicate(Root<CoordinatorInterestedDepartment> root, CriteriaQuery<?> query,
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
