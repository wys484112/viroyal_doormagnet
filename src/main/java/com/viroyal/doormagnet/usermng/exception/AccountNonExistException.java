package com.viroyal.doormagnet.usermng.exception;

/*
 * Author: Created by qinyl.
 * Date:   2017/2/9.
 * Comments:
 */
public class AccountNonExistException extends RuntimeException {
    public AccountNonExistException(String message) {
        super(message);
    }
}
