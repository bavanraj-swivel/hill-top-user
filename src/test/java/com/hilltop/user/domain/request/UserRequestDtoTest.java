package com.hilltop.user.domain.request;

import com.hilltop.user.enumeration.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * User requestDto test
 * Unit tests for {@link  UserRequestDto}
 */
class UserRequestDtoTest {

    private UserRequestDto userRequestDto;

    @BeforeEach
    void setUp() {
        userRequestDto = new UserRequestDto();
        userRequestDto.setName("User");
        userRequestDto.setMobileNo("779090909");
        userRequestDto.setPassword("password");
        userRequestDto.setUserType(UserType.USER);
    }

    /**
     * Unit tests for isRequiredFieldsAvailable() method
     */
    @Test
    void Should_ReturnTrue_When_RequiredFieldsAreAvailable() {
        assertTrue(userRequestDto.isRequiredFieldsAvailable());
    }

    @Test
    void Should_ReturnFalse_When_RequiredFieldsAreMissing() {
        userRequestDto.setPassword(null);
        assertFalse(userRequestDto.isRequiredFieldsAvailable());
    }

}