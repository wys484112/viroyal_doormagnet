package com.viroyal.doormagnet.usermng.controller;

import com.viroyal.doormagnet.usermng.pojo.*;
import com.viroyal.doormagnet.usermng.service.AccountService;
import com.viroyal.doormagnet.usermng.util.RandomUtil;
import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 * Author: Created by qinyl.
 * Date:   2017/2/23.
 * Comments:
 */
@SuppressWarnings(value = "unused")
@RestController
public class AccountController {
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    @GetMapping(value = "/account/v1/getMessage")
    ResponseEntity<?> getVerificationMessage(@RequestParam("phone") String phone, @RequestParam("action") String action, @RequestParam("type") int type) {
        return ResponseEntity.ok().body(accountService.getVerificationMessage(new MessageBean(phone, action, type)));
    }

    @PostMapping(value = "/account/v1/register")
    ResponseEntity<?> register(@RequestBody RegisterParam param) {
        return ResponseEntity.ok().body(accountService.register(param));
    }

    @PostMapping(value = "/account/v1/login")
    ResponseEntity<?> login(@RequestBody LoginParam param) {
        return ResponseEntity.ok().body(accountService.login(param));
    }

    @PostMapping(value = "/account/v1/forgetpwd")
    ResponseEntity<?> forget(@RequestBody ForgetParam param) {
        return ResponseEntity.ok().body(accountService.forget(param));
    }

    @PostMapping(value = "/account/v1/link")
    ResponseEntity<?> link(@RequestBody LinkParam param, @RequestHeader(value = "token") String token, @RequestHeader(value = "user_id") Long userId) {
        return ResponseEntity.ok().body(accountService.link(param, userId));
    }

    @DeleteMapping(value = "/account/v1/link")
    ResponseEntity<?> link(@RequestParam(value = "id") Long id, @RequestHeader(value = "user_id") Long userId) {
        return ResponseEntity.ok().body(accountService.link(id, userId));
    }

    @GetMapping(value = "/account/v1/link")
    ResponseEntity<?> link(@RequestHeader(value = "user_id") Long userId) {
        return ResponseEntity.ok().body(accountService.link(userId));
    }

    @PutMapping(value = "/account/v1/passwd")
    ResponseEntity<?> password(@RequestBody PasswordParam param, @RequestHeader(value = "token") String token) {
        return ResponseEntity.ok().body(accountService.password(param, token));
    }

    @PutMapping(value = "/account/v1/profile")
    ResponseEntity<?> profile(@RequestBody ProfileParam param, @RequestHeader(value = "token") String token) {
        return ResponseEntity.ok().body(accountService.profile(param, token));
    }

    @GetMapping(value = "/account/v1/profile")
    ResponseEntity<?> profile(@RequestHeader(value = "token") String token) {
        return ResponseEntity.ok().body(accountService.profile(token));
    }

    @PutMapping(value = "/v1/account/phone")
    ResponseEntity<?> phone(@RequestBody PhoneParam param, @RequestHeader(value = "token") String token, @RequestHeader(value = "user_id") Long userId) {
        return ResponseEntity.ok().body(accountService.phone(param, token, userId));
    }

    @PostMapping(value = "/v1/account/logout")
    ResponseEntity<?> logout(@RequestHeader(value = "token", required = false) String token, @RequestHeader(value = "user_id", required = false) Long userId) {
        return ResponseEntity.ok().body(accountService.logout(token, userId));
    }

    @PostMapping(value = "/v1/account/token")
    ResponseEntity<?> token(@RequestHeader(value = "mdc") String mdc, @RequestBody BaseParam param) {
        logger.info("token check mdc: " + mdc);
        MDC.put(RandomUtil.MDC_KEY, mdc);
        return ResponseEntity.ok().body(accountService.token(param));
    }

    @PostMapping(value = "/v1/account/h5token")
    ResponseEntity<?> h5token(@RequestHeader(value = "mdc") String mdc, @RequestBody BaseParam param) {
        logger.info("h5token check mdc: " + mdc);
        MDC.put(RandomUtil.MDC_KEY, mdc);
        return ResponseEntity.ok().body(accountService.h5token(param));
    }

    @PostMapping(value = "/v1/account/detail")
    ResponseEntity<?> detail(@RequestHeader(value = "mdc") String mdc, @RequestBody BaseParam param) {
        logger.info("mdc: " + mdc);
        MDC.put(RandomUtil.MDC_KEY, mdc);
        return ResponseEntity.ok().body(accountService.detail(param));
    }

    @PostMapping(value = "/v1/account/jpush_id")
    ResponseEntity<?> pushId(@RequestHeader(value = "user_id", required = false) Long userId, @RequestHeader(value = "token", required = false) String token, @RequestBody PushParam param) {
        return ResponseEntity.ok().body(accountService.pushId(userId, token, param));
    }
}
