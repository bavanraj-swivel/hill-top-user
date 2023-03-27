package com.hilltop.user.domain.request;

import com.hilltop.user.enumeration.UserType;
import lombok.Getter;
import lombok.Setter;

/**
 * User requestDto
 */
@Getter
@Setter
public class UserRequestDto extends LoginRequestDto {

    private String name;
    private UserType userType;

    /**
     * Used to validate required fields.
     *
     * @return true/false
     */
    @Override
    public boolean isRequiredFieldsAvailable() {
        return isNonEmpty(name) && super.isRequiredFieldsAvailable();
    }
}
