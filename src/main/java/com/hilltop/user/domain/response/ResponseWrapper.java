package com.hilltop.user.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Response wrapper
 */
@Getter
@AllArgsConstructor
public class ResponseWrapper {

    private String message;
    private ResponseDto data;

    public ResponseWrapper(String message) {
        this.message = message;
    }
}
