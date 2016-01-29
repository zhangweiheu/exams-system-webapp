package com.online.exams.system.webapp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Random;

public class TokenUtil {

    private static final Logger logger = LoggerFactory.getLogger(TokenUtil.class);
    private static final String SECURITY_KEY = "36krplus-smsmail-cms-7684-1193-3112";
    public static final String TOKEN_COOKIE_KEY = "_lid";
    public static final String TOKEN_PRIVILEGE_COOKIE_KEY = "_lpc";
    public static final String DEFAULT_MD5_SALT = "123456789";

    /*
     * 把数组每一字节换成16进制连成md5字符串
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer md5str = new StringBuffer();

        int digital;
        for (int i = 0; i < bytes.length; i++) {
            digital = bytes[i];
            if (digital < 0) {
                digital += 256;
            }
            if (digital < 16) {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }

        return md5str.toString();
    }

    public static String getMd5Str(String orignStr) {
        try {
            byte[] b1 = orignStr.getBytes("UTF-8");

            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] buff = md.digest(b1);

            String md5str = bytesToHex(buff);

            return md5str;
        } catch (Exception e) {
            logger.warn("digest to md5 fail, str {}", orignStr);
            return null;
        }
    }

    public static String generateToken(String userName) throws Exception {
        Random r = new Random(2015);
        int randInt = r.nextInt();

        long currentTime = System.currentTimeMillis();

        String s1 = Long.toString(currentTime) + Integer.toString(randInt) + userName + SECURITY_KEY;

        byte[] b1 = s1.getBytes("UTF-8");

        MessageDigest md = MessageDigest.getInstance("MD5");

        byte[] buff = md.digest(b1);

        String md5str = bytesToHex(buff);

        int hash = (md5str + userName + SECURITY_KEY).hashCode();
        if (hash < 0) {
            hash = hash * -1;
        }

        return md5str + "|" + userName + "|" + Integer.toString(hash);
    }

    public static boolean isValidToken(String token) throws Exception {
        String s[] = token.split("\\|");
        if (s.length != 3) {
            return false;
        }

        String md5str = s[0];
        String userName = s[1];

        int hash = (md5str + userName + SECURITY_KEY).hashCode();
        if (hash < 0) {
            hash = hash * -1;
        }

        String strHash = Integer.toString(hash);

        if (strHash.equals(s[2])) {
            return true;
        } else {
            return false;
        }
    }

}
