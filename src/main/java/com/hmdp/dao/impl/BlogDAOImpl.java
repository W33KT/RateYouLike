package com.hmdp.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.entity.Blog;
import com.hmdp.mapper.BlogMapper;
import com.hmdp.dao.BlogDAO;
import org.springframework.stereotype.Service;

/**
 * @author tankaiwen
 */
@Service
public class BlogDAOImpl extends ServiceImpl<BlogMapper, Blog> implements BlogDAO {

}
