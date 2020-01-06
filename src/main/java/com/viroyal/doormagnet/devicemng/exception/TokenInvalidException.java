package com.viroyal.doormagnet.devicemng.exception;

public class TokenInvalidException extends Exception {
    public TokenInvalidException(String token) {
        super("token invalid: " + token);
    }
}
