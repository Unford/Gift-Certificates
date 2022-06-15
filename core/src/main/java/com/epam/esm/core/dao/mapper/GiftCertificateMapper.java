package com.epam.esm.core.dao.mapper;

import com.epam.esm.core.dao.Column;
import com.epam.esm.core.dao.Table;
import com.epam.esm.core.model.domain.GiftCertificate;
import com.epam.esm.core.model.domain.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * The type Gift certificate mapper.
 */
@Component
public class GiftCertificateMapper implements RowMapper<GiftCertificate> {
    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(rs.getLong(Column.GC_ID.getFullName()));
        giftCertificate.setName(rs.getString(Column.GC_NAME.getFullName()));
        giftCertificate.setDescription(rs.getString(Column.GC_DESCRIPTION.getFullName()));
        giftCertificate.setPrice(rs.getBigDecimal(Column.GC_PRICE.getFullName()));
        giftCertificate.setDuration(rs.getInt(Column.GC_DURATION.getFullName()));
        giftCertificate.setCreateDate(rs.getTimestamp(Column.GC_CREATE_DATE.getFullName()).toLocalDateTime());
        giftCertificate.setLastUpdateDate(rs.getTimestamp(Column.GC_UPDATE_DATE.getFullName()).toLocalDateTime());
        giftCertificate.setTags(parseTagSet(rs.getString(Table.TAGS.toString())));

        return giftCertificate;
    }

    private Set<Tag> parseTagSet(String column) {
        Set<Tag> tags = column != null ? Arrays.stream(column.split(Column.TAGS_GROUP_CONCAT_DELIMITER))
                .map(s -> {
                    String[] fields = s.split(Column.TAGS_CONCAT_DELIMITER);
                    return new Tag(Long.parseLong(fields[0]), fields[1]);
                })
                .collect(Collectors.toSet()) : new HashSet<>();
        return tags;//todo
    }
}
