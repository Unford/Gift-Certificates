package com.epam.esm.core.repository.specification;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchCriteria)) return false;

        SearchCriteria that = (SearchCriteria) o;

        if (isNot != that.isNot) return false;
        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        return operation == that.operation;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (operation != null ? operation.hashCode() : 0);
        result = 31 * result + (isNot ? 1 : 0);
        return result;
    }
}
