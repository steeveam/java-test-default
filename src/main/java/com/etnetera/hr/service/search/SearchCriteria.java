package com.etnetera.hr.service.search;

public class SearchCriteria {
    private java.lang.String key;
    private Object value;
    private String operation;

    public SearchCriteria() {
    }

    public SearchCriteria(String key, Object value, String operation) {
        this.key = key;
        this.value = value;
        this.operation = operation;
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

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
