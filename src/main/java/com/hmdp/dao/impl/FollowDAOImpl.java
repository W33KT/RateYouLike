package com.hmdp.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.entity.Follow;
import com.hmdp.mapper.FollowMapper;
import com.hmdp.dao.FollowDAO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tankaiwen
 */
@Service
public class FollowDAOImpl extends ServiceImpl<FollowMapper, Follow> implements FollowDAO {
    @Override
    public List<Long> queryFansIds(Long userId) {
        return query().eq("follow_user_id", userId).list()
                .stream()
                .map(Follow::getUserId)
                .toList();
    }
}
