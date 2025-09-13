package com.hmdp.controller;


import com.hmdp.vo.request.LoginFormReqVO;
import com.hmdp.dto.Result;
import com.hmdp.entity.UserInfo;
import com.hmdp.facade.UserFacade;
import com.hmdp.dao.UserInfoDAO;
import com.hmdp.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserFacade userFacade;
    @Resource
    private UserInfoDAO userInfoDAO;

    /**
     * send phone number verification code
     */
    @PostMapping("code")
    public Result sendCode(@RequestParam("phone") String phone, HttpSession session) {
        return userFacade.sendCode(phone, session);
    }

    /**
     * user login function
     * @param loginForm containing phone number, password, verification code
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginFormReqVO loginForm, HttpSession session){
        return userFacade.login(loginForm, session);
    }

    /**
     * logout function
     * @return 无
     */
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request){
        return userFacade.logout(request);
    }

    @GetMapping("/me")
    public Result me(){
        return Result.ok(UserHolder.getUser());
    }

    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Long userId){
        // 查询详情
        UserInfo info = userInfoDAO.getById(userId);
        if (info == null) {
            // 没有详情，应该是第一次查看详情
            return Result.ok();
        }
        info.setCreateTime(null);
        info.setUpdateTime(null);
        // 返回
        return Result.ok(info);
    }
}
