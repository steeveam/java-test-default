package com.etnetera.hr.service.search;


import static java.util.Arrays.stream;

/**
 * Enum for Search operations.
 *
 * @author Stefan Marcin
 */
public enum SearchOperation {
    EQUALS(":"), MATCH("@"), GREATER_THAN(">"), LESS_THAN("<");

    private final String keyword;

    SearchOperation(String keyword) {
        this.keyword = keyword;
    }

    public static SearchOperation from(String operation) {
        return stream(values()).filter(op -> op.getKeyword().equals(operation)).findFirst().orElse(null);
    }

    public String getKeyword() {
        return keyword;
    }
}
