package com.hmdp.interceptor;

import com.hmdp.constants.ResponseCode;
import com.hmdp.utils.UserHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author tankaiwen
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (Objects.isNull(UserHolder.getUser())) {
            response.setStatus(ResponseCode.NOT_LOGIN);
            return false;
        }

        return true;
    }
}
