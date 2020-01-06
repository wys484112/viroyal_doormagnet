package com.viroyal.doormagnet.usermng.pojo;

/**
 * 从融云返回的结果
 *
 * @author LiGang
 *
 */
public class RongResponse {
    // code所有消息通用
    public int code;
    // 这两个是注册用户的返回
    public String token;
    public String userId;

    @Override
    public String toString() {
        return "[code=" + code + ",userId=" + userId + ",token=" + token + "]";
    }
}
