package com.epam.esm.core.dao.mapper;


import com.epam.esm.core.dao.Column;
import com.epam.esm.core.model.domain.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * The type Tag row mapper.
 */
@Component
public class TagRowMapper implements RowMapper<Tag> {

    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tag tag = new Tag();
        tag.setId(rs.getLong(Column.TAG_ID.getFullName()));
        tag.setName(rs.getString(Column.TAG_NAME.getFullName()));
        return tag;
    }
}
