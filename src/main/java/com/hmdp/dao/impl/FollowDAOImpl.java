package com.hmdp.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.entity.Follow;
import com.hmdp.mapper.FollowMapper;
import com.hmdp.dao.IFollowDAO;
import org.springframework.stereotype.Service;

/**
 * @author tankaiwen
 */
@Service
public class FollowDAOImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowDAO {

}
