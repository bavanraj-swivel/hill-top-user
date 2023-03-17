package com.hilltop.user.domain.request;

import lombok.Getter;
import lombok.Setter;

/**
 * User requestDto
 */
@Getter
@Setter
public class UserRequestDto extends LoginRequestDto {

    private String name;

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
