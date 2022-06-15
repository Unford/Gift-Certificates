package test.epam.esm.core.dao;

import com.epam.esm.core.config.TestDatabaseConfig;
import com.epam.esm.core.dao.TagDao;
import com.epam.esm.core.model.domain.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDatabaseConfig.class)
@ActiveProfiles("test")
class TagDaoTest {

    @Autowired
    private TagDao tagDao;

    @Test
    void testDaoSave() {
        Tag tag = new Tag(999, "new tag");
        Tag actualTag = tagDao.create(tag);
        long actual = actualTag.getId();
        long expected = tagDao.findAll().size();
        assertEquals(actual, expected);
    }

    @Test
    void testDaoFindAll() {
        List<Tag> tags = tagDao.findAll();
        int actual = tags.size();
        int expected = 5;
        assertEquals(expected, actual);
    }


    @ParameterizedTest
    @MethodSource("findByIdDataProvider")
    void testDaoFindById(long id, boolean expected) {
        Optional<Tag> tag = tagDao.findById(id);
        boolean actual = tag.isPresent();
        assertEquals(expected, actual);
    }

    public static Object[][] findByIdDataProvider() {
        return new Object[][]{
                {1, true},
                {Long.MAX_VALUE, false}
        };
    }

    @ParameterizedTest
    @MethodSource("findByNameDataProvider")
    void testDaoFindById(String name, boolean expected) {
        Optional<Tag> tag = tagDao.findByName(name);
        boolean actual = tag.isPresent();
        assertEquals(expected, actual);
    }

    public static Object[][] findByNameDataProvider() {
        return new Object[][]{
                {"tag_1", true},
                {"not exist", false}
        };
    }

    @ParameterizedTest
    @MethodSource("isAnyLinksToTagDataProvider")
    void testDaoIsAnyLinksToTag(long id, boolean expected) {
        boolean actual = tagDao.isAnyLinksToTag(id);
        assertEquals(expected, actual);
    }

    static Stream<Arguments> isAnyLinksToTagDataProvider() {
        return Stream.of(
                Arguments.of(1, true),
                Arguments.of(3, false),
                Arguments.of(Long.MAX_VALUE, false));
    }

    @ParameterizedTest
    @MethodSource("deleteByIdDataProvider")
    void testDaoDeleteById(long id, boolean expected) {
        boolean actual = tagDao.deleteById(id);
        assertEquals(expected, actual);
    }

    static Stream<Arguments> deleteByIdDataProvider() {
        return Stream.of(
                Arguments.of(3, true),
                Arguments.of(Long.MAX_VALUE, false));
    }

    @Test
    void testDaoDeleteByIdForbidden() {
        assertThrows(DataIntegrityViolationException.class, () -> tagDao.deleteById(1));
    }




}



