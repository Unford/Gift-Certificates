package com.epam.esm.core.dao.impl;

import com.epam.esm.core.dao.Column;
import com.epam.esm.core.dao.Table;
import com.epam.esm.core.dao.TagDao;
import com.epam.esm.core.dao.mapper.TagRowMapper;
import com.epam.esm.core.dao.query.LogicOperator;
import com.epam.esm.core.dao.query.QueryBuilder;
import com.epam.esm.core.model.domain.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * The type Tag dao.
 */
@Repository
public class TagDaoImpl implements TagDao {
    private static String INSERT_QUERY;
    private static String SELECT_ALL_QUERY;
    private static String SELECT_BY_ID_QUERY;
    private static String SELECT_BY_NAME_QUERY;
    private static String DELETE_BY_ID_QUERY;
    private static String SELECT_BY_ID_FROM_MANY_TO_MANY_TABLE;

    private final JdbcTemplate jdbcTemplate;
    private final TagRowMapper mapper;
    private final QueryBuilder queryBuilder;

    /**
     * Instantiates a new Tag dao.
     *
     * @param jdbcTemplate the jdbc template
     * @param mapper       the mapper
     * @param queryBuilder the query builder
     */
    @Autowired
    public TagDaoImpl(JdbcTemplate jdbcTemplate, TagRowMapper mapper, QueryBuilder queryBuilder) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
        this.queryBuilder = queryBuilder;
    }



    @PostConstruct
    private void postConstruct() {
        INSERT_QUERY = queryBuilder.insert(Table.TAGS)
                .setAllColumns(Table.TAGS).toString();
        SELECT_ALL_QUERY = queryBuilder.select()
                .setAllColumns(Table.TAGS).from(Table.TAGS).toString();
        SELECT_BY_ID_QUERY = queryBuilder.select()
                .setAllColumns(Table.TAGS)
                .from(Table.TAGS)
                .where(Column.TAG_ID, LogicOperator.EQUALS).toString();
        SELECT_BY_NAME_QUERY = queryBuilder.select()
                .setAllColumns(Table.TAGS)
                .from(Table.TAGS)
                .where(Column.TAG_NAME, LogicOperator.EQUALS).toString();
        DELETE_BY_ID_QUERY = queryBuilder.delete()
                .from(Table.TAGS)
                .where(Column.TAG_ID, LogicOperator.EQUALS).toString();
        SELECT_BY_ID_FROM_MANY_TO_MANY_TABLE = queryBuilder.select()
                .setColumn(Column.GC_HAS_TAG_GC_ID)
                .from(Table.GIFT_CERTIFICATE_HAS_TAG)
                .where(Column.GC_HAS_TAG_TAG_ID, LogicOperator.EQUALS)
                .toString();
    }

    @Override
    public Tag create(Tag entity) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, entity.getName());
            return statement;
        }, keyHolder);
        entity.setId(keyHolder.getKey().longValue());
        return entity;
    }

    @Override
    public Optional<Tag> update(Tag entity) {
        throw new UnsupportedOperationException("Update command is forbidden for tag dao");
    }

    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query(SELECT_ALL_QUERY, mapper);
    }

    @Override
    public Optional<Tag> findById(long id) {
        return jdbcTemplate.query(SELECT_BY_ID_QUERY, mapper, id).stream().findFirst();
    }

    @Override
    public Optional<Tag> findByName(String name) {
        return jdbcTemplate.query(SELECT_BY_NAME_QUERY, mapper, name).stream().findFirst();
    }

    @Override
    public boolean deleteById(long id) {
        return jdbcTemplate.update(DELETE_BY_ID_QUERY, id) == 1;
    }

    @Override
    public boolean isAnyLinksToTag(long id) {
        return jdbcTemplate.queryForRowSet(SELECT_BY_ID_FROM_MANY_TO_MANY_TABLE, id).next();
    }
}
