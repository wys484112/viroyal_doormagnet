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
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	};
}
