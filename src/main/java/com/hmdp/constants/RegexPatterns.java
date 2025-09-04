package com.hmdp.constants;

/**
 * @author tankaiwen
 */
public abstract class RegexPatterns {
    /**
     * Regex pattern for Dutch mobile numbers.
     * Matches both domestic format (06xxxxxxxx) and international format (+316xxxxxxxx or 00316xxxxxxxx).
     */
    public static final String NL_PHONE_REGEX = "^(06\\d{8}|(\\+31|0031)6\\d{8})$";;
    /**
     * 邮箱正则
     */
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    /**
     * 密码正则。4~32位的字母、数字、下划线
     */
    public static final String PASSWORD_REGEX = "^\\w{4,32}$";
    /**
     * 验证码正则, 6位数字或字母
     */
    public static final String VERIFY_CODE_REGEX = "^[a-zA-Z\\d]{6}$";

}
