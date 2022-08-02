package com.epam.esm.core.service;

import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.dto.OrderDto;
import com.epam.esm.core.model.dto.request.SimplePageRequest;

import java.util.List;

public interface OrderService {
    /**
     * "Finds all orders for a user, returning a page of results."
     *
     * @param userId The user's id
     * @param simplePage a page request object that contains the page number and the number of items per page.
     * @return List of OrderDto objects
     */
    List<OrderDto> findUserOrders(long userId, SimplePageRequest simplePage) throws ServiceException;

    /**
     * Finds the order with the given id for the user with the given id.
     *
     * @param userId The user ID of the user who placed the order.
     * @param orderId The order ID
     * @return UserOrderDto
     */
    OrderDto findUserOrderById(long userId, long orderId) throws ServiceException;

    /**
     * Create a new order for the user with the given id and the given certificates.
     *
     * @param userId the id of the user who is making the order
     * @param certificatesIds list of certificates ids
     * @return UserOrderDto
     */
    OrderDto createUserOrder(long userId, List<Long> certificatesIds) throws ServiceException;
}
