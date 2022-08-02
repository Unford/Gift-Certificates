package com.epam.esm.api.controller;

import com.epam.esm.api.hateoas.assembler.impl.OrderCollectionAssembler;
import com.epam.esm.api.hateoas.assembler.impl.UserCollectionAssembler;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.dto.OrderDto;
import com.epam.esm.core.model.dto.UserDto;
import com.epam.esm.core.model.dto.request.SimplePageRequest;
import com.epam.esm.core.service.OrderService;
import com.epam.esm.core.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.List;
/**
 * It's a controller that provides operations on users and their orders
 */
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

    /**
     * This method returns a user by id
     *
     * @param id the id of the user to be retrieved
     * @throws ServiceException if user not found
     * @return UserDto
     */
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") @Positive long id) throws ServiceException {
        return userService.findById(id);
    }

    /**
     * This method returns a collection of users, with pagination capability
     *
     * @param page The page number to return.
     * @param size The number of items to return per page.
     * @return A collection of userDto
     */
    @GetMapping
    public CollectionModel<UserDto> getUsers(
            @RequestParam(name = "page", required = false, defaultValue = "1") @Positive int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") @Positive int size)
            throws ServiceException {
        SimplePageRequest requestParameters = SimplePageRequest.of(page, size);
        List<UserDto> userDtoList = userService.findAll(requestParameters);
        return userCollectionAssembler.toCollectionModel(userDtoList, requestParameters);
    }

    /**
     * This function returns a collection of orders for a given user
     *
     * @param userId the id of the user whose orders we want to retrieve
     * @param page the page number, starting from 1.
     * @param size The number of items to be returned in a page.
     * @return CollectionModel<OrderDto> user orders
     */
    @GetMapping("/{userId}/orders")
    public CollectionModel<OrderDto> getUsersOrders(@PathVariable("userId") @Positive long userId,
                                                    @RequestParam(name = "page", required = false, defaultValue = "1")
                                                    @Positive int page,
                                                    @RequestParam(name = "size", required = false, defaultValue = "10")
                                                    @Positive int size) throws ServiceException {
        SimplePageRequest pageRequest = SimplePageRequest.of(page, size);
        List<OrderDto> orders = orderService.findUserOrders(userId, pageRequest);
        orders.forEach(e -> e.setUserId(userId));

        return orderCollectionAssembler.toCollectionModel(orders, pageRequest);
    }

    /**
     * This method returns an order by id for a given user
     *
     * @param userId The user's id.
     * @param orderId The order ID.
     * @throws ServiceException if user or order not found
     * @return OrderDto order
     */
    @GetMapping("/{userId}/orders/{orderId}")
    public OrderDto getUsersOrderById(@PathVariable("userId") @Positive long userId,
                                      @PathVariable("orderId") @Positive long orderId) throws ServiceException {
        OrderDto orderDto = orderService.findUserOrderById(userId, orderId);
        orderDto.setUserId(userId);
        return orderDto;
    }

    /**
     * It creates a new order for a user with the given userId, and returns the created order
     *
     * @param userId the id of the user who is making the order
     * @param certificatesIds a list of certificate IDs that the user wants to buy.
     * @return OrderDto created order
     */
    @PostMapping("/{userId}/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createUserOrder(@PathVariable("userId") @Positive long userId,
                                    @RequestBody @Valid @NotEmpty List<@Positive Long> certificatesIds)
            throws ServiceException {
        OrderDto orderDto = orderService.createUserOrder(userId, certificatesIds);
        orderDto.setUserId(userId);
        return orderDto;
    }

}
