package com.epam.esm.core.dao.impl;


import com.epam.esm.core.dao.Column;
import com.epam.esm.core.dao.GiftCertificateDao;
import com.epam.esm.core.dao.Table;
import com.epam.esm.core.dao.mapper.GiftCertificateMapper;
import com.epam.esm.core.dao.query.JoinType;
import com.epam.esm.core.dao.query.LogicOperator;
import com.epam.esm.core.dao.query.QueryBuilder;
import com.epam.esm.core.model.Pair;
import com.epam.esm.core.model.domain.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static com.epam.esm.core.dao.Column.*;
import static com.epam.esm.core.dao.query.OrderType.*;


/**
 * The type Gift certificate dao.
 */
@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {
    private static final String LIKE_FORMAT = "%%%s%%";
    private static final String SORT_DELIMITER = ", ";
    private static final String SORT_BY_NAME_REGEX = "-?name";
    private static final String SORT_BY_DATE_REGEX = "-?date";
    private static final String MINUS_SIGN = "-";

    private static String BASIC_SELECT_QUERY;
    private static String INSERT_QUERY;
    private static String SELECT_ALL_QUERY;
    private static String SELECT_BY_ID_QUERY;
    private static String SELECT_BY_NAME_QUERY;
    private static String DELETE_BY_ID_QUERY;
    private static String INSERT_MANY_TO_MANY_TABLE_QUERY;
    private static String SELECT_BY_ID_FROM_MANY_TO_MANY_TABLE;

    private final JdbcTemplate jdbcTemplate;
    private final GiftCertificateMapper mapper;
    private final QueryBuilder queryBuilder;

    /**
     * Instantiates a new Gift certificate dao.
     *
     * @param jdbcTemplate the jdbc template
     * @param mapper       the mapper
     * @param queryBuilder the query builder
     */
    @Autowired
    public GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate, GiftCertificateMapper mapper, QueryBuilder queryBuilder) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
        this.queryBuilder = queryBuilder;
    }

    @PostConstruct
    private void postConstruct() {
        INSERT_QUERY = queryBuilder.insert(Table.GIFT_CERTIFICATES)
                .setAllColumns(Table.GIFT_CERTIFICATES)
                .toString();
        BASIC_SELECT_QUERY = queryBuilder.select()
                .setAllColumns(Table.GIFT_CERTIFICATES)
                .groupConcat(QueryBuilder.concat(TAG_ID,
                        TAGS_CONCAT_DELIMITER,
                        TAG_NAME), Table.TAGS.toString())
                .from(Table.GIFT_CERTIFICATES)
                .join(JoinType.LEFT, Table.GIFT_CERTIFICATE_HAS_TAG)
                .on(GC_ID, GC_HAS_TAG_GC_ID)
                .join(JoinType.LEFT, Table.TAGS).on(GC_HAS_TAG_TAG_ID, TAG_ID).
                toString();
        SELECT_ALL_QUERY = BASIC_SELECT_QUERY + queryBuilder.clear().groupBy(GC_ID).toString();
        SELECT_BY_ID_QUERY = BASIC_SELECT_QUERY + queryBuilder.clear().where(GC_ID, LogicOperator.EQUALS)
                .groupBy(GC_ID).toString();
        SELECT_BY_NAME_QUERY = BASIC_SELECT_QUERY + queryBuilder.clear().where(GC_NAME, LogicOperator.EQUALS)
                .groupBy(GC_ID).toString();
        DELETE_BY_ID_QUERY = queryBuilder.delete().from(Table.GIFT_CERTIFICATES)
                .where(GC_ID, LogicOperator.EQUALS).toString();
        INSERT_MANY_TO_MANY_TABLE_QUERY = queryBuilder.insert(Table.GIFT_CERTIFICATE_HAS_TAG)
                .setAllColumns(Table.GIFT_CERTIFICATE_HAS_TAG)
                .toString();
        SELECT_BY_ID_FROM_MANY_TO_MANY_TABLE = queryBuilder.select()
                .setAllColumns(Table.GIFT_CERTIFICATE_HAS_TAG)
                .from(Table.GIFT_CERTIFICATE_HAS_TAG)
                .where(GC_HAS_TAG_GC_ID, LogicOperator.EQUALS)
                .and(GC_HAS_TAG_TAG_ID, LogicOperator.EQUALS)
                .toString();
    }

    @Override
    public GiftCertificate create(GiftCertificate giftCertificate) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime localDateTime = LocalDateTime.now();
        Timestamp now = Timestamp.valueOf(localDateTime);
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, giftCertificate.getName());
            statement.setString(2, giftCertificate.getDescription());
            statement.setBigDecimal(3, giftCertificate.getPrice());
            statement.setInt(4, giftCertificate.getDuration());
            statement.setTimestamp(5, now);
            statement.setTimestamp(6, now);
            return statement;
        }, keyHolder);
        giftCertificate.setCreateDate(localDateTime);
        giftCertificate.setLastUpdateDate(localDateTime);
        giftCertificate.setId(keyHolder.getKey().longValue());
        return giftCertificate;
    }

    @Override
    public Optional<GiftCertificate> update(GiftCertificate entity) {
        long id = entity.getId();
        Map<Column, Object> columnsMap = getUpdatedFields(entity);
        Column[] columns = columnsMap.keySet().toArray(new Column[0]);
        String updateQuery = queryBuilder
                .update(Table.GIFT_CERTIFICATES)
                .setAllColumns(columns)
                .where(GC_ID, LogicOperator.EQUALS)
                .toString();

        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(updateQuery);
            Collection<Object> values = columnsMap.values();
            int num = 1;
            for (Object value : values) {
                statement.setObject(num++, value);
            }
            statement.setLong(num, id);
            return statement;
        });

        return this.findById(id);
    }

    @Override
    public List<GiftCertificate> findAll() {
        return jdbcTemplate.query(SELECT_ALL_QUERY, mapper);
    }

    @Override
    public Optional<GiftCertificate> findById(long id) {
        return jdbcTemplate.query(SELECT_BY_ID_QUERY, mapper, id).stream().findAny();
    }

    @Override
    public Optional<GiftCertificate> findByName(String name) {
        return jdbcTemplate.query(SELECT_BY_NAME_QUERY, mapper, name).stream().findFirst();
    }

    @Override
    public boolean deleteById(long id) {
        return jdbcTemplate.update(DELETE_BY_ID_QUERY, id) == 1;
    }

    @Override
    public boolean createManyToManyLink(long giftCertificateId, long tagId) {
        return jdbcTemplate.update(INSERT_MANY_TO_MANY_TABLE_QUERY, giftCertificateId, tagId) == 1;
    }

    @Override
    public boolean isManyToManyLinkExist(long giftCertificateId, long tagId) {
        return jdbcTemplate.queryForRowSet(SELECT_BY_ID_FROM_MANY_TO_MANY_TABLE, giftCertificateId, tagId).next();
    }

    @Override
    public List<GiftCertificate> findAllByParameters(String tag, String name, String description, String sort) {
        if (tag == null && name == null && description == null && sort == null) {
            return this.findAll();
        }
        Pair<String, List<String>> selectQuery = buildSelectQuery(tag, name, description, sort);

        return jdbcTemplate.query(selectQuery.getFirst(), mapper, selectQuery.getSecond().toArray());
    }

    private Pair<String, List<String>> buildSelectQuery(String tag, String name, String description, String sort) {
        QueryBuilder selectQuery = new QueryBuilder(BASIC_SELECT_QUERY);
        List<String> values = new ArrayList<>();
        if (name != null) {
            selectQuery.where(GC_NAME).like();
            values.add(String.format(LIKE_FORMAT, name));
        }
        if (description != null) {
            if (name != null) {
                selectQuery.or(GC_DESCRIPTION);
            } else {
                selectQuery.where(GC_DESCRIPTION);
            }
            selectQuery.like();
            values.add(String.format(LIKE_FORMAT, description));
        }
        selectQuery.groupBy(GC_ID);
        if (tag != null) {
            selectQuery.having(Table.TAGS.toString()).like();
            values.add(String.format(LIKE_FORMAT, tag));
        }
        if (sort != null) {
            Arrays.stream(sort.split(SORT_DELIMITER)).forEach(s -> {
                if (s.matches(SORT_BY_NAME_REGEX)) {
                    selectQuery.orderBy(GC_NAME, s.startsWith(MINUS_SIGN) ? DESC : ASC);
                } else if (s.matches(SORT_BY_DATE_REGEX)) {
                    selectQuery.orderBy(GC_CREATE_DATE, s.startsWith(MINUS_SIGN) ? DESC : ASC);
                }
            });
        }
        return Pair.of(selectQuery.toString(), values);
    }

}
