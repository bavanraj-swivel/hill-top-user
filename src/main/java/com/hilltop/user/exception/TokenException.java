package com.hilltop.user.exception;

/**
 * Token exception
 */
public class TokenException extends HillTopUserApplicationException {

    /**
     * Token exception with error message and throwable error.
     *
     * @param errorMessage error message
     * @param error        error
     */
    public TokenException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
