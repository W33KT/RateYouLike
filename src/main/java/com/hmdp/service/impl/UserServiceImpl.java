package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.constants.RedisConstants;
import com.hmdp.constants.SystemConstants;
import com.hmdp.vo.LoginFormReqVO;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import com.hmdp.mapper.UserMapper;
import com.hmdp.service.IUserService;
import com.hmdp.utils.RegexUtils;
import com.hmdp.utils.UserHolder;
import com.hmdp.utils.ValidateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * @author tankaiwen
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void sendCode(String phone, HttpSession session) {
        // 1. validate param and phone number format
        ValidateUtils.notNull(session, "Session is null!");
        ValidateUtils.isTrue(!RegexUtils.isPhoneInvalid( phone), "Invalid phone number!");

        // 2. generate verify code
        String verifyCode = RandomUtil.randomString(6);

        // 3. save verify code to redis
        stringRedisTemplate.opsForValue().set(RedisConstants.LOGIN_CODE_KEY + phone, verifyCode,
                RedisConstants.LOGIN_CODE_TTL, TimeUnit.MINUTES);

        // 4. send verify code
        // This is a mock implementation for sending verify codes.
        // In a real scenario, the code would be sent via an external service.
        log.debug("Sent verify code: {} to phone: {}", verifyCode, phone);
    }

    @Override
    public String login(LoginFormReqVO loginForm, HttpSession session) {
        // 1. validate params, phone number format and verification code,
        //    judge whether verify code equals to the one in session
        validateLoginParam(loginForm, session);

        // 2. query user by phone number
        User user = query().eq("phone", loginForm.getPhone()).one();

        // 3. judge whether user exists, create user if not exists
        if (Objects.isNull(user)) {
            user = createNewUser(loginForm.getPhone());
        }

        // 4. generate token
        String token = UUID.randomUUID().toString(true);

        // 5. convert user data to hashmap and save it in redis
        UserDTO userDTO = UserDTO.convertFromUser(user);
        stringRedisTemplate.opsForHash().putAll(
                RedisConstants.LOGIN_USER_KEY + token,
                BeanUtil.beanToMap(userDTO.convertToDTO4Redis()));
        stringRedisTemplate.expire(RedisConstants.LOGIN_USER_KEY + token, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);

        // 6. return token
        return token;
    }

    private void validateLoginParam(LoginFormReqVO loginForm, HttpSession session) {
        ValidateUtils.notNull(session, "Session is null!");
        ValidateUtils.notNull(loginForm, "Login information is null!");
        ValidateUtils.isTrue(!RegexUtils.isPhoneInvalid( loginForm.getPhone()), "Invalid phone number!");
        checkVerifyCode(loginForm.getPhone(), loginForm.getCode(), session);
    }

    private void checkVerifyCode(String phone, String verifyCode, HttpSession session) {
        String codeInSession = stringRedisTemplate.opsForValue().get(RedisConstants.LOGIN_CODE_KEY + phone);
        ValidateUtils.isTrue(StringUtils.equals(verifyCode, codeInSession), "Invalid verify code!");
    }

    private User createNewUser(String phone) {
        User user = new User();
        user.setPhone(phone);
        user.setNickName(SystemConstants.USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
        save(user);
        return user;
    }

    @Override
    public void logout(String token) {
        // 1. get user's Nick name
        String name = UserHolder.getUser().getNickName();
        if (StringUtils.isBlank( name)) {
            log.warn("Logout: User's nick name is blank");
        }

        // 2. get current time
        LocalDateTime now = LocalDateTime.now();

        // 3. delete user's token
        stringRedisTemplate.delete(RedisConstants.LOGIN_USER_KEY + token);

        // 4. delete user data from thread local
        UserHolder.removeUser();

        // 5. write log
        log.info("User {} logout at {}", name, now);
    }
}

