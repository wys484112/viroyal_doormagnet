package com.viroyal.doormagnet.usermng.exception;

/*
 * Author: Created by qinyl.
 * Date:   2017/2/16.
 * Comments:
 */
public class AccountOldNewPwdSameException extends RuntimeException {
    public AccountOldNewPwdSameException(String message) {
        super(message);
    }
}
