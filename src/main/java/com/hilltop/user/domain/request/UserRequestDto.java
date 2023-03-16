package com.hilltop.user.domain.request;

import com.hilltop.user.enumeration.UserType;
import lombok.Getter;
import lombok.Setter;

/**
 * User requestDto
 */
@Getter
@Setter
public class UserRequestDto implements RequestDto {

    private String name;
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
        return isNonEmpty(name) && isNonEmpty(mobileNo) && isNonEmpty(password) && userType != null;
    }
}
