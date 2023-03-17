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

    private static final String MOBILE_NO_PATTERN = "^\\d{10}$";
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

    /**
     * This method is used to validate mobile number pattern.
     *
     * @return true/false
     */
    public boolean isValidMobileNo() {
        return mobileNo.matches(MOBILE_NO_PATTERN);
    }

}
