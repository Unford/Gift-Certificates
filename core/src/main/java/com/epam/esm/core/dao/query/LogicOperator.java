package com.epam.esm.core.dao.query;

/**
 * The enum Logic operator.
 */
public enum LogicOperator {
    /**
     * Equals logic operator.
     */
    EQUALS("="),
    /**
     * Greater logic operator.
     */
    GREATER(">"),
    /**
     * Less logic operator.
     */
    LESS("<"),
    /**
     * Greater or equals logic operator.
     */
    GREATER_OR_EQUALS(">="),
    /**
     * Less or equals logic operator.
     */
    LESS_OR_EQUALS("<="),
    /**
     * Not equals logic operator.
     */
    NOT_EQUALS("<>");

    private final String operator;

    LogicOperator(String operator) {
        this.operator = operator;
    }

    /**
     * Gets operator.
     *
     * @return the operator
     */
    public String getOperator() {
        return operator;
    }

    public String toString() {
        return getOperator();
    }
}
