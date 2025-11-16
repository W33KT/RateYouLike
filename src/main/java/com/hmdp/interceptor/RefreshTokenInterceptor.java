package com.hmdp.interceptor;

import cn.hutool.core.bean.BeanUtil;
import com.hmdp.constants.RedisConstants;
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

@Component
public class RefreshTokenInterceptor  implements HandlerInterceptor {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. get token
        String token = request.getHeader("authorization");
        if (StringUtils.isBlank( token)) {
            return true;
        }

        // 2. check token
        String key = RedisConstants.LOGIN_USER_KEY + token;
        Map<Object, Object> userPropertyMap = stringRedisTemplate.opsForHash().entries(key);
        if (MapUtils.isEmpty(userPropertyMap)) {
            return true;
        }

        // 3. save user data in thread local
        UserDTO userDTO = BeanUtil.fillBeanWithMap(userPropertyMap, new UserDTO.UserDTO4RedisString(), false).convertToDTO();
        UserHolder.saveUser(userDTO);

        // 4. refresh token in redis
        stringRedisTemplate.expire(key, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
