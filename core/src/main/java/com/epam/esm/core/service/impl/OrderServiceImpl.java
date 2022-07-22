package com.epam.esm.core.service.impl;

import com.epam.esm.core.exception.CustomErrorCode;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.domain.GiftCertificate;
import com.epam.esm.core.model.domain.Order;
import com.epam.esm.core.model.domain.User;
import com.epam.esm.core.model.dto.GiftCertificateDto;
import com.epam.esm.core.model.dto.OrderDto;
import com.epam.esm.core.model.dto.request.PageRequestParameters;
import com.epam.esm.core.repository.impl.GiftCertificateRepositoryImpl;
import com.epam.esm.core.repository.impl.OrderRepositoryImpl;
import com.epam.esm.core.repository.impl.UserRepositoryImpl;
import com.epam.esm.core.repository.specification.CustomSpecifications;
import com.epam.esm.core.service.OrderService;
import com.epam.esm.core.util.RequestParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepositoryImpl orderRepository;
    private final UserRepositoryImpl userRepository;
    private final GiftCertificateRepositoryImpl giftCertificateRepository;
    private final ModelMapper modelMapper;


    public OrderServiceImpl(OrderRepositoryImpl orderRepository,
                            UserRepositoryImpl userRepository,
                            GiftCertificateRepositoryImpl giftCertificateRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.giftCertificateRepository = giftCertificateRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<OrderDto> findUserOrders(long userId, PageRequestParameters pageRequestParameters)
            throws ServiceException {
        userRepository.findById(userId).orElseThrow(() -> new ServiceException(Long.toString(userId),
                CustomErrorCode.RESOURCE_NOT_FOUND));
        return orderRepository.findAll(CustomSpecifications.whereUserId(userId),
                        RequestParser.convertToPageable(pageRequestParameters))
                .stream()
                .map(certificate -> modelMapper.map(certificate, OrderDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto findUserOrderById(long userId, long orderId) throws ServiceException {
        userRepository.findById(userId).orElseThrow(() -> new ServiceException(Long.toString(userId),
                CustomErrorCode.RESOURCE_NOT_FOUND));
        Order order = orderRepository.findFirstBy(CustomSpecifications.whereUserIdAndOrderId(userId, orderId))
                .orElseThrow(() -> new ServiceException(Long.toString(orderId), CustomErrorCode.RESOURCE_NOT_FOUND));
        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    @Transactional
    public OrderDto createUserOrder(long userId, List<Long> certificatesIds) throws ServiceException {
        User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(Long.toString(userId),
                CustomErrorCode.RESOURCE_NOT_FOUND));
        List<GiftCertificate> certificates = new ArrayList<>(certificatesIds.size());
        for (long id : certificatesIds) {
            GiftCertificate certificate = giftCertificateRepository.findById(id)
                    .orElseThrow(() -> new ServiceException(Long.toString(id),
                            CustomErrorCode.RESOURCE_NOT_FOUND));
            certificates.add(certificate);
        }
        Order order = new Order();
        order.setUser(user);
        order.setGiftCertificates(certificates);
        Order newOrder = orderRepository.create(order);
        return modelMapper.map(newOrder, OrderDto.class);
    }


}
