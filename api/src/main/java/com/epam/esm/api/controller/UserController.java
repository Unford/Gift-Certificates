package com.epam.esm.api.controller;

import com.epam.esm.api.hateoas.assembler.impl.OrderCollectionAssembler;
import com.epam.esm.api.hateoas.assembler.impl.UserCollectionAssembler;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.dto.OrderDto;
import com.epam.esm.core.model.dto.UserDto;
import com.epam.esm.core.model.dto.request.PageRequestParameters;
import com.epam.esm.core.service.OrderService;
import com.epam.esm.core.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    private final UserServiceImpl userService;
    private final OrderService orderService;

    private final UserCollectionAssembler userCollectionAssembler;
    private final OrderCollectionAssembler orderCollectionAssembler;

    @Autowired
    public UserController(UserServiceImpl userService, OrderService orderService,
                          UserCollectionAssembler userCollectionAssembler,
                          OrderCollectionAssembler orderCollectionAssembler) {
        this.userService = userService;
        this.orderService = orderService;
        this.userCollectionAssembler = userCollectionAssembler;
        this.orderCollectionAssembler = orderCollectionAssembler;
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") @Positive long id) throws ServiceException {
        return userService.findById(id);
    }

    @GetMapping
    public CollectionModel<UserDto> getUsers(
            @RequestParam(name = "page", required = false, defaultValue = "1") @Positive int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") @Positive int size)
            throws ServiceException {
        PageRequestParameters requestParameters = PageRequestParameters.of(page, size);
        List<UserDto> userDtoList = userService.findAll(requestParameters);
        return userCollectionAssembler.toCollectionModel(userDtoList, requestParameters);
    }

    @GetMapping("/{id}/orders")
    public CollectionModel<OrderDto> getUsersOrders(@PathVariable("id") @Positive long userId,
                                                    @RequestParam(name = "page", required = false, defaultValue = "1")
                                                    @Positive int page,
                                                    @RequestParam(name = "size", required = false, defaultValue = "10")
                                                    @Positive int size) throws ServiceException {
        PageRequestParameters pageRequest = PageRequestParameters.of(page, size);
        List<OrderDto> orders = orderService.findUserOrders(userId, pageRequest);
        orders.forEach(e -> e.setUserId(userId));

        return orderCollectionAssembler.toCollectionModel(orders, pageRequest);
    }

    @GetMapping("/{userId}/orders/{orderId}")
    public OrderDto getUsersOrderById(@PathVariable("userId") @Positive long userId,
                                      @PathVariable("orderId") @Positive long orderId) throws ServiceException {
        OrderDto orderDto = orderService.findUserOrderById(userId, orderId);
        orderDto.setUserId(userId);
        return orderDto;
    }

    @PostMapping("/{userId}/orders")
    public OrderDto createUserOrder(@PathVariable("userId") @Positive long userId,
                                    @RequestBody @Valid @NotEmpty List<@Positive Long> certificatesIds)
            throws ServiceException {
        OrderDto orderDto = orderService.createUserOrder(userId, certificatesIds);
        orderDto.setUserId(userId);
        return orderDto;
    }

}
