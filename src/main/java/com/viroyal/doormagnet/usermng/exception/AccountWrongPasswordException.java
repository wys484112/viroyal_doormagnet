package com.viroyal.doormagnet.usermng.exception;

/*
 * Author: Created by qinyl.
 * Date:   2017/2/10.
 * Comments:
 */
public class AccountWrongPasswordException extends RuntimeException {
    public AccountWrongPasswordException(String message) {
        super(message);
    }
}
