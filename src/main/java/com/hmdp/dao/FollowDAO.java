package com.hmdp.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hmdp.entity.Follow;

import java.util.List;

/**
 * @author tankaiwen
 */
public interface FollowDAO extends IService<Follow> {
    List<Long> queryFansIds(Long userId);
}
