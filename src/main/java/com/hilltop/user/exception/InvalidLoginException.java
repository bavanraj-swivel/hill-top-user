package com.hilltop.user.exception;

/**
 * Invalid login exception
 */
public class InvalidLoginException extends HillTopUserApplicationException {
    /**
     * Invalid login exception with error message.
     *
     * @param errorMessage error message
     */
    public InvalidLoginException(String errorMessage) {
        super(errorMessage);
    }
}
