package test.epam.esm.core.service;

import com.epam.esm.core.config.MapperConfig;
import com.epam.esm.core.exception.CustomErrorCode;
import com.epam.esm.core.exception.ServiceException;
import com.epam.esm.core.model.domain.Order;
import com.epam.esm.core.model.domain.User;
import com.epam.esm.core.model.dto.UserDto;
import com.epam.esm.core.model.dto.request.SimplePageRequest;
import com.epam.esm.core.repository.impl.UserRepositoryImpl;
import com.epam.esm.core.service.impl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

class UserServiceImplTest {

    private final UserServiceImpl userService;
    private final UserRepositoryImpl userRepository;
    private List<User> expectedUserList;
    private List<UserDto> expectedUserDtoList;


    public UserServiceImplTest() {
        this.userRepository = Mockito.mock(UserRepositoryImpl.class);
        ModelMapper modelMapper = new MapperConfig().modelMapper();
        this.userService = new UserServiceImpl(userRepository, modelMapper);
    }

    @BeforeEach
    public void setUp() {
        expectedUserDtoList = new ArrayList<>();
        expectedUserList = new ArrayList<>();
        for (long i = 1; i <= 5; i++) {
            String name = "name_" + i;
            String login = "login_" + i;
            Order order = new Order();
            order.setId(6L - i);

            User user = new User();
            user.setId(i);
            user.setName(name);
            user.setPassword("password_" + i);
            user.setLogin(login);
            user.setOrders(Collections.singleton(order));

            UserDto userDto = new UserDto();
            userDto.setId(i);
            userDto.setName(name);
            userDto.setLogin(login);

            expectedUserList.add(user);
            expectedUserDtoList.add(userDto);
        }
    }

    @Test
    void givenUsers_whenFindAll_thenReturn() {
        Mockito.when(userRepository.findAll(Mockito.any())).thenReturn(expectedUserList);
        List<UserDto> actual = userService.findAll(SimplePageRequest.of(1, 10));
        Assertions.assertThat(actual).containsExactlyElementsOf(expectedUserDtoList);
        Mockito.verify(userRepository, Mockito.times(1)).findAll(any());
    }

    @Test
    void givenTag_whenFindById_thenReturn() throws ServiceException {
        User user = expectedUserList.get(1);
        UserDto expected = expectedUserDtoList.get(1);

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));
        UserDto actual = userService.findById(user.getId());
        assertEquals(expected, actual);
        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    void givenTagNotExist_whenFindById_theNotFound() throws ServiceException {
        Long userId = 22L;
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () -> userService.findById(userId));
        assertEquals(CustomErrorCode.RESOURCE_NOT_FOUND, exception.getErrorCode());
        assertEquals(userId, Long.valueOf(exception.getMessage()));

        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    void givenUnsupportedOperation_whenCreate_thenThrow() {
        UserDto userDto = expectedUserDtoList.get(0);
        Mockito.when(userRepository.create(Mockito.any())).thenReturn(null);
        assertThrows(UnsupportedOperationException.class, () -> userService.create(userDto));
        Mockito.verify(userRepository, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void givenUnsupportedOperation_whenUpdate_thenThrow() {
        UserDto userDto = expectedUserDtoList.get(0);
        Mockito.when(userRepository.update(Mockito.any())).thenReturn(null);
        assertThrows(UnsupportedOperationException.class, () -> userService.update(userDto));
        Mockito.verify(userRepository, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    void givenUnsupportedOperation_whenDeleteById_thenThrow() {
        Mockito.doNothing().when(userRepository).deleteById(Mockito.anyLong());
        assertThrows(UnsupportedOperationException.class, () -> userService.deleteById(-1L));
        Mockito.verify(userRepository, Mockito.times(0)).deleteById(Mockito.anyLong());
    }
}