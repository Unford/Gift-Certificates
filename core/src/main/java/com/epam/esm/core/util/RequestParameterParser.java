package com.epam.esm.core.util;

import com.epam.esm.core.repository.specification.*;
import com.epam.esm.core.model.domain.GiftCertificate;
import com.epam.esm.core.model.domain.GiftCertificate_;
import com.epam.esm.core.model.domain.Tag_;
import com.epam.esm.core.model.dto.request.CertificatePageRequest;
import com.epam.esm.core.model.dto.request.SimplePageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

public final class RequestParameterParser {
    private RequestParameterParser() {
    }

    private static final String DELIMITER_REGEX = ",\\s*";

    private static final String IN_DELIMITER_REGEX = ";|" + DELIMITER_REGEX;
    private static final String RHS_COLON_DELIMITER = ":";
    private static final String NEGATION_SIGN = "!";
    private static final String MINUS = "-";
    private static final String SORT_PROPERTY_DIRECTION_REGEX = "[+-].+";

    public static PageRequest convertToPageable(SimplePageRequest simplePage) {
        Sort sort = parseSortQuery(simplePage.getSort());
        return PageRequest.of(simplePage.getPage(), simplePage.getSize(), sort);
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

    public static Specification<GiftCertificate> parseSpecification(CertificatePageRequest pageRequest) {
        List<SearchCriteria> criteriaList = new ArrayList<>();
        criteriaList.addAll(parseQueryList(pageRequest.getName(), GiftCertificate_.NAME));
        criteriaList.addAll(parseQueryList(pageRequest.getDescription(), GiftCertificate_.DESCRIPTION));

        Optional<SearchCriteria> tagCriteria = parseSingleQuery(pageRequest.getTag(), Tag_.NAME);

        Specification<GiftCertificate> fieldsSpecification = new RepositorySpecification<>(criteriaList);
        Specification<GiftCertificate> tagsSpecification = parseTagsSpecification(tagCriteria.orElse(null));

        return fieldsSpecification.and(tagsSpecification);
    }

    private static List<SearchCriteria> parseQueryList(List<String> queries, String key) {
        List<SearchCriteria> criteriaList = new ArrayList<>();
        if (queries != null && !queries.isEmpty()) {
            queries.forEach(s -> RequestParameterParser.parseSingleQuery(s, key).ifPresent(criteriaList::add));
        }
        return criteriaList;
    }

    private static Specification<GiftCertificate> parseTagsSpecification(SearchCriteria searchCriteria) {
        Specification<GiftCertificate> specification = null;
        if (searchCriteria != null) {
            if (searchCriteria.getOperation() == SearchOperation.IN) {
                String[] values = (String[]) searchCriteria.getValue();
                specification = searchCriteria.isNot() ? CustomSpecifications.notInTags(values) :
                        CustomSpecifications.inTags(values);
            } else {
                specification = new JoinedRepositorySpecification<>(Collections.singletonList(searchCriteria),
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
            Object value = strings[0];
            SearchOperation searchOperation = SearchOperation.EQUAL;

            boolean isNot = operation.startsWith(NEGATION_SIGN);
            if (strings.length >= 2) {
                value = strings[1];
                searchOperation = SearchOperation.parseOperation(isNot ? operation.substring(1)
                        : operation).orElse(SearchOperation.EQUAL);
            }
            value = searchOperation != SearchOperation.IN ? value :
                    Arrays.stream(value.toString().split(IN_DELIMITER_REGEX))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .toArray(String[]::new);
            result = Optional.of(new SearchCriteria(key, value, searchOperation, isNot));
        }
        return result;
    }
}
