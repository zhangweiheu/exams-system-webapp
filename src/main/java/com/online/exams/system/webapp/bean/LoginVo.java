package com.online.exams.system.webapp.bean;

import com.sun.org.glassfish.gmbal.NameValue;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by zhang on 2016/2/21.
 */
public class LoginVo {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String geetest_challenge;
    @NotNull
    private String geetest_seccode;
    @NotNull
    private String geetest_validate;

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
}
