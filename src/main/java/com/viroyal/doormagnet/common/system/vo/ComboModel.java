package com.viroyal.doormagnet.common.system.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import java.io.Serializable;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ComboModel implements Serializable {
    private String id;
    private String title;
    /**文档管理 表单table默认选中*/
    private boolean checked;
    /**文档管理 表单table 用户账号*/
    private String username;
    /**文档管理 表单table 用户邮箱*/
    private String email;
    /**文档管理 表单table 角色编码*/
    private String roleCode;

    public ComboModel(){

    };

    public ComboModel(String id,String title,boolean checked,String username){
        this.id = id;
        this.title = title;
        this.checked = false;
        this.username = username;
    };
}
