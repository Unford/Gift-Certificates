package test.epam.esm.core.service;

import com.epam.esm.core.config.MapperConfig;
import com.epam.esm.core.exception.CustomErrorCode;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.domain.GiftCertificate;
import com.epam.esm.core.model.domain.Order;
import com.epam.esm.core.model.domain.User;
import com.epam.esm.core.model.dto.OrderDto;
import com.epam.esm.core.model.dto.request.SimplePageRequest;
import com.epam.esm.core.repository.impl.GiftCertificateRepositoryImpl;
import com.epam.esm.core.repository.impl.OrderRepositoryImpl;
import com.epam.esm.core.repository.impl.UserRepositoryImpl;
import com.epam.esm.core.service.impl.OrderServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

class OrderServiceImplTest {
    private final OrderServiceImpl orderService;
    private final OrderRepositoryImpl orderRepository;
    private final UserRepositoryImpl userRepository;
    private final GiftCertificateRepositoryImpl certificateRepository;

    private List<OrderDto> expectedOrderDtoList;
    private List<Order> expectedOrderList;


    public OrderServiceImplTest() {
        this.orderRepository = Mockito.mock(OrderRepositoryImpl.class);
        this.userRepository = Mockito.mock(UserRepositoryImpl.class);
        this.certificateRepository = Mockito.mock(GiftCertificateRepositoryImpl.class);
        ModelMapper modelMapper = new MapperConfig().modelMapper();

        this.orderService = new OrderServiceImpl(orderRepository, userRepository,
                certificateRepository, modelMapper);
    }

    @BeforeEach
    void setUp() {
        expectedOrderDtoList = new ArrayList<>();
        expectedOrderList = new ArrayList<>();
        for (long i = 1; i <= 5; i++) {
            LocalDateTime now = LocalDateTime.now();
            User user = new User();
            user.setId(6L - i);
            user.setName("user_name_" + i);
            Order order = new Order();
            order.setId(i);
            order.setPurchaseDate(now);
            order.setUser(user);
            order.setCost(BigDecimal.valueOf(i));
            OrderDto orderDto = new OrderDto();
            orderDto.setId(i);
            orderDto.setPurchaseDate(now);
            orderDto.setUserId(i);
            orderDto.setCost(order.getCost());
            expectedOrderDtoList.add(orderDto);
            expectedOrderList.add(order);
        }
    }

    @Test
    void givenOrders_whenFindUserOrders_thenReturn() throws ServiceException {
        Order expectedOrder = expectedOrderList.get(0);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(expectedOrder.getUser()));
        Mockito.when(orderRepository.findAll(any(), any())).thenReturn(expectedOrderList);

        List<OrderDto> actual = orderService.findUserOrders(1L, SimplePageRequest.of(1, 10));
        Assertions.assertThat(actual).containsExactlyElementsOf(expectedOrderDtoList);
        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(orderRepository, Mockito.times(1)).findAll(any(), any());
    }

    @Test
    void givenUserNotExist_whenFindUserOrders_thenNotFound() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Mockito.when(orderRepository.findAll(any(), any())).thenReturn(expectedOrderList);

        ServiceException exception = assertThrows(ServiceException.class,
                () -> orderService.findUserOrders(1L, SimplePageRequest.of(1, 10)));
        assertEquals(CustomErrorCode.RESOURCE_NOT_FOUND, exception.getErrorCode());

        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(orderRepository, Mockito.times(0)).findAll(any(), any());
    }

    @Test
    void givenOrder_whenFindUserOrderById_thenReturn() throws ServiceException {
        Order expectedOrder = expectedOrderList.get(0);
        OrderDto expectedOrderDto = expectedOrderDtoList.get(0);

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(expectedOrder.getUser()));
        Mockito.when(orderRepository.findFirstWhere(any())).thenReturn(Optional.of(expectedOrder));

        OrderDto actual = orderService.findUserOrderById(1L, 1L);
        assertEquals(expectedOrderDto, actual);
        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(orderRepository, Mockito.times(1)).findFirstWhere(any());
    }

    @Test
    void givenUserNotExist_whenFindUserOrderById_thenNotFound() throws ServiceException {
        Order expectedOrder = expectedOrderList.get(0);
        User user = expectedOrder.getUser();
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Mockito.when(orderRepository.findFirstWhere(any())).thenReturn(Optional.of(expectedOrder));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> orderService.findUserOrderById(user.getId(), expectedOrder.getId()));
        assertEquals(CustomErrorCode.RESOURCE_NOT_FOUND, exception.getErrorCode());
        assertEquals(user.getId(), Long.valueOf(exception.getMessage()));
        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(orderRepository, Mockito.times(0)).findFirstWhere(any());
    }

    @Test
    void givenOrderNotExist_whenFindUserOrderById_thenNotFound() throws ServiceException {
        Order expectedOrder = expectedOrderList.get(0);
        User user = expectedOrder.getUser();
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(orderRepository.findFirstWhere(any())).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class,
                () -> orderService.findUserOrderById(user.getId(), expectedOrder.getId()));
        assertEquals(CustomErrorCode.RESOURCE_NOT_FOUND, exception.getErrorCode());
        assertEquals(expectedOrder.getId(), Long.valueOf(exception.getMessage()));

        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(orderRepository, Mockito.times(1)).findFirstWhere(any());
    }

    @Test
    void givenOrders_whenCreateUserOrder_thenReturn() throws ServiceException {
        Order expectedOrder = expectedOrderList.get(0);
        OrderDto expectedOrderDto = expectedOrderDtoList.get(0);
        GiftCertificate giftCertificate = new GiftCertificate();
        expectedOrder.setGiftCertificates(Collections.singletonList(giftCertificate));

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(expectedOrder.getUser()));
        Mockito.when(orderRepository.create(any())).thenReturn(expectedOrder);
        Mockito.when(certificateRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(giftCertificate));


        OrderDto actual = orderService.createUserOrder(1L, Collections.singletonList(1L));
        assertEquals(expectedOrderDto, actual);

        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(orderRepository, Mockito.times(1)).create(any());
        Mockito.verify(certificateRepository, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    void givenUserNotExist_whenCreateUserOrder_thenNotFound() throws ServiceException {
        Order expectedOrder = expectedOrderList.get(0);
        User user = expectedOrder.getUser();
        GiftCertificate giftCertificate = new GiftCertificate();
        expectedOrder.setGiftCertificates(Collections.singletonList(giftCertificate));

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Mockito.when(orderRepository.create(any())).thenReturn(null);
        Mockito.when(certificateRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());


        ServiceException exception = assertThrows(ServiceException.class,
                () -> orderService.createUserOrder(user.getId(), Collections.singletonList(1L)));
        assertEquals(CustomErrorCode.RESOURCE_NOT_FOUND, exception.getErrorCode());
        assertEquals(user.getId(), Long.valueOf(exception.getMessage()));

        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(certificateRepository, Mockito.times(0)).findById(Mockito.anyLong());
        Mockito.verify(orderRepository, Mockito.times(0)).create(any());
    }

    @Test
    void givenCertificateNotExist_whenCreateUserOrder_thenNotFound() throws ServiceException {
        Order expectedOrder = expectedOrderList.get(0);
        User user = expectedOrder.getUser();
        long giftCertificateId = expectedOrder.getId() + user.getId();
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(giftCertificateId);
        expectedOrder.setGiftCertificates(Collections.singletonList(giftCertificate));

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(certificateRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Mockito.when(orderRepository.create(any())).thenReturn(null);


        ServiceException exception = assertThrows(ServiceException.class,
                () -> orderService.createUserOrder(user.getId(), Collections.singletonList(giftCertificateId)));
        assertEquals(CustomErrorCode.RESOURCE_NOT_FOUND, exception.getErrorCode());
        assertEquals(giftCertificateId, Long.valueOf(exception.getMessage()));

        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(certificateRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(orderRepository, Mockito.times(0)).create(any());
    }
}