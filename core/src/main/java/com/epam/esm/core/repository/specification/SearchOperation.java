package com.epam.esm.core.repository.specification;

import java.util.Arrays;
import java.util.Optional;


/**
 * An enum that is used to parse the operation from the query string to specification.
 */
public enum SearchOperation {
    GREATER_THAN("gt"),
    LESS_THAN("lt"),
    GREATER_THAN_EQUAL("gte"),
    LESS_THAN_EQUAL("lte"),
    EQUAL("eq"),
    LIKE("lk"),
    IN("in");

    private final String operation;

    SearchOperation(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }

    /**
     * It takes a string and returns an Optional of SearchOperation
     *
     * @param operation The operation to be performed.
     * @return Optional of search operation that match the operation parameter.
     */
    public static Optional<SearchOperation> parseOperation(String operation) {
        return Arrays.stream(SearchOperation.values())
                .filter(searchOperation -> searchOperation.getOperation().equals(operation))
                .findFirst();

    }
}
