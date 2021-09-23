package com.xiaojumao.exception;

/**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-07-02 20:25
 * @Modified By:
 */
public class DuplicateCPhoneException extends Exception{
    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public DuplicateCPhoneException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public DuplicateCPhoneException(String message) {
        super(message);
    }
}
