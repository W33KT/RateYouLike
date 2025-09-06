package com.hmdp.aop;

import cn.hutool.core.bean.BeanUtil;
import com.hmdp.constants.RedisConstants;
import com.hmdp.constants.ResponseCode;
import com.hmdp.dto.UserDTO;
import com.hmdp.utils.UserHolder;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author tankaiwen
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. get token
        String token = request.getHeader("authorization");
        if (StringUtils.isBlank( token)) {
            response.setStatus(ResponseCode.NOT_LOGIN);
            return false;
        }

        // 2. check token
        String key = RedisConstants.LOGIN_USER_KEY + token;
        Map<Object, Object> userPropertyMap = stringRedisTemplate.opsForHash().entries(key);
        if (MapUtils.isEmpty(userPropertyMap)) {
            response.setStatus(ResponseCode.NOT_LOGIN);
            return false;
        }

        // 3. save user data in thread local
        UserDTO userDTO = BeanUtil.fillBeanWithMap(userPropertyMap, new UserDTO.UserDTO4RedisString(), false).convertToDTO();
        UserHolder.saveUser(userDTO);

        // 4. refresh token in redis
        stringRedisTemplate.expire(key, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
