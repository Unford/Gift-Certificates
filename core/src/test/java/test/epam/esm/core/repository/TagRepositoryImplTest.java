package test.epam.esm.core.repository;

import com.epam.esm.core.config.CoreApplication;
import com.epam.esm.core.model.domain.Tag;
import com.epam.esm.core.model.domain.Tag_;
import com.epam.esm.core.repository.impl.TagRepositoryImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CoreApplication.class)
@ActiveProfiles("test")
class TagRepositoryImplTest {

    @Autowired
    private TagRepositoryImpl repository;

    @Test
    void givenTag_whenCreate_thenReturn() {
        String expectedName = "new tag";
        Tag newTag = new Tag(expectedName);
        Tag actual = repository.create(newTag);

        assertEquals(expectedName, actual.getName());
        assertNotNull(actual.getId());
        assertNull(actual.getGiftCertificates());
    }

    @Test
    void givenUnsupported_whenUpdate_thenThrow() {
        Tag tag = new Tag("updated tag");
        assertThrows(UnsupportedOperationException.class, () -> repository.update(tag));
    }

    @Test
    void givenPageRequest_whenFindAll_thenReturn() {
        PageRequest pageRequest = PageRequest.of(1, 2, Sort.by("id"));
        List<Tag> actual = repository.findAll(pageRequest);
        Assertions.assertThat(actual)
                .hasSize(pageRequest.getPageSize())
                .extracting(Tag::getId).containsExactly(1L, 2L);
    }

    @ParameterizedTest
    @MethodSource("countAllWhereDataProvider")
    void given_whenCountAllWhere_thenReturn(Specification<Tag> specification, long expected) {
        long actual = repository.countAllWhere(specification);
        assertEquals(expected, actual);
    }

    static Stream<Arguments> countAllWhereDataProvider() {
        return Stream.of(
                Arguments.of(Specification.where(null), 6L),
                Arguments.of((Specification<Tag>)
                        (root, query, builder) -> builder.like(root.get(Tag_.name), "%5%"), 2L));
    }

    @ParameterizedTest
    @MethodSource("findByIdDataProvider")
    void givenTag_whenFindById_thenReturn(long id, Tag tag) {
        Optional<Tag> expected = Optional.ofNullable(tag);
        Optional<Tag> actual = repository.findById(id);
        assertEquals(expected, actual);
    }

    static Stream<Arguments> findByIdDataProvider() {
        return Stream.of(
                Arguments.of(1L, new Tag(1L, "tag_1")),
                Arguments.of(333L, null));
    }

    @ParameterizedTest
    @MethodSource("findByNameDataProvider")
    void givenTag_whenFindByName_thenReturn(String name, Tag tag) {
        Optional<Tag> expected = Optional.ofNullable(tag);
        Optional<Tag> actual = repository.findByName(name);
        assertEquals(expected, actual);
    }

    static Stream<Arguments> findByNameDataProvider() {
        return Stream.of(
                Arguments.of("tag_1", new Tag(1L, "tag_1")),
                Arguments.of("I am not exist", null));
    }


    @Test
    void givenTag_whenFindFirstWhereIdOrNameEquals_thenReturn() {
        Optional<Tag> actual = repository.findFirstWhere((Specification<Tag>)
                (root, query, builder) -> builder.or(builder.equal(root.get(Tag_.id), "1"),
                        builder.equal(root.get(Tag_.name), "tag_2")));
        assertTrue(actual.isPresent());
    }

    @Test
    @Transactional
    void givenTag_whenDeleteById_thenOk() {
        assertDoesNotThrow(() -> repository.deleteById(6L));
        Long actualSize = repository.countAllWhere(Specification.where(null));
        Long expectedSize = 5L;
        assertEquals(expectedSize, actualSize);
    }

    @Test
    void givenOrders_whenFindTheMostWidelyUsedTag_thenReturn() {
        Optional<Tag> actual = repository.findTheMostWidelyUsedTag();
        Assertions.assertThat(actual.get())
                .extracting(Tag::getId, Tag::getName)
                .containsExactly(5L, "tag_5");
    }
}