package com.hmdp.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hmdp.constants.RedisConstants;
import com.hmdp.dao.FollowDAO;
import com.hmdp.dao.UserDAO;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.Follow;
import com.hmdp.entity.User;
import com.hmdp.utils.RedisService;
import com.hmdp.utils.UserHolder;
import com.hmdp.utils.ValidateUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author tankaiwen
 */
@Service
public class FollowService {
    @Resource
    private FollowDAO followDAO;
    @Resource
    private RedisService redisService;
    @Resource
    private UserDAO userDAO;

    public void follow(Long toFollowUserId, boolean isFollow) {
        ValidateUtils.notNull(toFollowUserId, "the id of the user to follow is null");
        UserDTO user = UserHolder.getUser();
        ValidateUtils.notNull(user, "Please log in!");
        Long userId = user.getId();
        if (toFollowUserId.equals(userId)) {
            return;
        }
        String userFollowKey = RedisConstants.USER_FOLLOWING_KEY + userId;

        if (isFollow) {
            Follow follow = new Follow();
            follow.setUserId(userId);
            follow.setFollowUserId(toFollowUserId);
            boolean res = followDAO.save(follow);
            if (!res) {
                return;
            }
            redisService.addToSet(userFollowKey, toFollowUserId.toString());
        }

        QueryWrapper<Follow> queryWrapper = new QueryWrapper<Follow>()
                .eq("user_id", userId)
                .eq("follow_user_id", toFollowUserId);
        boolean res = followDAO.remove(queryWrapper);
        if (!res) {
            return;
        }
        redisService.removeFromSet(userFollowKey, toFollowUserId.toString());
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

    public List<UserDTO> commonFollowing(Long id) {
        UserDTO user = UserHolder.getUser();
        ValidateUtils.notNull(user, "Please log in!");
        Long userId = user.getId();
        String thisUserKey = RedisConstants.USER_FOLLOWING_KEY + userId;
        String otherUserKey = RedisConstants.USER_FOLLOWING_KEY + id;

        Set<String> intersection = redisService.getIntersection(thisUserKey, otherUserKey);
        List<Long> ids = CollectionUtils.emptyIfNull(intersection).stream().map(Long::valueOf).toList();
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }

        List<User> users = userDAO.listByIds(ids);
        return users.stream().map(UserDTO::convertFromUser).toList();
    }
}
