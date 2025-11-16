package com.hmdp.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.hmdp.constants.RedisConstants;
import com.hmdp.constants.SystemConstants;
import com.hmdp.dao.UserDAO;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import com.hmdp.utils.*;
import com.hmdp.vo.request.LoginFormReqVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author tankaiwen
 */
@Service
@Slf4j
public class UserService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private UserDAO userDAO;
    @Autowired
    private RedisService redisService;

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

    public String login(LoginFormReqVO loginForm, HttpSession session) {
        // 1. validate params, phone number format and verification code,
        //    judge whether verify code equals to the one in session
        validateLoginParam(loginForm, session);

        // 2. query user by phone number
        User user = userDAO.query().eq("phone", loginForm.getPhone()).one();

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
        userDAO.save(user);
        return user;
    }

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

    public void sign() {
        Pair<String, Integer> keyDayPair = getSignInfo();
        int day = keyDayPair.getRight();
        String key = keyDayPair.getLeft();

        redisService.setBitMap(key, day - 1, true);
    }

    private Pair<String, Integer> getSignInfo() {
        UserDTO userDTO = UserHolder.getUser();
        ValidateUtils.notNull(userDTO, "Please log in~");
        Long userId = userDTO.getId();

        Pair<String, LocalDateTime> nowPair = DateUtils.getNowByFormat(DateUtils.YYYY_MM);
        String key = RedisConstants.USER_SIGN_KEY + userId + RedisConstants.SPLIT + nowPair.getLeft();

        int day = nowPair.getRight().getDayOfMonth();

        return Pair.of(key, day);
    }

    public Integer signCount() {
        Pair<String, Integer> keyDayPair = getSignInfo();
        int day = keyDayPair.getRight();
        String key = keyDayPair.getLeft();

        List<Long> bitMap = redisService.getSubBitMap(key, 0, day);
        if (CollectionUtils.isEmpty(bitMap)) {
            return 0;
        }
        Long num = bitMap.get(0);
        int res = 0;
        while ((num & 1) != 0) {
            res ++;
            num = num >> 1;
        }
        return res;
    }
}
