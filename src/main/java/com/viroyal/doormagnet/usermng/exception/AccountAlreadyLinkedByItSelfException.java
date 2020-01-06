package com.viroyal.doormagnet.usermng.exception;

/*
 * Author: Created by qinyl.
 * Date:   2017/2/9.
 * Comments:
 */
public class AccountAlreadyLinkedByItSelfException extends RuntimeException {
    public AccountAlreadyLinkedByItSelfException(String message) {
        super(message);
    }
}
