package com.epam.esm.core.dao.specification;

import java.util.List;

public class SearchCriteria {
    private String key;
    private String value;
    private SearchOperation operation;
    private boolean isNot;

    private List<String> joinTables;


    public SearchCriteria(String key, String value, SearchOperation operation) {
        this.key = key;
        this.value = value;
        this.operation = operation;
    }

    public SearchCriteria(String key, String value, SearchOperation operation, boolean isNot) {
        this.key = key;
        this.value = value;
        this.operation = operation;
        this.isNot = isNot;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SearchOperation getOperation() {
        return operation;
    }

    public void setOperation(SearchOperation operation) {
        this.operation = operation;
    }

    public boolean isNot() {
        return isNot;
    }

    public void setNot(boolean not) {
        isNot = not;
    }

    public List<String> getJoinTables() {
        return joinTables;
    }

    public void setJoinTables(List<String> joinTables) {
        this.joinTables = joinTables;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SearchCriteria{");
        sb.append("key='").append(key).append('\'');
        sb.append(", value=").append(value);
        sb.append(", operation=").append(operation);
        sb.append(", isNot=").append(isNot);
        sb.append('}');
        return sb.toString();
    }


}
