package com.epam.esm.core.dao.specification;


public class SearchCriteria {
    private String key;
    private Object value;
    private SearchOperation operation;
    private boolean isNot;


    public SearchCriteria(String key, Object value, SearchOperation operation) {
        this.key = key;
        this.value = value;
        this.operation = operation;
    }

    public SearchCriteria(String key, Object value, SearchOperation operation, boolean isNot) {
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

    public boolean isNot() {
        return isNot;
    }

    public void setNot(boolean not) {
        isNot = not;
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
