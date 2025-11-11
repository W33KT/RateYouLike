package com.hmdp.facade;

import com.hmdp.dto.Result;
import com.hmdp.service.FollowService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tankaiwen
 */
@Service
public class FollowFacade {
    @Resource
    private FollowService followService;
    public Result follow(Long toFollowUserId, Boolean isFollow) {
        followService.follow(toFollowUserId, isFollow);
        return Result.ok();
    }

    public Result isFollow(Long toFollowUserId) {
        return Result.ok(followService.isFollow(toFollowUserId));
    }
}
