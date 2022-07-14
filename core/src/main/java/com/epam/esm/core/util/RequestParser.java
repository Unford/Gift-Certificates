package com.epam.esm.core.util;

import com.epam.esm.core.dao.specification.DaoSpecification;
import com.epam.esm.core.dao.specification.SearchCriteria;
import com.epam.esm.core.dao.specification.SearchOperation;
import com.epam.esm.core.model.domain.GiftCertificate;
import com.epam.esm.core.model.domain.GiftCertificate_;
import com.epam.esm.core.model.domain.Tag_;
import com.epam.esm.core.model.dto.GiftCertificateRequest;
import com.epam.esm.core.model.dto.PageRequestParameters;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

public class RequestParser {
    private static final String SORT_DELIMITER_REGEX = ",\\s*";
    private static final String RHS_COLON_DELIMITER = ":";
    private static final String NEGATION_SIGN = "!";
    private static final String MINUS = "-";
    private static final String SORT_PROPERTY_DIRECTION_REGEX = "[+-].+";

    public static PageRequest convertToPageable(PageRequestParameters pageRequestParameters) {
        Sort sort = parseSortQuery(pageRequestParameters.getSort());
        return PageRequest.of(pageRequestParameters.getPage(), pageRequestParameters.getSize(), sort);
    }

    private static Sort parseSortQuery(String sortQuery) {
        Sort sort = Sort.unsorted();
        if (sortQuery != null && !sortQuery.isEmpty()) {
            List<Sort.Order> orders = new ArrayList<>();
            Arrays.stream(sortQuery.split(SORT_DELIMITER_REGEX)).forEach((s -> {
                Sort.Direction direction = s.startsWith(MINUS) ? Sort.Direction.DESC : Sort.Direction.ASC;
                if (s.matches(SORT_PROPERTY_DIRECTION_REGEX)) {
                    s = s.substring(1);
                }
                orders.add(new Sort.Order(direction, s));
            }));
            sort = Sort.by(orders);
        }
        return sort;
    }

    public static Specification<GiftCertificate> parseSpecification(GiftCertificateRequest pageRequest) {
        List<SearchCriteria> criteriaList = new ArrayList<>();
        List<String> names = pageRequest.getName();
        parseQueryList(criteriaList, names, GiftCertificate_.NAME);
        List<String> descriptions = pageRequest.getDescription();
        parseQueryList(criteriaList, descriptions, GiftCertificate_.DESCRIPTION);
        List<String> tags = pageRequest.getTag();
        parseQueryList(criteriaList, tags, Tag_.NAME, Collections.singletonList(GiftCertificate_.TAGS));

        return new DaoSpecification<>(criteriaList);
    }

    private static void parseQueryList(List<SearchCriteria> criteriaList,
                                       List<String> queries, String key, List<String> joins) {
        if (queries != null && !queries.isEmpty()) {
            queries.forEach(s -> {
                SearchCriteria searchCriteria = RequestParser.parseSingleQuery(s, key);
                searchCriteria.setJoinTables(joins);
                criteriaList.add(searchCriteria);
            });
        }
    }

    private static void parseQueryList(List<SearchCriteria> criteriaList,
                                       List<String> queries, String key) {
        parseQueryList(criteriaList, queries, key, Collections.emptyList());
    }

    private static SearchCriteria parseSingleQuery(String query, String key) {
        String[] strings = query.split(RHS_COLON_DELIMITER, 2);
        boolean isNot = strings[0].startsWith(NEGATION_SIGN);
        SearchCriteria searchCriteria = new SearchCriteria(key, strings[1],
                SearchOperation.parseOperation(isNot ? strings[0].substring(1) : strings[0])
                        .orElse(SearchOperation.EQUAL), isNot);
        return searchCriteria;
    }
}
