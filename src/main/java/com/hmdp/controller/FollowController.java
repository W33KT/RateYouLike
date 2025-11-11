package com.hmdp.controller;


import com.hmdp.dto.Result;
import com.hmdp.facade.FollowFacade;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author tankaiwen
 */
@RestController
@RequestMapping("/follow")
public class FollowController {
    @Resource
    private FollowFacade followFacade;

    @PutMapping("/{id}/{isFollow}")
    public Result follow(@PathVariable("id") Long toFollowUserId, @PathVariable("isFollow") Boolean isFollow) {
        return followFacade.follow(toFollowUserId, isFollow);
    }

    @GetMapping("/or/not/{id}")
    public Result isFollow(@PathVariable("id") Long followUserId) {
        return followFacade.isFollow(followUserId);
    }
}
