package test.epam.esm.api.controller;

import com.epam.esm.api.config.ApiApplication;
import com.epam.esm.api.controller.UserController;
import com.epam.esm.api.hateoas.assembler.impl.OrderCollectionAssembler;
import com.epam.esm.api.hateoas.assembler.impl.UserCollectionAssembler;
import com.epam.esm.api.hateoas.processor.OrderModelProcessor;
import com.epam.esm.api.hateoas.processor.UserModelProcessor;
import com.epam.esm.core.exception.CustomErrorCode;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.dto.OrderDto;
import com.epam.esm.core.model.dto.UserDto;
import com.epam.esm.core.service.OrderService;
import com.epam.esm.core.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.core.exception.CustomErrorCode.CONSTRAINT_VIOLATION;
import static com.epam.esm.core.exception.CustomErrorCode.RESOURCE_NOT_FOUND;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = {ApiApplication.class})
@Import({UserCollectionAssembler.class, UserModelProcessor.class,
        OrderCollectionAssembler.class, OrderModelProcessor.class})
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserServiceImpl userService;
    @MockBean
    private OrderService orderService;

    private final Class<UserController> controllerClass = UserController.class;

    @Test
    void givenUser_whenFindById_thenReturn() throws Exception {
        UserDto userDto = new UserDto(1L, "NAME", "LOGIN");
        Mockito.when(userService.findById(Mockito.anyLong())).thenReturn(userDto);
        String uri = linkTo(methodOn(controllerClass).getUserById(userDto.getId()))
                .toUriComponentsBuilder()
                .toUriString();

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.login").value(userDto.getLogin()))
                .andExpect(jsonPath("$._links.*.href", notNullValue()));

        Mockito.verify(userService, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    void givenUser_whenFindByInvalidId_thenConstraintViolation() throws Exception {
        CustomErrorCode errorCode = CONSTRAINT_VIOLATION;
        long invalidId = -1L;
        Mockito.when(userService.findById(Mockito.anyLong()))
                .thenThrow(new ServiceException(Long.toString(invalidId), errorCode));

        String uri = linkTo(methodOn(controllerClass).getUserById(invalidId))
                .toUriComponentsBuilder()
                .toUriString();

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().is(errorCode.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());

        Mockito.verify(userService, Mockito.times(0)).findById(Mockito.anyLong());
    }

    @Test
    void givenUserNotExist_whenFindById_thenNotFound() throws Exception {
        CustomErrorCode errorCode = RESOURCE_NOT_FOUND;
        long id = 1L;
        Mockito.when(userService.findById(Mockito.anyLong()))
                .thenThrow(new ServiceException(Long.toString(id), errorCode));

        String uri = linkTo(methodOn(controllerClass).getUserById(id))
                .toUriComponentsBuilder()
                .toUriString();

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().is(errorCode.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());

        Mockito.verify(userService, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    void givenUsers_whenFindAll_thenReturn() throws Exception {
        List<UserDto> userDtoList = new ArrayList<>();
        for (long i = 1; i <= 3; i++) {
            userDtoList.add(new UserDto(i, "name " + i, "login " + i));
        }

        Mockito.when(userService.findAll(Mockito.any())).thenReturn(userDtoList);

        String uri = linkTo(methodOn(controllerClass).getUsers(1, 3))
                .toUriComponentsBuilder()
                .toUriString();

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.users", hasSize(userDtoList.size())))
                .andExpect(jsonPath("$._embedded.users[*]._links.*.href", notNullValue()))
                .andExpect(jsonPath("$._links.*.href", notNullValue()));

        Mockito.verify(userService, Mockito.times(1)).findAll(Mockito.any());
    }

    @Test
    void givenOrder_whenFindUserOrderById_thenReturn() throws Exception {
        OrderDto order = new OrderDto(1L, BigDecimal.ONE, LocalDateTime.now());
        order.setUserId(1L);
        Mockito.when(orderService.findUserOrderById(order.getUserId(), order.getId())).thenReturn(order);

        String uri = linkTo(methodOn(controllerClass).getUsersOrderById(order.getUserId(), order.getId()))
                .toUriComponentsBuilder()
                .toUriString();

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order.getId()))
                .andExpect(jsonPath("$.cost").value(order.getCost()))
                .andExpect(jsonPath("$.purchaseDate").value(order.getPurchaseDate()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.userId").doesNotExist())
                .andExpect(jsonPath("$._links.*.href", notNullValue()));

        Mockito.verify(orderService, Mockito.times(1))
                .findUserOrderById(order.getUserId(), order.getId());
    }

    @Test
    void givenOrder_whenFindUserOrderByInvalidId_thenConstraintViolation() throws Exception {
        CustomErrorCode errorCode = CONSTRAINT_VIOLATION;
        long invalidId = -1L;
        Mockito.when(orderService.findUserOrderById(Mockito.anyLong(), Mockito.anyLong()))
                .thenThrow(new ServiceException(Long.toString(invalidId), errorCode));

        String uri = linkTo(methodOn(controllerClass).getUsersOrderById(invalidId, invalidId))
                .toUriComponentsBuilder()
                .toUriString();

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().is(errorCode.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());

        Mockito.verify(orderService, Mockito.times(0))
                .findUserOrderById(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    void givenUserNotExist_whenFindUserOrderById_thenNotFound() throws Exception {
        CustomErrorCode errorCode = RESOURCE_NOT_FOUND;
        long id = 1L;
        Mockito.when(orderService.findUserOrderById(Mockito.anyLong(), Mockito.anyLong()))
                .thenThrow(new ServiceException(Long.toString(id), errorCode));

        String uri = linkTo(methodOn(controllerClass).getUsersOrderById(id, id))
                .toUriComponentsBuilder()
                .toUriString();

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().is(errorCode.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());

        Mockito.verify(orderService, Mockito.times(1))
                .findUserOrderById(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    void givenOrders_whenFindAllUserOrders_thenReturn() throws Exception {
        List<OrderDto> orders = new ArrayList<>();
        long userId = 1;
        for (long i = 1; i <= 3; i++) {
            OrderDto order = new OrderDto(i, BigDecimal.valueOf(i), LocalDateTime.now().minusHours(i), userId);
            orders.add(order);
        }

        Mockito.when(orderService.findUserOrders(Mockito.anyLong(), Mockito.any())).thenReturn(orders);

        String uri = linkTo(methodOn(controllerClass).getUsersOrders(userId, 1, orders.size()))
                .toUriComponentsBuilder()
                .toUriString();

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.orders", hasSize(orders.size())))
                .andExpect(jsonPath("$._embedded.orders[*].id", Matchers.containsInAnyOrder(1, 2, 3)))
                .andExpect(jsonPath("$._embedded.orders[*]._links.*.href", notNullValue()))
                .andExpect(jsonPath("$._links.*.href", notNullValue()));

        Mockito.verify(orderService, Mockito.times(1)).findUserOrders(Mockito.anyLong(),
                Mockito.any());
    }

    @Test
    void givenUserNotExist_whenFindAllUserOrders_thenNotFound() throws Exception {
        CustomErrorCode errorCode = RESOURCE_NOT_FOUND;
        long id = 1L;
        Mockito.when(orderService.findUserOrders(Mockito.anyLong(), Mockito.any()))
                .thenThrow(new ServiceException(Long.toString(id), errorCode));

        String uri = linkTo(methodOn(controllerClass).getUsersOrders(id, 1, 10))
                .toUriComponentsBuilder()
                .toUriString();

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().is(errorCode.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());

        Mockito.verify(orderService, Mockito.times(1))
                .findUserOrders(Mockito.anyLong(), Mockito.any());
    }

    @Test
    void givenOrder_whenFindAllUserOrders_thenConstraintViolation() throws Exception {
        CustomErrorCode errorCode = CONSTRAINT_VIOLATION;
        long userId = -1L;
        String uri = linkTo(methodOn(controllerClass).getUsersOrders(userId, 1, 10))
                .toUriComponentsBuilder()
                .toUriString();

        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().is(errorCode.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());

        Mockito.verify(orderService, Mockito.times(0))
                .findUserOrders(Mockito.anyLong(), Mockito.any());
    }

    @Test
    void givenOrder_whenCreateOrder_thenReturn() throws Exception {
        List<Long> certificateIds = new ArrayList<>();
        for (long i = 1; i <= 3; i++) {
            certificateIds.add(i);
        }
        long userId = 1L;
        OrderDto createdOrder = new OrderDto(1L, BigDecimal.TEN, LocalDateTime.now(), userId);
        Mockito.when(orderService.createUserOrder(Mockito.anyLong(), Mockito.anyList()))
                .thenReturn(createdOrder);

        String uri = linkTo(methodOn(controllerClass).createUserOrder(userId, certificateIds))
                .toUriComponentsBuilder()
                .toUriString();

        ObjectMapper writer = new ObjectMapper();

        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writer.writeValueAsString(certificateIds)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(createdOrder.getId()))
                .andExpect(jsonPath("$.cost").value(createdOrder.getCost()))
                .andExpect(jsonPath("$.purchaseDate").value(createdOrder.getPurchaseDate()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.userId").doesNotExist())
                .andExpect(jsonPath("$._links.*.href").isNotEmpty());

        Mockito.verify(orderService, Mockito.times(1))
                .createUserOrder(Mockito.anyLong(), Mockito.anyList());
    }

    @Test
    void givenInvalidCertificateId_whenCreateOrder_thenConstraintViolation() throws Exception {
        List<Long> certificateIds = new ArrayList<>();
        certificateIds.add(-11L);
        for (long i = 1; i <= 3; i++) {
            certificateIds.add(i);
        }

        Mockito.when(orderService.createUserOrder(Mockito.anyLong(), Mockito.anyList()))
                .thenReturn(null);

        String uri = linkTo(methodOn(controllerClass).createUserOrder(1, certificateIds))
                .toUriComponentsBuilder()
                .toUriString();

        ObjectMapper writer = new ObjectMapper();
        CustomErrorCode errorCode = CONSTRAINT_VIOLATION;

        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writer.writeValueAsString(certificateIds)))
                .andDo(print())
                .andExpect(status().is(errorCode.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());

        Mockito.verify(orderService, Mockito.times(0))
                .createUserOrder(Mockito.anyLong(), Mockito.anyList());
    }

    @Test
    void givenInvalidUserId_whenCreateOrder_thenConstraintViolation() throws Exception {
        List<Long> certificateIds = new ArrayList<>();
        for (long i = 1; i <= 3; i++) {
            certificateIds.add(i);
        }
        long userId = -111;
        Mockito.when(orderService.createUserOrder(Mockito.anyLong(), Mockito.anyList()))
                .thenReturn(null);

        String uri = linkTo(methodOn(controllerClass).createUserOrder(userId, certificateIds))
                .toUriComponentsBuilder()
                .toUriString();

        ObjectMapper writer = new ObjectMapper();
        CustomErrorCode errorCode = CONSTRAINT_VIOLATION;

        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writer.writeValueAsString(certificateIds)))
                .andDo(print())
                .andExpect(status().is(errorCode.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());

        Mockito.verify(orderService, Mockito.times(0))
                .createUserOrder(Mockito.anyLong(), Mockito.anyList());
    }

    @Test
    void givenOrders_whenUserNotFoundCreateOrder_thenNotFound() throws Exception {
        List<Long> certificateIds = new ArrayList<>();
        for (long i = 1; i <= 3; i++) {
            certificateIds.add(i);
        }
        long userId = 3;
        CustomErrorCode errorCode = RESOURCE_NOT_FOUND;

        Mockito.when(orderService.createUserOrder(Mockito.anyLong(), Mockito.anyList()))
                .thenThrow(new ServiceException(Long.toString(userId), errorCode));

        String uri = linkTo(methodOn(controllerClass).createUserOrder(userId, certificateIds))
                .toUriComponentsBuilder()
                .toUriString();

        ObjectMapper writer = new ObjectMapper();

        mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writer.writeValueAsString(certificateIds)))
                .andDo(print())
                .andExpect(status().is(errorCode.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());

        Mockito.verify(orderService, Mockito.times(1))
                .createUserOrder(Mockito.anyLong(), Mockito.anyList());
    }
}
