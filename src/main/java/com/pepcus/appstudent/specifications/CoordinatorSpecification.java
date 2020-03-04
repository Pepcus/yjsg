package com.pepcus.appstudent.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.pepcus.appstudent.entity.Coordinator;
import com.pepcus.appstudent.entity.Student;

public class CoordinatorSpecification {
	
	private CoordinatorSpecification() {
	}
	
	public static Specification<Coordinator> getCoordinators(String firstName, String lastName,
			String primaryContactNumber, String dob) {

		return new Specification<Coordinator>() {
			@Override
			public Predicate toPredicate(Root<Coordinator> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				if (StringUtils.isNotEmpty(firstName)) {
					predicates.add(cb.like(cb.lower(root.get("firstName")), "%"+firstName+"%"));
				}
				if (StringUtils.isNotEmpty(lastName)) {
					predicates.add(cb.like(cb.lower(root.get("lastName")), "%"+lastName+"%"));
				}
				if (StringUtils.isNotEmpty(primaryContactNumber)) {
					predicates.add(cb.equal(root.get("primaryContactNumber"), primaryContactNumber));
				}
				if (StringUtils.isNotEmpty(dob)) {
					predicates.add(cb.equal(root.get("dob"), dob));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};

	}

}
