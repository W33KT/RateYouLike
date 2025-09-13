package com.hmdp.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dao.UserDAO;
import com.hmdp.entity.User;
import com.hmdp.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * @author tankaiwen
 */
@Service
@Slf4j
public class UserDAOImpl extends ServiceImpl<UserMapper, User> implements UserDAO {

}

