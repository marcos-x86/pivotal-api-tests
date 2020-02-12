package com.pivotal.api;

/**
 * Custom Runtime Exception class.
 */
public class CustomRuntimeException extends RuntimeException {

    /**
     * Constructor for Exception class.
     * @param message Custom message for the error.
     * @param cause The cause for the error was thrown.
     */
    public CustomRuntimeException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
