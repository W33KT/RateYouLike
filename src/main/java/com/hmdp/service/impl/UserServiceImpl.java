package com.hmdp.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.entity.User;
import com.hmdp.mapper.UserMapper;
import com.hmdp.service.IUserService;
import com.hmdp.utils.RegexUtils;
import com.hmdp.utils.ValidateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;


/**
 * @author tankaiwen
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Override
    public void sendCode(String phone, HttpSession session) {
        // 1. validate phone number
        ValidateUtils.isTrue(!RegexUtils.isPhoneInvalid( phone), "Invalid phone number!");
        // 2. generate verification code
        String verificationCode = RandomUtil.randomString(6);

        // 3. save verification code to session
        session.setAttribute("verificationCode", verificationCode);

        // 4. send verification code
        // This is a mock implementation for sending verification codes.
        // In a real scenario, the code would be sent via an external service.
        log.debug("Sent verification code: {} to phone: {}", verificationCode, phone);
    }
}
