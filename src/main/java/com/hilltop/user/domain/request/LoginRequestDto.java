package com.hilltop.user.domain.request;

import com.hilltop.user.enumeration.UserType;
import lombok.Getter;
import lombok.Setter;

/**
 * Login requestDto
 */
@Getter
@Setter
public class LoginRequestDto implements RequestDto {

    private String mobileNo;
    private String password;
    private UserType userType;

    /**
     * Used to validate required fields.
     *
     * @return true/false
     */
    @Override
    public boolean isRequiredFieldsAvailable() {
        return isNonEmpty(mobileNo) && isNonEmpty(password) && userType != null;
    }

}
