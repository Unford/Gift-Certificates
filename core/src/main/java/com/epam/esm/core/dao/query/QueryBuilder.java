package com.epam.esm.core.dao.query;

import com.epam.esm.core.dao.Column;
import com.epam.esm.core.dao.Table;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The type Query builder.
 */
@Component
public class QueryBuilder {
    private static final String SELECT = "SELECT ";
    private static final String UPDATE = "UPDATE ";
    private static final String INSERT_INTO = "INSERT INTO ";
    private static final String DELETE = "DELETE ";

    private static final String HAVING = " HAVING ";
    private static final String WHERE = " WHERE ";
    private static final String OR = " OR ";
    private static final String AND = " AND ";

    private static final String LIKE = " LIKE ";
    private static final String AS = " AS ";

    private static final String FROM = " FROM ";
    private static final String VALUES = "VALUES";
    private static final String ORDER_BY = " ORDER BY ";
    private static final String GROUP_BY = " GROUP BY ";

    private static final String GROUP_CONCAT = " GROUP_CONCAT";
    private static final String CONCAT = " CONCAT";
    private static final String SET = " SET ";


    private static final String JOIN = " JOIN ";
    private static final String ON = " ON ";

    private static final char WHITESPACE = ' ';
    private static final char COMMA = ',';
    private static final String QUESTION_SIGN = "?";
    private static final String EQUAL_SIGN = "=";
    private static final String OPEN_BRACKET = "(";
    private static final String CLOSE_BRACKET = ") ";

    private static final String EMPTY_STRING = "";

    private enum OperationType {
        /**
         * Select operation type.
         */
        SELECT,
        /**
         * Update operation type.
         */
        UPDATE,
        /**
         * Delete operation type.
         */
        DELETE,
        /**
         * Insert operation type.
         */
        INSERT,
        /**
         * Default operation type.
         */
        DEFAULT
    }

    private final StringBuilder stringBuilder = new StringBuilder();
    private OperationType operationType = OperationType.DEFAULT;
    private final List<String> values = new ArrayList<>();
    private boolean isFirstOrderBy = true;

    /**
     * Instantiates a new Query builder.
     */
    public QueryBuilder() {
    }

    /**
     * Instantiates a new Query builder.
     *
     * @param query the query
     */
    public QueryBuilder(String query){
        this.stringBuilder.append(query);
    }

    /**
     * Select query builder.
     *
     * @return the query builder
     */
    public QueryBuilder select() {
        clear();
        operationType = OperationType.SELECT;
        stringBuilder.append(SELECT);
        return this;
    }

    /**
     * Update query builder.
     *
     * @param table the table
     * @return the query builder
     */
    public QueryBuilder update(Table table) {
        clear();
        operationType = OperationType.UPDATE;
        stringBuilder.append(UPDATE).append(table).append(SET);
        return this;
    }

    /**
     * Delete query builder.
     *
     * @return the query builder
     */
    public QueryBuilder delete() {
        clear();
        operationType = OperationType.DELETE;
        stringBuilder.append(DELETE);
        return this;
    }

    /**
     * Insert query builder.
     *
     * @param table the table
     * @return the query builder
     */
    public QueryBuilder insert(Table table) {
        clear();
        operationType = OperationType.INSERT;
        stringBuilder.append(INSERT_INTO).append(table).append(OPEN_BRACKET);
        return this;
    }


    /**
     * Sets column.
     *
     * @param column the column
     * @return the column
     */
    public QueryBuilder setColumn(String column) {
        values.add(column);
        return this;
    }

    /**
     * Sets column.
     *
     * @param column the column
     * @return the column
     */
    public QueryBuilder setColumn(Column column) {
        return setColumn(operationType == OperationType.INSERT ? column.getName() : column.getFullName());
    }

    /**
     * Sets all columns.
     *
     * @param columns the columns
     * @return the all columns
     */
    public QueryBuilder setAllColumns(Column... columns) {
        Arrays.stream(columns).forEach(this::setColumn);
        return this;
    }

    /**
     * Sets all columns.
     *
     * @param table the table
     * @return the all columns
     */
    public QueryBuilder setAllColumns(Table table) {
        Column[] columns = operationType != OperationType.SELECT ? table.getColumns()
                .stream()
                .filter(column -> column.getType() != Column.Type.GENERATED)
                .toArray(Column[]::new) : table.getColumns().toArray(new Column[0]);
        setAllColumns(columns);
        return this;
    }

    /**
     * Join query builder.
     *
     * @param type  the type
     * @param table the table
     * @return the query builder
     */
    public QueryBuilder join(JoinType type, Table table) {
        stringBuilder.append(WHITESPACE).append(type == JoinType.INNER ? EMPTY_STRING : type)
                .append(JOIN)
                .append(table);
        return this;
    }

    /**
     * Join query builder.
     *
     * @param table the table
     * @return the query builder
     */
    public QueryBuilder join(Table table) {
        return join(JoinType.INNER, table);
    }

    /**
     * On query builder.
     *
     * @param leftColumn  the left column
     * @param rightColumn the right column
     * @return the query builder
     */
    public QueryBuilder on(Column leftColumn, Column rightColumn) {
        return on(leftColumn.getFullName(), rightColumn.getFullName());
    }

    /**
     * On query builder.
     *
     * @param leftColumn  the left column
     * @param rightColumn the right column
     * @return the query builder
     */
    public QueryBuilder on(String leftColumn, String rightColumn) {
        stringBuilder.append(ON).append(OPEN_BRACKET)
                .append(leftColumn).append(EQUAL_SIGN)
                .append(rightColumn).append(CLOSE_BRACKET);
        return this;
    }

    /**
     * From query builder.
     *
     * @param table the table
     * @return the query builder
     */
    public QueryBuilder from(Table table) {
        if (operationType == OperationType.SELECT) {
            values.forEach(v -> stringBuilder.append(WHITESPACE).append(v).append(COMMA));
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        stringBuilder.append(FROM).append(table);
        return this;
    }

    /**
     * Where query builder.
     *
     * @param column   the column
     * @param operator the operator
     * @param value    the value
     * @return the query builder
     */
    public QueryBuilder where(Column column, LogicOperator operator, String value) {
        if (operationType == OperationType.UPDATE) {
            values.forEach(v -> stringBuilder
                    .append(WHITESPACE)
                    .append(v)
                    .append(EQUAL_SIGN)
                    .append(QUESTION_SIGN)
                    .append(COMMA));
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        stringBuilder.append(WHERE)
                .append(column.getFullName())
                .append(operator)
                .append(value);
        return this;
    }

    /**
     * Where query builder.
     *
     * @param column   the column
     * @param operator the operator
     * @return the query builder
     */
    public QueryBuilder where(Column column, LogicOperator operator) {
        return where(column, operator, QUESTION_SIGN);
    }

    /**
     * Where query builder.
     *
     * @param column the column
     * @return the query builder
     */
    public QueryBuilder where(Column column) {
        stringBuilder.append(WHERE).append(column.getFullName());
        return this;
    }

    /**
     * Or query builder.
     *
     * @param column the column
     * @return the query builder
     */
    public QueryBuilder or(Column column) {
        stringBuilder.append(OR).append(column.getFullName());
        return this;
    }

    /**
     * And query builder.
     *
     * @param column   the column
     * @param operator the operator
     * @return the query builder
     */
    public QueryBuilder and(Column column, LogicOperator operator) {
        stringBuilder.append(AND).append(column.getFullName())
                .append(operator)
                .append(QUESTION_SIGN);
        return this;
    }

    /**
     * Like query builder.
     *
     * @return the query builder
     */
    public QueryBuilder like() {
        stringBuilder.append(LIKE).append(QUESTION_SIGN);
        return this;
    }

    /**
     * Order by query builder.
     *
     * @param col  the col
     * @param type the type
     * @return the query builder
     */
    public QueryBuilder orderBy(String col, OrderType type) {
        stringBuilder.append(isFirstOrderBy ? ORDER_BY : COMMA).append(col).append(WHITESPACE)
                .append(type == OrderType.DESC ? type : EMPTY_STRING);
        isFirstOrderBy = false;
        return this;
    }

    /**
     * Order by query builder.
     *
     * @param column the column
     * @return the query builder
     */
    public QueryBuilder orderBy(Column column) {
        return orderBy(column.getFullName(), OrderType.ASC);
    }


    /**
     * Order by query builder.
     *
     * @param column the column
     * @param type   the type
     * @return the query builder
     */
    public QueryBuilder orderBy(Column column, OrderType type) {
        return orderBy(column.getFullName(), type);
    }

    /**
     * Having query builder.
     *
     * @param column the column
     * @return the query builder
     */
    public QueryBuilder having(Column column) {
        return having(column.getFullName());
    }

    /**
     * Having query builder.
     *
     * @param column the column
     * @return the query builder
     */
    public QueryBuilder having(String column) {
        stringBuilder.append(HAVING).append(column);
        return this;
    }

    /**
     * Group concat query builder.
     *
     * @param column the column
     * @param as     the as
     * @return the query builder
     */
    public QueryBuilder groupConcat(String column, String as) {
        StringBuilder builder = new StringBuilder(GROUP_CONCAT)
                .append(OPEN_BRACKET)
                .append(column).append(CLOSE_BRACKET)
                .append(AS).append(as);
        values.add(builder.toString());
        return this;
    }

    /**
     * Concat string.
     *
     * @param columns the columns
     * @return the string
     */
    public static String concat(String... columns) {
        StringBuilder builder = new StringBuilder();
        builder.append(CONCAT).append(OPEN_BRACKET);
        Arrays.stream(columns).forEach(c -> builder.append(c).append(COMMA));
        builder.deleteCharAt(builder.length() - 1);
        builder.append(CLOSE_BRACKET);
        return builder.toString();
    }

    /**
     * Concat string.
     *
     * @param first     the first
     * @param delimiter the delimiter
     * @param second    the second
     * @return the string
     */
    public static String concat(Column first, String delimiter, Column second) {
        return concat(first.getFullName(), String.format("'%s'", delimiter), second.getFullName());
    }


    /**
     * Group by query builder.
     *
     * @param column the column
     * @return the query builder
     */
    public QueryBuilder groupBy(Column column) {
        stringBuilder.append(GROUP_BY).append(column.getFullName());
        return this;
    }


    @Override
    public String toString() {
        if (operationType == OperationType.INSERT) {
            values.forEach(v -> stringBuilder.append(WHITESPACE).append(v).append(COMMA));
            stringBuilder.deleteCharAt(stringBuilder.length() - 1)
                    .append(CLOSE_BRACKET).append(VALUES).append(OPEN_BRACKET);
            values.forEach(v -> stringBuilder.append(QUESTION_SIGN).append(COMMA));
            stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), CLOSE_BRACKET);
        }
        return stringBuilder.toString();
    }

    /**
     * Clear query builder.
     *
     * @return the query builder
     */
    public QueryBuilder clear() {
        stringBuilder.setLength(0);
        values.clear();
        isFirstOrderBy = true;
        operationType = OperationType.DEFAULT;
        return this;
    }
}
