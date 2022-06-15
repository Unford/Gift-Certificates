package test.epam.esm.core.dao;

import com.epam.esm.core.config.TestDatabaseConfig;
import com.epam.esm.core.dao.GiftCertificateDao;
import com.epam.esm.core.model.domain.AbstractDaoEntity;
import com.epam.esm.core.model.domain.GiftCertificate;
import com.epam.esm.core.model.domain.Tag;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringJUnitConfig(TestDatabaseConfig.class)
@ActiveProfiles("test")
class GiftCertificateDaoTest {
    @Autowired
    private GiftCertificateDao giftCertificateDao;

    @Test
    void testDaoSave() {
        GiftCertificate newGiftCertificate = new GiftCertificate.GiftCertificateBuilder()
                .name("new certificate")
                .description("new description")
                .duration(1)
                .price(BigDecimal.ONE)
                .build();
        newGiftCertificate = giftCertificateDao.create(newGiftCertificate);
        long actualId = newGiftCertificate.getId();
        assertNotEquals(0, actualId);
    }

    @Test
    void testDaoMapper() {
        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(new Tag(1, "tag_1"));
        tagSet.add(new Tag(5, "tag_5"));
        GiftCertificate expected = new GiftCertificate.GiftCertificateBuilder()
                .id(1)
                .name("certificate_1")
                .description("description_1")
                .price(BigDecimal.valueOf(10.43))
                .duration(30)
                .createDate(LocalDateTime.parse("2022-04-18T23:49"))
                .lastUpdateDate(LocalDateTime.parse("2022-04-25T23:00"))
                .tags(tagSet)
                .build();
        GiftCertificate actual = giftCertificateDao.findById(1).get();
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("deleteByIdDataProvider")
    void testDaoDeleteById(long id, boolean expected) {
        boolean actual = giftCertificateDao.deleteById(id);
        assertEquals(expected, actual);
    }

    static Stream<Arguments> deleteByIdDataProvider() {
        return Stream.of(
                Arguments.of(3, true),
                Arguments.of(Long.MAX_VALUE, false));
    }

    @Test
    void testDaoFindAll() {
        List<GiftCertificate> giftCertificateList = giftCertificateDao.findAll();
        int actualSize = giftCertificateList.size();
        int expectedSize = 5;
        assertEquals(actualSize, expectedSize);
    }

    @ParameterizedTest
    @MethodSource("isManyToManyLinkExistDataProvider")
    void testDaoIsManyToManyLinkExist(long giftId, long tagId, boolean expected) {
        boolean actual = giftCertificateDao.isManyToManyLinkExist(giftId, tagId);
        assertEquals(expected, actual);
    }

    static Stream<Arguments> isManyToManyLinkExistDataProvider() {
        return Stream.of(
                Arguments.of(1, 1, true),
                Arguments.of(1, 5, true),
                Arguments.of(3, 1, false),
                Arguments.of(Long.MAX_VALUE, Long.MAX_VALUE, false));
    }

    @Test
    void testDaoCreateManyToManyLink() {
        long giftId = 1;
        GiftCertificate before = giftCertificateDao.findById(giftId).get();
        giftCertificateDao.createManyToManyLink(giftId, 3);
        GiftCertificate after = giftCertificateDao.findById(giftId).get();
        assertNotEquals(before.getTags(), after.getTags());
    }


    @ParameterizedTest
    @MethodSource("FindAllByParametersDataProvider")
    void testDaoFindAllByParameters(String tag, String name, String description, String sort,
                                    List<Long> expected) {
        List<Long> actual = giftCertificateDao.findAllByParameters(tag, name, description, sort)
                .stream().map(AbstractDaoEntity::getId)
                .collect(Collectors.toList());
        MatcherAssert.assertThat(actual, Matchers.contains(expected.toArray(new Long[0])));
    }

    static Stream<Arguments> FindAllByParametersDataProvider() {
        return Stream.of(
                Arguments.of("tag_5", null, null, null, Arrays.asList(1L, 3L, 4L, 5L)),
                Arguments.of(null, "find", null, null, Arrays.asList(2L, 4L)),
                Arguments.of(null, null, "find", null, Arrays.asList(2L, 5L)),
                Arguments.of(null, null, null, "-name", Arrays.asList(4L, 5L, 3L, 2L, 1L)),
                Arguments.of(null, null, null, "date", Arrays.asList(5L, 2L, 3L, 4L, 1L)),
                Arguments.of("find", "find", "find", "-name", Arrays.asList(4L, 2L)),
                Arguments.of(null, null, null, null, Arrays.asList(1L, 2L, 3L, 4L, 5L)));
    }
}