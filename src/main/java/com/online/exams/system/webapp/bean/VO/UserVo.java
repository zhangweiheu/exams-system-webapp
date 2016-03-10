package com.online.exams.system.webapp.bean.VO;

import com.online.exams.system.webapp.bean.BaseObject;

import java.util.Date;

/**
 * Created by zhang on 2016/3/8.
 */
public class UserVo extends BaseObject {
    /** id */
    private Integer id;

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    /** 身份标识，普通用户：0|管理员：1 */
    private Boolean isAdmin;

    /** 是否删除 */
    private Boolean isDelete;

    /** 头像 */
    private String avatar;

    /** 简介 */
    private String intro;

    /** 邮箱 */
    private String email;

    /** 手机号 */
    private String phone;

    /** 微信号 */
    private String wechat;

    /** 总得分 */
    private Integer totalScore;

    /** 总题量 */
    private Integer totalDone;

    /** 平均得分 */
    private Integer averageScore;

    /** 创建时间 */
    private Date createAt;

    /** 更新时间 */
    private Date updateAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getTotalDone() {
        return totalDone;
    }

    public void setTotalDone(Integer totalDone) {
        this.totalDone = totalDone;
    }

    public Integer getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Integer averageScore) {
        this.averageScore = averageScore;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}
