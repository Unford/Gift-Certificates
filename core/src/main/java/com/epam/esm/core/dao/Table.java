package com.epam.esm.core.dao;


import java.util.Arrays;
import java.util.EnumSet;

import static com.epam.esm.core.dao.Column.*;

/**
 * The enum Table.
 */
public enum Table {
    /**
     * Gift certificates table.
     */
    GIFT_CERTIFICATES(GC_ID, GC_NAME, GC_DESCRIPTION, GC_PRICE, GC_DURATION, GC_CREATE_DATE, GC_UPDATE_DATE),
    /**
     * Tags table.
     */
    TAGS(TAG_ID, TAG_NAME),
    /**
     * Gift certificate has tag table.
     */
    GIFT_CERTIFICATE_HAS_TAG(GC_HAS_TAG_GC_ID, GC_HAS_TAG_TAG_ID);

    private final EnumSet<Column> columns;

    Table(Column... columns){
        this.columns = EnumSet.noneOf(Column.class);
        this.columns.addAll(Arrays.asList(columns));
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    /**
     * Gets columns.
     *
     * @return the columns
     */
    public EnumSet<Column> getColumns() {
        return columns;
    }
}
