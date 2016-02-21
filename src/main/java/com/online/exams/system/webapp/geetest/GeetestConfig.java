package com.online.exams.system.webapp.geetest;

/**
 * Created by zhang on 2016/2/20.
 */
public class GeetestConfig {
    private static final String captchaId = "cd11b281ee028675ca276680e6d40367";
    private static final String privateKey = "da579a86145e2d1fe565ef7dadb80586";

    public static String getCaptchaId() {
        return captchaId;
    }
    public static String getPrivateKey() {
        return privateKey;
    }
}
