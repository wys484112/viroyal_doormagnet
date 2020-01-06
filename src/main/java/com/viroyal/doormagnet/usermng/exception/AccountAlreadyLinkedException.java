package com.viroyal.doormagnet.usermng.exception;

/*
 * Author: Created by qinyl.
 * Date:   2017/2/9.
 * Comments:
 */
public class AccountAlreadyLinkedException extends RuntimeException {
    public AccountAlreadyLinkedException(String message) {
        super(message);
    }
}
