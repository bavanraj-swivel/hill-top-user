package com.hilltop.user.service;

import com.hilltop.user.domain.entity.User;
import com.hilltop.user.domain.request.UserRequestDto;
import com.hilltop.user.enumeration.UserType;
import com.hilltop.user.exception.HillTopUserApplicationException;
import com.hilltop.user.exception.UserExistException;
import com.hilltop.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.dao.DataAccessException;

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

    private static final String MOBILE_NO = "0779090909";
    private static final String PASSWORD = "password";
    private static final String FAILED = "Failed.";
    private final UserRequestDto userRequestDto = getUserRequestDto();
    private final User user = getUser();
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtTokenService jwtTokenService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        openMocks(this);
        userService = new UserService(userRepository, jwtTokenService);
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
        when(userRepository.save(any())).thenThrow(new DataAccessException(FAILED) {
        });
        HillTopUserApplicationException exception = assertThrows(HillTopUserApplicationException.class,
                () -> userService.addUser(userRequestDto));
        assertEquals("Failed to save user info in database.", exception.getMessage());
    }

    /**
     * unit tests for checkMobileNoExist() method.
     */
    @Test
    void Should_ThrowUserExistException_When_FailedToAccessUserData() {
        when(userRepository.findByMobileNo(any())).thenReturn(Optional.of(user));
        UserExistException exception = assertThrows(UserExistException.class,
                () -> userService.checkMobileNoExist(MOBILE_NO));
        assertEquals("Mobile number already registered.", exception.getMessage());
    }

    @Test
    void Should_ThrowHillTopUserApplicationException_When_FailedToGetUserByMobileNo() {
        when(userRepository.findByMobileNo(any())).thenThrow(new DataAccessException(FAILED) {
        });
        HillTopUserApplicationException exception = assertThrows(HillTopUserApplicationException.class,
                () -> userService.checkMobileNoExist(MOBILE_NO));
        assertEquals("Failed to get user by mobileNo from database.", exception.getMessage());
    }

    /**
     * unit tests for generateToken() method.
     */
    @Test
    void Should_CallJwtTokenServiceGenerateTokenMethod_When_GenerateTokenIsCalled() {
        userService.generateToken("0779777782");
        verify(jwtTokenService, times(1)).generateToken(anyString());
    }

    /**
     * unit tests for generateToken() method.
     */
    @Test
    void Should_CallJwtTokenServiceValidateTokenMethod_When_ValidateTokenIsCalled() {
        String token = userService.generateToken("0779777782");
        userService.validateToken(token);
        verify(jwtTokenService, times(1)).validateToken(token);
    }

    /**
     * This method is used to mock userRequestDto.
     *
     * @return userRequestDto
     */
    private UserRequestDto getUserRequestDto() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("User");
        userRequestDto.setMobileNo(MOBILE_NO);
        userRequestDto.setPassword(PASSWORD);
        userRequestDto.setUserType(UserType.USER);
        return userRequestDto;
    }

    /**
     * This method is used to mock user.
     *
     * @return user
     */
    private User getUser() {
        User user = new User();
        user.setMobileNo(MOBILE_NO);
        user.setPassword(PASSWORD);
        user.setUserType(UserType.USER);
        return user;
    }
}