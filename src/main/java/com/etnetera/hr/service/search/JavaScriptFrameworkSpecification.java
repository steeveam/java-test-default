package com.etnetera.hr.service.search;

import com.etnetera.hr.data.JavaScriptFramework;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static com.etnetera.hr.service.search.SearchOperation.EQUALS;
import static com.etnetera.hr.service.search.SearchOperation.GREATER_THAN;
import static com.etnetera.hr.service.search.SearchOperation.LESS_THAN;
import static com.etnetera.hr.service.search.SearchOperation.MATCH;

public class JavaScriptFrameworkSpecification implements Specification<JavaScriptFramework> {

    private final List<SearchCriteria> list;

    public JavaScriptFrameworkSpecification(List<SearchCriteria> params) {
        this.list = params;
    }

    @Override
    public Predicate toPredicate(Root<JavaScriptFramework> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        List<Predicate> predicates = new ArrayList<>();

        for (SearchCriteria criteria : list) {
            if (EQUALS.equals(criteria.getOperation())) {
                predicates.add(builder.equal(
                        root.get(criteria.getKey()),
                        criteria.getValue().toString()));
            }
            if (GREATER_THAN.equals(criteria.getOperation())) {
                predicates.add(builder.greaterThan(
                        root.get(criteria.getKey()),
                        criteria.getValue().toString()));
            }
            if (LESS_THAN.equals(criteria.getOperation())) {
                predicates.add(builder.lessThan(
                        root.get(criteria.getKey()),
                        criteria.getValue().toString()));
            }
            if (MATCH.equals(criteria.getOperation())) {
                predicates.add(builder.like(
                        root.get(criteria.getKey()),
                        "%" + criteria.getValue().toString() + "%"));
            }
        }
        return builder.and(predicates.toArray(new Predicate[0]));
    }
}