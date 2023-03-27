package com.hilltop.user.enumeration;

import lombok.Getter;

/**
 * Success messages.
 */
@Getter
public enum SuccessMessage {

    SUCCESSFULLY_ADDED("Successfully added."),
    SUCCESSFULLY_LOGGED_IN("Successfully logged in."),
    VALID_TOKEN("Valid token");

    private final String message;

    SuccessMessage(String message) {
        this.message = message;
    }
}
