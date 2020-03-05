package com.pepcus.appstudent.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.pepcus.appstudent.entity.GmsStudent;


/**
 * Specification class for Student GMS
 * 
 * @author Sandeep Vishwakarma
 * @since 05-03-2020
 *
 */
public class GmsStudentSpecification {

	private GmsStudentSpecification() {
	}

	public static Specification<GmsStudent> getStudents(String name, String mobileNumber) {

		return new Specification<GmsStudent>() {
			@Override
			public Predicate toPredicate(Root<GmsStudent> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				if (StringUtils.isNotEmpty(name)) {
					predicates.add(cb.equal(cb.lower(root.get("name")), name));
				}

				if (StringUtils.isNotEmpty(mobileNumber)) {
					predicates.add(cb.equal(cb.lower(root.get("mobile")), mobileNumber));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};

	}

}
