package com.hmdp.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.entity.UserInfo;
import com.hmdp.mapper.UserInfoMapper;
import com.hmdp.dao.IUserInfoDAO;
import org.springframework.stereotype.Service;

/**
 * @author tankaiwen
 */
@Service
public class UserInfoDAOImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoDAO {

}
