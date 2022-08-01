package test.epam.esm.core.util;

import com.epam.esm.core.model.dto.request.SimplePageRequest;
import com.epam.esm.core.repository.specification.SearchCriteria;
import com.epam.esm.core.util.RequestParameterParser;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.stream.Stream;

import static com.epam.esm.core.repository.specification.SearchOperation.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


class RequestParameterParserTest {

    @Test
    void givenPageRequest_whenConvertToPageable_thenReturn() {
        int expectedPage = 1;
        int expectedSize = 2;

        SimplePageRequest pageRequest = new SimplePageRequest();
        pageRequest.setPage(expectedPage);
        pageRequest.setSize(expectedSize);
        pageRequest.setSort("-name,  +id");

        Pageable actual = RequestParameterParser.convertToPageable(pageRequest);
        assertEquals(expectedPage, actual.getPageNumber());
        assertEquals(expectedSize, actual.getPageSize());
        assertThat(actual.getSort().get())
                .extracting(Sort.Order::getProperty, Sort.Order::getDirection)
                .containsExactly(Tuple.tuple("name", Sort.Direction.DESC),
                        Tuple.tuple("id", Sort.Direction.ASC));
    }


    @ParameterizedTest
    @MethodSource("ParseSingleQueryDataProvider")
    void givenQuery_whenParseSingleQuery_thenReturn(String query, String key, SearchCriteria expected) {
        SearchCriteria actual = RequestParameterParser.parseSingleQuery(query, key).get();
        assertThat(actual)
                .extracting(SearchCriteria::getKey, SearchCriteria::getOperation, SearchCriteria::getValue)
                .containsExactly(expected.getKey(), expected.getOperation(), expected.getValue());
    }

    static Stream<Arguments> ParseSingleQueryDataProvider() {
        return Stream.of(
                Arguments.of("lk:string", "key",
                        new SearchCriteria("key", "string", LIKE)),
                Arguments.of("!eq:another", "another",
                        new SearchCriteria("another", "another", EQUAL, true)),
                Arguments.of("!in:first, second; third", "key",
                        new SearchCriteria("key", new String[]{"first", "second", "third"}, IN, true)));
    }

    @ParameterizedTest
    @MethodSource("ParseSortQueryDataProvider")
    void givenQuery_whenParseSortQuery_thenReturn(String query, Sort expected) {
        Sort actual = RequestParameterParser.parseSortQuery(query);
        assertEquals(expected, actual);
    }

    static Stream<Arguments> ParseSortQueryDataProvider() {
        return Stream.of(
                Arguments.of("-name", Sort.by(Sort.Direction.DESC, "name")),
                Arguments.of("id", Sort.by(Sort.Direction.ASC, "id")),
                Arguments.of("-name, +date",
                        Sort.by(Sort.Order.desc("name"), Sort.Order.asc("date"))));
    }

}