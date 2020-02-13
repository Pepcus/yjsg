package com.pepcus.appstudent.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.pepcus.appstudent.entity.Student;

public class StudentSpecification {

	private StudentSpecification() {
	}

	public static Specification<Student> getStudents(String name, String fatherName,
			String fatherMobileNumber) {

		return new Specification<Student>() {
			@Override
			public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				if (StringUtils.isNotEmpty(name)) {
					predicates.add(cb.like(cb.lower(root.get("name")), name));
				}
				if (StringUtils.isNotEmpty(fatherName)) {
					predicates.add(cb.like(cb.lower(root.get("fatherName")), fatherName));
				}
				if (StringUtils.isNotEmpty(fatherMobileNumber)) {
					predicates.add(cb.like(cb.lower(root.get("mobile")), fatherMobileNumber));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};

	}

}
