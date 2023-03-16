package com.hilltop.user.exception;

/**
 * HillTop user application exception
 */
public class HillTopUserApplicationException extends RuntimeException {

    /**
     * Hill Top application exception with error message and throwable error.
     *
     * @param errorMessage error message
     * @param error        error
     */
    public HillTopUserApplicationException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }

    /**
     * Hill Top application exception with error message.
     *
     * @param errorMessage error message
     */
    public HillTopUserApplicationException(String errorMessage) {
        super(errorMessage);
    }
}
