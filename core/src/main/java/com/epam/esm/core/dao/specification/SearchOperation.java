package com.epam.esm.core.dao.specification;

import java.util.Arrays;
import java.util.Optional;

public enum SearchOperation {
    GREATER_THAN("gt"),
    LESS_THAN("lt"),
    GREATER_THAN_EQUAL("gte"),
    LESS_THAN_EQUAL("lte"),
    EQUAL("eq"),
    LIKE("lk");

    private final String operation;

    SearchOperation(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }

    public static Optional<SearchOperation> parseOperation(String operation) {
        return Arrays.stream(SearchOperation.values())
                .filter(searchOperation -> searchOperation.getOperation().equals(operation))
                .findFirst();

    }
}
