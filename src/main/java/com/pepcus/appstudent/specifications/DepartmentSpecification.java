package com.pepcus.appstudent.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;

import com.pepcus.appstudent.entity.Department;

public class DepartmentSpecification {

	private DepartmentSpecification() {
	}

	public static Specification<Department> getDepartments(List<String> internalNameList) {

		return new Specification<Department>() {
			@Override
			public Predicate toPredicate(Root<Department> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(internalNameList)) {
					predicates.add(root.get("internalName").in(internalNameList));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

}
