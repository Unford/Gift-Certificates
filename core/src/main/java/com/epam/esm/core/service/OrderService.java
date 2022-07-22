package com.epam.esm.core.service;

import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.dto.GiftCertificateDto;
import com.epam.esm.core.model.dto.OrderDto;
import com.epam.esm.core.model.dto.request.PageRequestParameters;

import java.util.List;

public interface OrderService {
    List<OrderDto> findUserOrders(long userId, PageRequestParameters pageRequestParameters) throws ServiceException;

    OrderDto findUserOrderById(long userId, long orderId) throws ServiceException;

    OrderDto createUserOrder(long userId, List<Long> certificatesIds) throws ServiceException;
}
