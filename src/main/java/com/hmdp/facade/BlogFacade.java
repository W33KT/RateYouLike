package com.hmdp.facade;

import com.hmdp.dto.Result;
import com.hmdp.service.BlogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tankaiwen
 */
@Service
public class BlogFacade {
    @Resource
    private BlogService blogService;
    public Result queryHotBlog(Integer offset) {
        return Result.ok(blogService.queryHotBlog(offset));
    }

    public Result queryBlogById(Long id) {
        return Result.ok(blogService.queryBlogById(id));
    }

    public Result likeBlog(Long id) {
        blogService.likeBlog(id);
        return Result.ok();
    }
}
