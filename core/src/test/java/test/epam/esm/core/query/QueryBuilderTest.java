package test.epam.esm.core.query;

import com.epam.esm.core.dao.Column;
import com.epam.esm.core.dao.Table;
import com.epam.esm.core.dao.query.JoinType;
import com.epam.esm.core.dao.query.LogicOperator;
import com.epam.esm.core.dao.query.QueryBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.epam.esm.core.dao.Column.*;

class QueryBuilderTest {
    private QueryBuilder queryBuilder;

    @BeforeEach
    public void setUp() {
        queryBuilder = new QueryBuilder();
    }

    @Test
    void testBuildTagsSelect() {
        String expected = "SELECT tags.id, tags.name FROM tags WHERE tags.id=?";
        String actual = queryBuilder.select()
                .setAllColumns(Table.TAGS)
                .from(Table.TAGS)
                .where(Column.TAG_ID, LogicOperator.EQUALS)
                .toString();
        Assertions.assertEquals(removeWhiteSpaces(expected), removeWhiteSpaces(actual));
    }

    @Test
    void testBuildGiftCertificatesSelect() {
        String expected = "SELECT gift_certificates.id," +
                " gift_certificates.name, gift_certificates.description," +
                " gift_certificates.price, gift_certificates.duration," +
                " gift_certificates.create_date," +
                " gift_certificates.last_update_date" +
                " FROM gift_certificates WHERE gift_certificates.name=?";
        String actual = queryBuilder.select()
                .setAllColumns(Table.GIFT_CERTIFICATES)
                .from(Table.GIFT_CERTIFICATES)
                .where(Column.GC_NAME, LogicOperator.EQUALS)
                .toString();
        Assertions.assertEquals(removeWhiteSpaces(expected), removeWhiteSpaces(actual));
    }

    @Test
    void testBuildGiftCertificatesDelete() {
        String expected = "DELETE FROM gift_certificates WHERE gift_certificates.name=?";
        String actual = queryBuilder.delete()
                .from(Table.GIFT_CERTIFICATES)
                .where(Column.GC_NAME, LogicOperator.EQUALS)
                .toString();
        Assertions.assertEquals(removeWhiteSpaces(expected), removeWhiteSpaces(actual));
    }

    @Test
    void testBuildGiftCertificatesUpdate() {
        String expected = "UPDATE gift_certificates SET" +
                " gift_certificates.name=?, gift_certificates.description=?," +
                " gift_certificates.price=?, gift_certificates.duration=?," +
                " gift_certificates.create_date=?, gift_certificates.last_update_date=?" +
                " WHERE gift_certificates.id=?";
        String actual = queryBuilder
                .update(Table.GIFT_CERTIFICATES)
                .setAllColumns(Table.GIFT_CERTIFICATES)
                .where(GC_ID, LogicOperator.EQUALS)
                .toString();
        Assertions.assertEquals(removeWhiteSpaces(expected), removeWhiteSpaces(actual));
    }

    @Test
    void testBuildGiftCertificateComplexSelect() {
        String expected = "SELECT gift_certificates.id, gift_certificates.name," +
                " gift_certificates.description, gift_certificates.price," +
                " gift_certificates.duration, gift_certificates.create_date," +
                " gift_certificates.last_update_date," +
                " GROUP_CONCAT( CONCAT(tags.id,':',tags.name) ) AS tags" +
                " FROM gift_certificates" +
                " LEFT JOIN gift_certificate_has_tag ON (gift_certificates.id=gift_certificate_has_tag.gift_certificate_id)" +
                " LEFT JOIN tags ON (gift_certificate_has_tag.tag_id=tags.id)" +
                " GROUP BY gift_certificates.id";
        String actual = queryBuilder.select()
                .setAllColumns(Table.GIFT_CERTIFICATES)
                .groupConcat(QueryBuilder.concat(TAG_ID,
                        TAGS_CONCAT_DELIMITER,
                        TAG_NAME), Table.TAGS.toString())
                .from(Table.GIFT_CERTIFICATES)
                .join(JoinType.LEFT, Table.GIFT_CERTIFICATE_HAS_TAG)
                .on(GC_ID, GC_HAS_TAG_GC_ID)
                .join(JoinType.LEFT, Table.TAGS).on(GC_HAS_TAG_TAG_ID, TAG_ID)
                .groupBy(GC_ID)
                .toString();
        Assertions.assertEquals(removeWhiteSpaces(expected), removeWhiteSpaces(actual));
    }

    @Test
    void testBuildTagInsert() {
        String expected = "INSERT INTO tags( name) VALUES(?)";
        String actual = queryBuilder.insert(Table.TAGS)
                .setAllColumns(Table.TAGS)
                .toString();
        Assertions.assertEquals(removeWhiteSpaces(expected), removeWhiteSpaces(actual));
    }


    String removeWhiteSpaces(String input) {
        return input.trim().replaceAll("\\s{2,}", " ");
    }
}
