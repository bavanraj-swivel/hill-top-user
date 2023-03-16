package com.hilltop.user.domain.request;

import com.hilltop.user.enumeration.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Login requestDto test
 * Unit tests for {@link  LoginRequestDto}
 */
class LoginRequestDtoTest {

    private LoginRequestDto loginRequestDto;

    @BeforeEach
    void setUp() {
        loginRequestDto = new LoginRequestDto();
        loginRequestDto.setMobileNo("779090909");
        loginRequestDto.setPassword("password");
        loginRequestDto.setUserType(UserType.USER);
    }

    /**
     * Unit tests for isRequiredFieldsAvailable() method
     */
    @Test
    void Should_ReturnTrue_When_RequiredFieldsAreAvailable() {
        assertTrue(loginRequestDto.isRequiredFieldsAvailable());
    }

    @Test
    void Should_ReturnFalse_When_RequiredFieldsAreMissing() {
        loginRequestDto.setPassword(null);
        assertFalse(loginRequestDto.isRequiredFieldsAvailable());
    }
}