package com.hmdp.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.entity.BlogComments;
import com.hmdp.mapper.BlogCommentsMapper;
import com.hmdp.dao.IBlogCommentsDAO;
import org.springframework.stereotype.Service;

/**
 * @author tankaiwen
 */
@Service
public class BlogCommentsDAOImpl extends ServiceImpl<BlogCommentsMapper, BlogComments> implements IBlogCommentsDAO {

}
