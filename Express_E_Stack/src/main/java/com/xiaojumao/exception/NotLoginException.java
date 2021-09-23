package com.xiaojumao.exception;

/**
 * Express_E_Stack_SSM
 *
 * @author wuhanwei
 * @version 1.0
 * @date 2021/9/22
 */
public class NotLoginException extends Exception{
    public NotLoginException() {
        super();
    }

    public NotLoginException(String message) {
        super(message);
    }
}
