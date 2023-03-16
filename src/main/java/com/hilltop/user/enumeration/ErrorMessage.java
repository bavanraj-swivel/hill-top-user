package com.hilltop.user.enumeration;

import lombok.Getter;

/**
 * Error messages.
 */
@Getter
public enum ErrorMessage {

    INTERNAL_SERVER_ERROR("Something went wrong."),
    MISSING_REQUIRED_FIELDS("Required fields are missing."),
    INVALID_LOGIN("Invalid login.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
