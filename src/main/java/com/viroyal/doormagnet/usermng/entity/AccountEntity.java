package com.viroyal.doormagnet.usermng.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/*
 * Author: Created by qinyl.
 * Date:   2017/2/7.
 * Comments:
 */
@Entity
@Table(name = "AC_ACCOUNT")
public class AccountEntity implements Serializable {
    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    public UserEntity userEntity;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JsonIgnore
    @Column(name = "USER_ID")
    private Long userId;
    @JsonProperty("acc_name")
    @Column(name = "ACC_NAME")
    private String accName;
    @JsonIgnore
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "TYPE")
    private Integer type;
    @JsonIgnore
    @Column(name = "LOGIN_AT")
    private Date loginAt;
    @JsonIgnore
    @Column(name = "INFO")
    private String info;
    @JsonIgnore
    @Column(name = "CREATED_AT")
    private Date createdAt;
    @JsonIgnore
    @Column(name = "UPDATED_AT")
    private Date updatedAt;
    @JsonIgnore
    @Column(name = "DELETED_AT")
    private Date deletedAt;

    public AccountEntity() {

    }

    public AccountEntity(String accName,
                         String password,
                         Integer type,
                         Date createdAt,
                         Date updatedAt,
                         UserEntity userEntity
    ) {
        this.accName = accName;
        this.password = password;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userEntity = userEntity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getLoginAt() {
        return loginAt;
    }

    public void setLoginAt(Date loginAt) {
        this.loginAt = loginAt;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
