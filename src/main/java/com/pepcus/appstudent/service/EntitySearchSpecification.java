package com.pepcus.appstudent.service;

import static com.pepcus.appstudent.util.EntitySearchUtil.isStringField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.pepcus.appstudent.entity.Student;

import lombok.Data;

/**
 * Specification specific to Student
 * 
 * @author Shubham Solanki
 * @since 2018-18-04
 *
 */
@Data
public class EntitySearchSpecification<T extends Student> implements Specification<T> {

    private Map<String, String> searchParameters;
    private T t;

    /**
     * Constructor to create Specification specific to Student
     * 
     * @param searchSpec
     * @param entity
     */
    public EntitySearchSpecification(T entity) {
        super();
        this.t = entity;
    }

    /**
     * Constructor to create Specification specific to Student
     * 
     * @param searchParams
     * @param entity
     */
    public EntitySearchSpecification(Map<String, String> searchParams, T entity) {
        super();
        this.searchParameters = searchParams;
        this.t = entity;
    }

    @Override
    public Predicate toPredicate(Root<T> from, CriteriaQuery<?> criteria, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (searchParameters != null && !searchParameters.isEmpty()) {
            predicates.add(createPredicate(from, criteriaBuilder, searchParameters));
        }

        Predicate[] pr = new Predicate[predicates.size()];
        predicates.toArray(pr);
        return criteriaBuilder.and(pr);

    }

    /**
     * 
     * @param from
     * @param criteriaBuilder
     * @param requestParameters
     * @return filterPredicate
     */
    private Predicate createPredicate(Root<T> from, CriteriaBuilder criteriaBuilder,
            Map<String, String> requestParameters) {

        Predicate filterPredicate = criteriaBuilder.conjunction();
        requestParameters.entrySet().forEach(searchParam -> {
            if (isStringField(t.getClass(), searchParam.getKey())) {
                if (searchParam.getKey().equals("mobile")) {
                    filterPredicate.getExpressions()
                            .add(criteriaBuilder.equal(from.get(searchParam.getKey()), searchParam.getValue()));
                } else {
                    filterPredicate.getExpressions().add(
                            criteriaBuilder.like(from.get(searchParam.getKey()), "%" + searchParam.getValue() + "%"));
                }
            } else {
                filterPredicate.getExpressions()
                        .add(criteriaBuilder.equal(from.get(searchParam.getKey()), searchParam.getValue()));
            }
        });
        return filterPredicate;
    }

}
