package com.etnetera.hr.service.search;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SearchCriteria {
    private String key;
    private Object value;
    private SearchOperation operation;

    public SearchCriteria() {
    }

    public SearchCriteria(String key, String operation, Object value) {
        validate(key, operation, value);
        this.key = key;
        this.value = value;
        this.operation = SearchOperation.from(operation);
    }

    private void validate(String key, String operation, Object value) {
        if ("deprecationDate".equals(key)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Search by deprecationDate not implemented.");
        }
        if (key.equals("name") && (">".equals(operation) || "<".equals(operation))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name search with operators >, < not supported.");
        }
    }

    public java.lang.String getKey() {
        return key;
    }

    public void setKey(java.lang.String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public SearchOperation getOperation() {
        return operation;
    }

    public void setOperation(SearchOperation operation) {
        this.operation = operation;
    }
}
