package com.epam.esm.core.dao;

import com.epam.esm.core.model.domain.GiftCertificate;

import java.time.LocalDateTime;
import java.util.*;

/**
 * The enum Column.
 */
public enum Column {
    /**
     * Gc id column.
     */
    GC_ID("id", Type.GENERATED),
    /**
     * Gc name column.
     */
    GC_NAME,
    /**
     * Gc description column.
     */
    GC_DESCRIPTION,
    /**
     * Gc price column.
     */
    GC_PRICE,
    /**
     * Gc duration column.
     */
    GC_DURATION,
    /**
     * Gc create date column.
     */
    GC_CREATE_DATE("create_date"),
    /**
     * Gc update date column.
     */
    GC_UPDATE_DATE("last_update_date"),

    /**
     * Tag id column.
     */
    TAG_ID("id", Type.GENERATED),
    /**
     * Tag name column.
     */
    TAG_NAME,

    /**
     * Gc has tag gc id column.
     */
    GC_HAS_TAG_GC_ID("gift_certificate_id"),
    /**
     * Gc has tag tag id column.
     */
    GC_HAS_TAG_TAG_ID("tag_id");

    private final String name;
    private Type type = Type.REQUIRED;

    Column(String name) {
        this.name = name;
    }

    Column(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    /**
     * The constant TAGS_GROUP_CONCAT_DELIMITER.
     */
    public static final String TAGS_GROUP_CONCAT_DELIMITER = ",";
    /**
     * The constant TAGS_CONCAT_DELIMITER.
     */
    public static final String TAGS_CONCAT_DELIMITER = ":";
    private static final String UNDERSCORE = "_";

    Column() {
        this.name = this.name().substring(this.name().lastIndexOf(UNDERSCORE) + 1).toLowerCase();
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public Type getType() {
        return type;
    }

    /**
     * Gets full name.
     *
     * @return the full name
     */
    public String getFullName() {
        return formatName(getTable().toString());
    }

    /**
     * Gets table.
     *
     * @return the table
     */
    public Table getTable() {
        Optional<Table> table = Arrays.stream(Table.values())
                .filter(t -> t.getColumns()
                        .contains(this))
                .findAny();
        return table.orElse(null);
    }

    private String formatName(String table) {
        return String.format("%s.%s", table, name);
    }

    /**
     * The enum Type.
     */
    public enum Type {
        /**
         * Generated type.
         */
        GENERATED,
        /**
         * Required type.
         */
        REQUIRED
    }

    /**
     * Get updated fields map.
     *
     * @param giftCertificate the gift certificate
     * @return the map
     */
    public static Map<Column, Object> getUpdatedFields(GiftCertificate giftCertificate){
        Map<Column, Object> columns = new EnumMap<>(Column.class);
        if (giftCertificate.getName() != null){
            columns.put(GC_NAME, giftCertificate.getName());
        }
        if (giftCertificate.getDescription() != null){
            columns.put(GC_DESCRIPTION, giftCertificate.getDescription());
        }
        if (giftCertificate.getPrice() != null){
            columns.put(GC_PRICE, giftCertificate.getPrice());
        }
        if (giftCertificate.getDuration() != null){
            columns.put(GC_DURATION, giftCertificate.getDuration());
        }
        if (!columns.isEmpty()){
            columns.put(GC_UPDATE_DATE, LocalDateTime.now());
        }
        return columns;
    }
}
