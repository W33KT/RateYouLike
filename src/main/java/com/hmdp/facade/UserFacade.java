package com.hmdp.facade;

import com.hmdp.vo.LoginFormReqVO;
import com.hmdp.dto.Result;
import com.hmdp.service.IUserService;
import com.hmdp.utils.ValidateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author tankaiwen
 */
@Service
public class UserFacade {
    @Resource
    private IUserService userService;

    public Result sendCode(String phone, HttpSession session) {
        userService.sendCode(phone, session);

        return Result.ok();
    }

    public Result login(LoginFormReqVO loginForm, HttpSession session) {
        String token = userService.login(loginForm, session);

        return Result.ok(token);
    }

    public Result logout(HttpServletRequest request) {
        ValidateUtils.notNull(request, "Request is null!");

        userService.logout(request.getHeader("authorization"));

        return Result.ok();
    }
}
