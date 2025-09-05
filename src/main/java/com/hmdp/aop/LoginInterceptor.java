package com.hmdp.aop;

import com.hmdp.constants.ResponseCode;
import com.hmdp.constants.SystemConstants;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import com.hmdp.utils.UserHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * @author tankaiwen
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        Object user = session.getAttribute(SystemConstants.SESSION_USER_KEY);
        if (Objects.isNull( user)) {
            response.setStatus(ResponseCode.NOT_LOGIN);
            return false;
        }

        UserHolder.saveUser(UserDTO.convertFromUser((User) user));
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
