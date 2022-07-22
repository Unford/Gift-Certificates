package com.epam.esm.api.controller;

import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.dto.GiftCertificateDto;
import com.epam.esm.core.model.dto.OrderDto;
import com.epam.esm.core.model.dto.TagDto;
import com.epam.esm.core.model.dto.UserDto;
import com.epam.esm.core.model.dto.request.PageRequestParameters;
import com.epam.esm.core.service.OrderService;
import com.epam.esm.core.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
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


    @Autowired
    public UserController(UserServiceImpl userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") @Positive long id) throws ServiceException {
        return userService.findById(id);
    }

    /**
     * Gets all tags.
     *
     * @return the all tags
     */
    @GetMapping
    public List<UserDto> getUsers(@Valid PageRequestParameters pageRequestParameters, BindingResult bindingResult) {
        return userService.findAll(pageRequestParameters);
    }

    @GetMapping("/{id}/orders")
    public List<OrderDto> getUsersOrders(@PathVariable("id") @Positive long userId,
                                         @Valid PageRequestParameters pageRequestParameters,
                                         BindingResult bindingResult) throws ServiceException {
        return orderService.findUserOrders(userId, pageRequestParameters);
    }

    @GetMapping("/{userId}/orders/{orderId}")
    public OrderDto getUsersOrderById(@PathVariable("userId") @Positive long userId,
                                            @PathVariable("orderId") @Positive long orderId) throws ServiceException {
        return orderService.findUserOrderById(userId, orderId);
    }

    @PostMapping("/{userId}/orders")
    public OrderDto createUserOrder(@PathVariable("userId") @Positive long userId,
                                    @RequestBody @Valid @NotEmpty List< @Positive Long> certificatesIds)
            throws ServiceException {
        return orderService.createUserOrder(userId, certificatesIds);
    }

}
