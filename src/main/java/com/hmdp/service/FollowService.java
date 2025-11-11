package com.hmdp.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hmdp.dao.FollowDAO;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.Follow;
import com.hmdp.utils.UserHolder;
import com.hmdp.utils.ValidateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tankaiwen
 */
@Service
public class FollowService {
    @Resource
    private FollowDAO followDAO;

    public void follow(Long toFollowUserId, boolean isFollow) {
        ValidateUtils.notNull(toFollowUserId, "the id of the user to follow is null");
        UserDTO user = UserHolder.getUser();
        ValidateUtils.notNull(user, "Please log in!");
        Long userId = user.getId();
        if (toFollowUserId.equals(userId)) {
            return;
        }

        if (isFollow) {
            Follow follow = new Follow();
            follow.setUserId(userId);
            follow.setFollowUserId(toFollowUserId);
            followDAO.save( follow);
            return;
        }

        QueryWrapper<Follow> queryWrapper = new QueryWrapper<Follow>()
                .eq("user_id", userId)
                .eq("follow_user_id", toFollowUserId);
        followDAO.remove(queryWrapper);
    }

    public boolean isFollow(Long toFollowUserId) {
        ValidateUtils.notNull(toFollowUserId, "the id of user to follow is null");
        UserDTO user = UserHolder.getUser();
        ValidateUtils.notNull(user, "Please log in!");
        Long userId = user.getId();
        if (toFollowUserId.equals(userId)) {
            return false;
        }

        QueryWrapper<Follow> queryWrapper = new QueryWrapper<Follow>()
                .eq("user_id", userId)
                .eq("follow_user_id", toFollowUserId);

        return followDAO.count(queryWrapper) > 0;
    }
}
