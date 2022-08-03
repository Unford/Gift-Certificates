package test.epam.esm.core.repository;

import com.epam.esm.core.config.CoreApplication;
import com.epam.esm.core.model.domain.*;
import com.epam.esm.core.repository.impl.GiftCertificateRepositoryImpl;
import com.epam.esm.core.repository.impl.OrderRepositoryImpl;
import com.epam.esm.core.repository.impl.UserRepositoryImpl;
import com.epam.esm.core.repository.specification.CustomSpecifications;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CoreApplication.class)
@ActiveProfiles("test")
class OrderRepositoryImplTest {
    @Autowired
    private OrderRepositoryImpl orderRepository;
    @Autowired
    private UserRepositoryImpl userRepository;
    @Autowired
    private GiftCertificateRepositoryImpl certificateRepository;

    @Test
    void givenTag_whenCreateNew_thenReturn() {
        List<GiftCertificate> certificates = certificateRepository.findAll(PageRequest.of(2, 2));
        BigDecimal expectedCost = certificates.stream().map(GiftCertificate::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        User user = userRepository.findById(3).get();
        Order newOrder = new Order();
        newOrder.setGiftCertificates(certificates);
        newOrder.setUser(user);

        Order actual = orderRepository.create(newOrder);

        assertEquals(expectedCost, actual.getCost());
        assertNotNull(actual.getId());
        assertNotNull(actual.getPurchaseDate());
        assertEquals(certificates.size(), actual.getGiftCertificates().size());
    }

    @Test
    void givenOrders_whenFindAll_thenReturn() {
        PageRequest pageRequest = PageRequest.of(4, 1, Sort.by("id"));
        List<Order> actual = orderRepository.findAll(pageRequest);
        Assertions.assertThat(actual)
                .hasSize(pageRequest.getPageSize())
                .extracting(Order::getId).containsExactly(4L);
    }

    @ParameterizedTest
    @MethodSource("countAllWhereDataProvider")
    void given_whenCountAllWhere_thenReturn(Specification<Order> specification, long expected) {
        long actual = orderRepository.countAllWhere(specification);
        assertEquals(expected, actual);
    }

    static Stream<Arguments> countAllWhereDataProvider() {
        return Stream.of(
                Arguments.of(Specification.where(null), 6L),
                Arguments.of((Specification<Order>)
                                (root, query, builder) -> builder.greaterThan(root.get(Order_.cost), BigDecimal.valueOf(7)),
                        2L));
    }


    @ParameterizedTest
    @MethodSource("findByIdDataProvider")
    void givenOrderOrNothing_whenFindById_thenReturn(long id, Order order) {
        Optional<Order> expected = Optional.ofNullable(order);
        Optional<Order> actual = orderRepository.findById(id);
        assertEquals(expected, actual);
    }

    static Stream<Arguments> findByIdDataProvider() {
        return Stream.of(
                Arguments.of(2L,
                        new Order(2L, BigDecimal.valueOf(10).setScale(2),
                                LocalDateTime.parse("2022-01-12T11:50:00.000"), null, null)),
                Arguments.of(333L, null));
    }

    @ParameterizedTest
    @MethodSource("findFirstBySpecificationProvider")
    void givenSpecification_whenFindFirstWhere_thenReturn(Specification<Order> specification, Order order) {
        Optional<Order> expected = Optional.ofNullable(order);
        Optional<Order> actual = orderRepository.findFirstWhere(specification);
        assertEquals(expected, actual);

    }

    static Stream<Arguments> findFirstBySpecificationProvider() {
        return Stream.of(
                Arguments.of(CustomSpecifications.whereUserId(2),
                        new Order(2L, BigDecimal.valueOf(10).setScale(2),
                                LocalDateTime.parse("2022-01-12T11:50:00.000"), null, null)),
                Arguments.of(CustomSpecifications.whereUserIdAndOrderId(4, 5),
                        new Order(5L, BigDecimal.valueOf(6).setScale(2),
                                LocalDateTime.parse("2022-05-02T06:50:00.000"), null, null)),
                Arguments.of((Specification<Order>)
                                (root, query, builder) -> builder.equal(root.get(Order_.id), 9213),
                        null));
    }

    @Test
    void givenUnsupported_whenUpdate_thenThrow() {
        User user = new User("new user", "new login", "new password", null);
        Order order = new Order(1L, BigDecimal.TEN, LocalDateTime.now(), user, null);
        assertThrows(UnsupportedOperationException.class, () -> orderRepository.update(order));

    }

    @Test
    void givenUnsupported_whenFindByName_thenThrow() {
        assertThrows(UnsupportedOperationException.class, () -> orderRepository.findByName("name"));
    }

    @Test
    void givenUnsupported_whenDeleteById_then() {
        assertThrows(UnsupportedOperationException.class, () -> orderRepository.deleteById(1L));

    }
}