package com.epam.esm.api.hateoas;

/**
 * It's a collection of constants that represent the names of the custom link relations
 */
public final class CustomLinkRelation {

    private CustomLinkRelation() {
    }

    public static final String FIND_USER = "user";
    public static final String FIND_USER_ORDERS = "findUserOrders";
    public static final String THE_MOST_WIDELY_USED_TAG = "theMostWidelyUsedTag";
    public static final String CREATE = "create";
    public static final String CREATE_ORDER = "createOrder";
    public static final String DELETE_BY_ID = "delete";
    public static final String PREVIOUS_PAGE = "previousPage";
    public static final String NEXT_PAGE = "nextPage";
    public static final String UPDATE_BY_ID = "patch";


}
