package com.epam.esm.core.util;

import com.epam.esm.core.dao.specification.DaoSpecification;
import com.epam.esm.core.dao.specification.JoinedDaoSpecification;
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
    private static final String DELIMITER_REGEX = ",\\s*";
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
            Arrays.stream(sortQuery.split(DELIMITER_REGEX)).forEach((s -> {
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
        criteriaList.addAll(parseQueryList(pageRequest.getName(), GiftCertificate_.NAME));
        criteriaList.addAll(parseQueryList(pageRequest.getDescription(), GiftCertificate_.DESCRIPTION));

        Optional<SearchCriteria> tagCriteria = parseSingleQuery(pageRequest.getTag(), Tag_.NAME);

        Specification<GiftCertificate> fieldsSpecification = new DaoSpecification<>(criteriaList);
        Specification<GiftCertificate> tagsSpecification = parseTagsSpecification(tagCriteria.orElse(null));


        return fieldsSpecification.and(tagsSpecification);
    }

    private static List<SearchCriteria> parseQueryList(List<String> queries, String key) {
        List<SearchCriteria> criteriaList = new ArrayList<>();
        if (queries != null && !queries.isEmpty()) {
            queries.forEach(s -> RequestParser.parseSingleQuery(s, key).ifPresent(criteriaList::add));
        }
        return criteriaList;
    }

    private static Specification<GiftCertificate> parseTagsSpecification(SearchCriteria searchCriteria) {
        Specification<GiftCertificate> specification = null;
        if (searchCriteria != null) {
            if (searchCriteria.getOperation() == SearchOperation.IN) {
                String[] values = searchCriteria.getValue().toString().split(DELIMITER_REGEX);
                specification = searchCriteria.isNot() ? JoinedDaoSpecification.notInTags(values) :
                        JoinedDaoSpecification.inTags(values);
            } else {
                specification = new JoinedDaoSpecification<>(Collections.singletonList(searchCriteria),
                        GiftCertificate_.TAGS);
            }
        }
        return specification;
    }

    private static Optional<SearchCriteria> parseSingleQuery(String query, String key) {
        Optional<SearchCriteria> result = Optional.empty();
        if (query != null && !query.isEmpty()) {
            String[] strings = query.split(RHS_COLON_DELIMITER, 2);
            String operation = strings[0];
            String value = strings[0];
            SearchOperation searchOperation = SearchOperation.EQUAL;

            boolean isNot = operation.startsWith(NEGATION_SIGN);
            if (strings.length >= 2) {
                value = strings[1];
                searchOperation = SearchOperation.parseOperation(isNot ? operation.substring(1)
                        : operation).orElse(SearchOperation.EQUAL);
            }
            result = Optional.of(new SearchCriteria(key, value, searchOperation, isNot));
        }
        return result;
    }
}
