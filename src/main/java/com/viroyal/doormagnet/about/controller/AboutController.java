package com.viroyal.doormagnet.about.controller;

import com.viroyal.doormagnet.about.entity.FeedbackEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.viroyal.doormagnet.about.pojo.BaseResponse;
import com.viroyal.doormagnet.about.service.AboutService;

/**
 * 其他模块接口
 *
 * @author walker
 */
@RestController
@RequestMapping("/about/")
public class AboutController {
    private static final Logger logger = LoggerFactory.getLogger(AboutController.class);

    @Autowired
    private AboutService aboutService;

    @RequestMapping(value = "v1/version")
    public BaseResponse getVersion() {
        return aboutService.getLatestVersion();
    }

    @RequestMapping(value = "v1/ads")
    public BaseResponse getAdvDataList() {
        return aboutService.getAdvData();
    }

    @PostMapping(value = "v1/feedback")
    public BaseResponse feedback(@RequestBody FeedbackEntity feedback, @RequestHeader(value = "user_id", required = false) Long userId) {
        return aboutService.feedback(feedback, userId);
    }

}
