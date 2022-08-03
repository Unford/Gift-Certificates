package test.epam.esm.core.repository;

import com.epam.esm.core.config.CoreApplication;
import com.epam.esm.core.model.domain.GiftCertificate;
import com.epam.esm.core.model.domain.GiftCertificate_;
import com.epam.esm.core.repository.impl.GiftCertificateRepositoryImpl;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CoreApplication.class)
@ActiveProfiles("test")
class GiftCertificateRepositoryImplTest {
    @Autowired
    private GiftCertificateRepositoryImpl certificateRepository;

    @Test
    void givenNewCertificate_whenCreate_thenReturn() {
        long expectedSize = certificateRepository.countAllWhere(Specification.where(null)) + 1;
        int expectedDuration = 11;
        BigDecimal expectedPrice = BigDecimal.valueOf(122);
        String expectedDescription = "new description 11";
        String expectedName = "new name 11";

        GiftCertificate newCertificate = new GiftCertificate.GiftCertificateBuilder()
                .duration(expectedDuration)
                .price(expectedPrice)
                .description(expectedDescription)
                .name(expectedName)
                .build();

        GiftCertificate actual = certificateRepository.create(newCertificate);
        long actualSize = certificateRepository.countAllWhere(Specification.where(null));

        assertNotNull(actual.getId());
        assertNotNull(actual.getCreateDate());
        assertNotNull(actual.getLastUpdateDate());
        Assertions.assertThat(actual).extracting(GiftCertificate::getDuration,
                        GiftCertificate::getPrice, GiftCertificate::getDescription,
                        GiftCertificate::getName)
                .containsExactly(expectedDuration, expectedPrice, expectedDescription, expectedName);
        assertEquals(expectedSize, actualSize);
    }

    @Test
    void givenCertificate_whenUpdate_thenUpdated() {
        GiftCertificate updatedCertificate = certificateRepository.findById(1).get();

        LocalDateTime unexpectedLastUpdateDate = updatedCertificate.getLastUpdateDate();
        int expectedDuration = 11;
        BigDecimal expectedPrice = BigDecimal.valueOf(122);
        String expectedDescription = "update description 22";
        String expectedName = "update name 22";

        updatedCertificate.setDuration(11);
        updatedCertificate.setPrice(expectedPrice);
        updatedCertificate.setDescription(expectedDescription);
        updatedCertificate.setName(expectedName);

        GiftCertificate actual = certificateRepository.update(updatedCertificate);

        assertEquals(updatedCertificate.getId(), actual.getId());
        assertEquals(updatedCertificate.getCreateDate() ,actual.getCreateDate());
        assertNotEquals(unexpectedLastUpdateDate, actual.getLastUpdateDate());
        Assertions.assertThat(actual).extracting(GiftCertificate::getDuration,
                        GiftCertificate::getPrice, GiftCertificate::getDescription,
                        GiftCertificate::getName)
                .containsExactly(expectedDuration, expectedPrice, expectedDescription, expectedName);

    }

    @Test
    void givenPageRequest_whenFindAll_thenReturn() {
        PageRequest pageRequest = PageRequest.of(1, 2, Sort.by("id"));
        List<GiftCertificate> actual = certificateRepository.findAll(pageRequest);
        Assertions.assertThat(actual)
                .hasSize(pageRequest.getPageSize())
                .extracting(GiftCertificate::getId).containsExactly(1L, 2L);
    }

    @ParameterizedTest
    @MethodSource("countAllWhereDataProvider")
    void given_whenCountAllWhere_thenReturn(Specification<GiftCertificate> specification, long expected) {
        long actual = certificateRepository.countAllWhere(specification);
        assertEquals(expected, actual);
    }

    static Stream<Arguments> countAllWhereDataProvider() {
        return Stream.of(
                Arguments.of(Specification.where(null), 7L),
                Arguments.of((Specification<GiftCertificate>)
                        (root, query, builder) -> builder.like(root.get(GiftCertificate_.name), "%find%"), 2L),
                Arguments.of((Specification<GiftCertificate>)
                        (root, query, builder) ->
                                builder.like(root.get(GiftCertificate_.description), "%find%"), 3L));
    }

    @ParameterizedTest
    @MethodSource("findByIdDataProvider")
    void givenCertificate_whenFindById_thenReturn(long id, Long expected) {
        Optional<GiftCertificate> actual = certificateRepository.findById(id);
        if (expected == null) {
            assertFalse(actual.isPresent());
        } else {
            assertEquals(expected, actual.get().getId());
        }
    }

    static Stream<Arguments> findByIdDataProvider() {
        return Stream.of(
                Arguments.of(1L, 1L),
                Arguments.of(333L, null));
    }

    @ParameterizedTest
    @MethodSource("findByNameDataProvider")
    void givenCertificate_whenFindByName_thenReturn(String name, Long expected) {
        Optional<GiftCertificate> actual = certificateRepository.findByName(name);
        if (expected == null) {
            assertFalse(actual.isPresent());
        } else {
            assertEquals(expected, actual.get().getId());
        }
    }

    static Stream<Arguments> findByNameDataProvider() {
        return Stream.of(
                Arguments.of("certificate_3", 3L),
                Arguments.of("certificate_find_4", 4L),
                Arguments.of("I am not exist", null));
    }

    @Test
    @Transactional
    void givenCertificateWithoutOrders_whenDeleteById_thenOk() {
        Long expectedSize = certificateRepository.countAllWhere(Specification.where(null)) - 1;
        assertDoesNotThrow(() -> certificateRepository.deleteById(7L));
        Long actualSize = certificateRepository.countAllWhere(Specification.where(null));
        assertEquals(expectedSize, actualSize);
    }
}