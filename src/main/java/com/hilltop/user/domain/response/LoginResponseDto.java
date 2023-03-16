package com.hilltop.user.domain.response;

import com.hilltop.user.domain.entity.User;
import com.hilltop.user.enumeration.UserType;
import lombok.Getter;

/**
 * Login responseDto
 */
@Getter
public class LoginResponseDto implements ResponseDto {

    private final String userId;
    private final UserType userType;

    public LoginResponseDto(User user) {
        this.userId = user.getId();
        this.userType = user.getUserType();
    }
}
