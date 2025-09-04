package com.hmdp.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.constants.SystemConstants;
import com.hmdp.dto.LoginFormDTO;
import com.hmdp.entity.User;
import com.hmdp.mapper.UserMapper;
import com.hmdp.service.IUserService;
import com.hmdp.utils.RegexUtils;
import com.hmdp.utils.ValidateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Objects;


/**
 * @author tankaiwen
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Override
    public void sendCode(String phone, HttpSession session) {
        // 1. validate param and phone number format
        ValidateUtils.notNull(session, "Session is null!");
        ValidateUtils.isTrue(!RegexUtils.isPhoneInvalid( phone), "Invalid phone number!");

        // 2. generate verify code
        String verifyCode = RandomUtil.randomString(6);

        // 3. save verify code to session
        session.setAttribute(phone + SystemConstants.SESSION_VERIFY_CODE_KEY_SUFFIX, verifyCode);

        // 4. send verify code
        // This is a mock implementation for sending verify codes.
        // In a real scenario, the code would be sent via an external service.
        log.debug("Sent verify code: {} to phone: {}", verifyCode, phone);
    }

    @Override
    public void login(LoginFormDTO loginForm, HttpSession session) {
        // 1. validate params, phone number format and verification code,
        //    judge whether verify code equals to the one in session
        validateLoginParam(loginForm, session);

        // 2. query user by phone number
        User user = query().eq("phone", loginForm.getPhone()).one();

        // 3. judge whether user exists, create user if not exists
        if (Objects.isNull(user)) {
            user = createNewUser(loginForm.getPhone());
        }

        // 4. save user to session
        session.setAttribute(SystemConstants.SESSION_USER_KEY, user);
    }

    private void validateLoginParam(LoginFormDTO loginForm, HttpSession session) {
        ValidateUtils.notNull(session, "Session is null!");
        ValidateUtils.notNull(loginForm, "Login information is null!");
        ValidateUtils.isTrue(!RegexUtils.isPhoneInvalid( loginForm.getPhone()), "Invalid phone number!");
        checkVerifyCode(loginForm.getPhone(), loginForm.getCode(), session);
    }

    private void checkVerifyCode(String phone, String verifyCode, HttpSession session) {
        Object codeInSession = session.getAttribute(phone + SystemConstants.SESSION_VERIFY_CODE_KEY_SUFFIX);
        ValidateUtils.isTrue(Objects.nonNull(codeInSession) && codeInSession.toString().equals(verifyCode), "Invalid verify code!");
    }

    private User createNewUser(String phone) {
        User user = new User();
        user.setPhone(phone);
        user.setNickName(SystemConstants.USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
        save(user);
        return user;
    }
}

