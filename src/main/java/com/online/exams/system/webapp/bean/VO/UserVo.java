package com.online.exams.system.webapp.bean.VO;

import com.online.exams.system.core.mybatis.enums.UserStatusEnum;
import com.online.exams.system.core.mybatis.enums.UserTypeEnum;
import com.online.exams.system.webapp.bean.BaseObject;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by zhang on 2016/3/8.
 */
public class UserVo extends BaseObject {
    /** id */
    private Integer id;

    /** 用户名 */
    @NotNull
    @Length(min = 5,max = 20,message = "用户名长度不合法")
    private String username;

    /** 密码 */
    @NotNull
    @Length(min = 5,max = 20,message = "密码长度不合法")
    private String password;

    /** 密码 */
    @Length(min = 5,max = 20,message = "密码长度不合法")
    private String password2;

    /** 头像 */
    private String avatar;

    /**
     * 标签
     */
    private String tagList;

    /** 简介 */
    private String intro;

    /** 邮箱 */
    @Email
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

    /** 是否保存密码 */
    private String isSavePassword;

    @NotNull
    private String geetest_challenge;

    @NotNull
    private String geetest_seccode;

    @NotNull
    private String geetest_validate;

    /** 状态:0未审核，1：审核不通过，2审核通过，3已删除 ， 4黑名单 */
    private UserStatusEnum status;

    /** 用户类型：0普通用户 | 1管理员 | 2系统管理员 */
    private UserTypeEnum type;

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

    public String getTagList() {
        return tagList;
    }

    public void setTagList(String tagList) {
        this.tagList = tagList;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getIsSavePassword() {
        return isSavePassword;
    }

    public void setIsSavePassword(String isSavePassword) {
        this.isSavePassword = isSavePassword;
    }

    public String getGeetest_challenge() {
        return geetest_challenge;
    }

    public void setGeetest_challenge(String geetest_challenge) {
        this.geetest_challenge = geetest_challenge;
    }

    public String getGeetest_seccode() {
        return geetest_seccode;
    }

    public void setGeetest_seccode(String geetest_seccode) {
        this.geetest_seccode = geetest_seccode;
    }

    public String getGeetest_validate() {
        return geetest_validate;
    }

    public void setGeetest_validate(String geetest_validate) {
        this.geetest_validate = geetest_validate;
    }

    public UserStatusEnum getStatus() {
        return status;
    }

    public void setStatus(UserStatusEnum status) {
        this.status = status;
    }

    public UserTypeEnum getType() {
        return type;
    }

    public void setType(UserTypeEnum type) {
        this.type = type;
    }
}
