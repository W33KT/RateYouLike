package com.hmdp.facade;

import com.hmdp.dto.Result;
import com.hmdp.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
}
