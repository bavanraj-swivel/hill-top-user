package com.hilltop.user.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Login responseDto
 */
@Getter
@AllArgsConstructor
public class LoginResponseDto implements ResponseDto {

    private String token;
}
