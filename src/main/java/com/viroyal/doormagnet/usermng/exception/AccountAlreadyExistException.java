package com.viroyal.doormagnet.usermng.exception;

/*
 * Author: Created by qinyl.
 * Date:   2017/2/9.
 * Comments:
 */
public class AccountAlreadyExistException extends RuntimeException {
    public AccountAlreadyExistException(String message) {
        super(message);
    }
}
