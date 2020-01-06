package com.viroyal.doormagnet.usermng.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/*
 * Author: Created by qinyl.
 * Date:   2017/2/7.
 * Comments:
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "AC_USER")
public class UserEntity implements Serializable {
    @JsonProperty("user_id")
    @Transient
    public String userId;

    @JsonIgnore
    @Id
    @GeneratedValue(generator = "shortUid")
    @GenericGenerator(name = "shortUid", strategy = "com.viroyal.doormagnet.usermng.util.ShortUUID")
    private Long id;

    @JsonProperty("token")
    @Column(name = "TOKEN")
    private String token;

    @JsonProperty("h5token")
    @Column(name = "H5TOKEN")
    private String h5token;

    @JsonProperty("user_name")
    @Column(name = "USER_NAME")
    private String username;

    @JsonIgnore
    @Column(name = "REAL_NAME")
    private String realname;

    @JsonProperty("phone")
    @Column(name = "PHONE")
    private String phone;

    @JsonProperty("avatar_url")
    @Column(name = "AVATAR_URL")
    private String avatarUrl;

    @Column(name = "PROFESSION")
    private String profession;

    @Column(name = "OVERVIEW")
    private String overview;

    @JsonProperty("im_id")
    @Column(name = "IM_ID")
    private String imId;

    @JsonProperty("im_token")
    @Column(name = "IM_TOKEN")
    private String imToken;

    @JsonProperty("im_tag")
    @Column(name = "IM_TAG")
    private String imTag;

    @JsonIgnore
    @Column(name = "CREATED_AT")
    private Date createdAt;

    @JsonIgnore
    @Column(name = "UPDATED_AT")
    private Date updatedAt;

    @JsonIgnore
    @Column(name = "DELETED_AT")
    private Date deletedAt;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private Set<AccountEntity> accountEntities = new HashSet<>();

    public UserEntity() {

    }

    public UserEntity(String username,
                      String phone,
                      String avatarUrl,
                      String imId,
                      String imToken,
                      String imTag,
                      AccountEntity entity
    ) {
        this.username = username;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
        this.imId = imId;
        this.imToken = imToken;
        this.imTag = imTag;
        this.accountEntities.add(entity);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getH5token() {
        return h5token;
    }

    public void setH5token(String h5token) {
        this.h5token = h5token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getImId() {
        return imId;
    }

    public void setImId(String imId) {
        this.imId = imId;
    }

    public String getImToken() {
        return imToken;
    }

    public void setImToken(String imToken) {
        this.imToken = imToken;
    }

    public String getImTag() {
        return imTag;
    }

    public void setImTag(String imTag) {
        this.imTag = imTag;
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

    public Set<AccountEntity> getAccountEntities() {
        return accountEntities;
    }

    public void setAccountEntities(Set<AccountEntity> accountEntities) {
        this.accountEntities = accountEntities;
    }
}
