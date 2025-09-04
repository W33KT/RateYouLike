package com.hmdp.utils;

import cn.hutool.core.util.StrUtil;
import com.hmdp.constants.RegexPatterns;


/**
 * @author tankaiwen
 */
public class RegexUtils {
    /**
     * validate phone number
     * @param phone phone number to validate
     * @return true: invalid, false: valid
     */
    public static boolean isPhoneInvalid(String phone){
        return mismatch(phone, RegexPatterns.NL_PHONE_REGEX);
    }
    /**
     * 是否是无效邮箱格式
     * @param email 要校验的邮箱
     * @return true:符合，false：不符合
     */
    public static boolean isEmailInvalid(String email){
        return mismatch(email, RegexPatterns.EMAIL_REGEX);
    }

    /**
     * 是否是无效验证码格式
     * @param code 要校验的验证码
     * @return true:符合，false：不符合
     */
    public static boolean isCodeInvalid(String code){
        return mismatch(code, RegexPatterns.VERIFY_CODE_REGEX);
    }

    /**
     * verify regex
     * @param str
     * @param regex
     * @return
     */
    private static boolean mismatch(String str, String regex){
        if (StrUtil.isBlank(str)) {
            return true;
        }
        return !str.matches(regex);
    }
}
