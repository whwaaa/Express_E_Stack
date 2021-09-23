package com.xiaojumao.exception;

/**
 * @Author: whw
 * @Description:
 * @Date Created in 2021-07-02 18:12
 * @Modified By:
 */
public class DuplicateUserNameException extends Exception{
    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public DuplicateUserNameException() {
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public DuplicateUserNameException(String message) {
        super(message);
    }
}
