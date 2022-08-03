package test.epam.esm.core.repository;

import com.epam.esm.core.config.CoreApplication;
import com.epam.esm.core.model.domain.Tag;
import com.epam.esm.core.model.domain.Tag_;
import com.epam.esm.core.model.domain.User;
import com.epam.esm.core.model.domain.User_;
import com.epam.esm.core.repository.impl.TagRepositoryImpl;
import com.epam.esm.core.repository.impl.UserRepositoryImpl;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CoreApplication.class)
@ActiveProfiles("test")
class UserRepositoryImplTest {

    @Autowired
    private UserRepositoryImpl repository;

    @Test
    void givenPageRequest_whenFindAll_thenReturn() {
        PageRequest pageRequest = PageRequest.of(2, 2, Sort.by("id"));
        List<User> actual = repository.findAll(pageRequest);
        Assertions.assertThat(actual)
                .hasSize(pageRequest.getPageSize())
                .extracting(User::getId).containsExactly(3L, 4L);
    }

    @ParameterizedTest
    @MethodSource("countAllWhereDataProvider")
    void given_whenCountAllWhere_thenReturn(Specification<User> specification, long expected) {
        long actual = repository.countAllWhere(specification);
        assertEquals(expected, actual);
    }

    static Stream<Arguments> countAllWhereDataProvider() {
        return Stream.of(
                Arguments.of(Specification.where(null), 5L),
                Arguments.of((Specification<User>)
                        (root, query, builder) -> builder.like(root.get(User_.login), "%count%"), 2L));
    }

    @ParameterizedTest
    @MethodSource("findByIdDataProvider")
    void givenUserOrNothing_whenFindById_thenReturn(long id, User user) {
        Optional<User> expected = Optional.ofNullable(user);
        Optional<User> actual = repository.findById(id);
        assertEquals(expected, actual);
    }

    static Stream<Arguments> findByIdDataProvider() {
        return Stream.of(
                Arguments.of(1L,
                        new User(1L, "user_1", "login_1", "password_1", null)),
                Arguments.of(333L, null));
    }

    @ParameterizedTest
    @MethodSource("findByNameDataProvider")
    void givenUser_whenFindByName_thenReturn(String name, User user) {
        Optional<User> expected = Optional.ofNullable(user);
        Optional<User> actual = repository.findByName(name);
        assertEquals(expected, actual);
    }

    static Stream<Arguments> findByNameDataProvider() {
        return Stream.of(
                Arguments.of("user_2",
                        new User(2L, "user_2", "login_2", "password_2", null)),
                Arguments.of("I am not exist", null));
    }

    @Test
    void givenUser_whenFindFirstWherePasswordEquals_thenReturn() {
        User expected = new User(5L, "user_5", "login_5", "password_5", null);
        Optional<User> actual = repository.findFirstWhere((Specification<User>)
                (root, query, builder) -> builder.equal(root.get(User_.password), "password_5"));
        assertEquals(expected, actual.get());
    }

    @Test
    void givenUnsupported_whenCreate_thenThrow() {
        User user = new User("new user", "new login", "new password", null);
        assertThrows(UnsupportedOperationException.class, () -> repository.create(user));
    }

    @Test
    void givenUnsupported_whenUpdate_thenThrow() {
        User user = new User(1L, "update user", "update login", "update password", null);
        assertThrows(UnsupportedOperationException.class, () -> repository.update(user));
    }

    @Test
    void givenUnsupported_whenDeleteById_then() {
        assertThrows(UnsupportedOperationException.class, () -> repository.deleteById(1L));
    }
}