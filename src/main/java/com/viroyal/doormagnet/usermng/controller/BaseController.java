package com.viroyal.doormagnet.usermng.controller;

import com.viroyal.doormagnet.usermng.pojo.BaseResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * Author: Created by qinyl.
 * Date:   2017/1/18.
 * Comments:
 */
@SuppressWarnings(value = "unused")
@RestController
public class BaseController {
    @RequestMapping(value = "/")
    public BaseResponse root() {
        return BaseResponse.SUCCESS;
    }
}
