package com.viroyal.doormagnet.modules.shiro.vo;

import java.util.HashSet;
import java.util.Set;

public class UserBean {
    private String username;
    private String password;
    private Set<String> roles = new HashSet<>();    //用户所有角色值，用于shiro做角色权限的判断
    private Set<String> perms = new HashSet<>();    //用户所有权限值，用于shiro做资源权限的判断
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Set<String> getRoles() {
		return roles;
	}
	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
	public Set<String> getPerms() {
		return perms;
	}
	public void setPerms(Set<String> perms) {
		this.perms = perms;
	}

}