package com.hilltop.user.service;

import com.hilltop.user.domain.entity.User;
import com.hilltop.user.domain.request.LoginRequestDto;
import com.hilltop.user.domain.request.UserRequestDto;
import com.hilltop.user.enumeration.UserType;
import com.hilltop.user.exception.HillTopUserApplicationException;
import com.hilltop.user.exception.InvalidLoginException;
import com.hilltop.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

/**
 * User service test
 * Unit tests for {@link  UserService}
 */
class UserServiceTest {

    private final UserRequestDto userRequestDto = getUserRequestDto();
    private final LoginRequestDto loginRequestDto = getLoginRequestDto();
    private final User user = getUser();
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        openMocks(this);
        userService = new UserService(userRepository, passwordEncoder);
    }

    /**
     * Unit tests for addUser() method.
     */
    @Test
    void Should_SaveUserDetailOnDatabase_When_ValidDataIsGiven() {
        userService.addUser(userRequestDto);
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void Should_ThrowHillTopUserApplicationException_When_FailedToAddUserData() {
        when(userRepository.save(any())).thenThrow(new DataAccessException("Failed") {
        });
        HillTopUserApplicationException exception = assertThrows(HillTopUserApplicationException.class, () -> {
            userService.addUser(userRequestDto);
        });
        assertEquals("Failed to save user info in database.", exception.getMessage());
    }

    /**
     * unit tests for loginUser() method.
     */
    @Test
    void Should_ReturnUser_When_ValidDataIsGiven() {
        when(userRepository.findByMobileNo(any())).thenReturn(Optional.of(user));
        when((passwordEncoder.matches(any(), any()))).thenReturn(true);
        User userFromDb = userService.loginUser(loginRequestDto);
        assertEquals(loginRequestDto.getMobileNo(), userFromDb.getMobileNo());
    }

    @Test
    void Should_ThrowHillTopUserApplicationException_When_FailedToAccessUserData() {
        when(userRepository.findByMobileNo(any())).thenThrow(new DataAccessException("Failed") {
        });
        HillTopUserApplicationException exception = assertThrows(HillTopUserApplicationException.class, () -> {
            userService.loginUser(loginRequestDto);
        });
        assertEquals("Failed to get user from database.", exception.getMessage());
    }

    @Test
    void Should_ThrowInvalidLoginExceptionException_When_PasswordDoesntMatch() {
        when(userRepository.findByMobileNo(any())).thenReturn(Optional.of(user));
        when((passwordEncoder.matches(any(), any()))).thenReturn(false);
        InvalidLoginException exception = assertThrows(InvalidLoginException.class, () -> {
            userService.loginUser(loginRequestDto);
        });
        assertEquals("Invalid credentials.", exception.getMessage());
    }

    /**
     * This method is used to mock userRequestDto.
     *
     * @return userRequestDto
     */
    private UserRequestDto getUserRequestDto() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("User");
        userRequestDto.setMobileNo("779090909");
        userRequestDto.setPassword("password");
        userRequestDto.setUserType(UserType.USER);
        return userRequestDto;
    }

    /**
     * This method is used to mock loginRequestDto.
     *
     * @return loginRequestDto
     */
    private LoginRequestDto getLoginRequestDto() {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setMobileNo("779090909");
        loginRequestDto.setPassword("password");
        loginRequestDto.setUserType(UserType.USER);
        return loginRequestDto;
    }

    /**
     * This method is used to mock user.
     *
     * @return user
     */
    private User getUser() {
        User user = new User();
        user.setMobileNo("779090909");
        user.setPassword("password");
        user.setUserType(UserType.USER);
        return user;
    }
}