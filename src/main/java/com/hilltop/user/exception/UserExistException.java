package com.hilltop.user.exception;

/**
 * User exist exception
 */
public class UserExistException extends HillTopUserApplicationException {
    /**
     * Hill Top application exception with error message and throwable error.
     *
     * @param errorMessage error message
     */
    public UserExistException(String errorMessage) {
        super(errorMessage);
    }
}
