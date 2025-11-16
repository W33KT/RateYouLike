package com.hmdp.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmdp.constants.SystemConstants;
import com.hmdp.dao.BlogDAO;
import com.hmdp.dao.UserDAO;
import com.hmdp.dto.Result;
import com.hmdp.dto.ScrollResult;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.Blog;
import com.hmdp.facade.BlogFacade;
import com.hmdp.utils.UserHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tankaiwen
 */
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Resource
    private BlogDAO blogDAO;
    @Resource
    private UserDAO userDAO;
    @Resource
    private BlogFacade blogFacade;

    @PostMapping
    public Result saveBlog(@RequestBody Blog blog) {
        return blogFacade.saveBlog(blog);
    }

    @PutMapping("/like/{id}")
    public Result likeBlog(@PathVariable("id") Long id) {
        return blogFacade.likeBlog(id);
    }

    @GetMapping("/of/me")
    public Result queryMyBlog(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        // 获取登录用户
        UserDTO user = UserHolder.getUser();
        // 根据用户查询
        Page<Blog> page = blogDAO.query()
                .eq("user_id", user.getId()).page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        // 获取当前页数据
        List<Blog> records = page.getRecords();
        return Result.ok(records);
    }

    @GetMapping("/hot")
    public Result queryHotBlog(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        return blogFacade.queryHotBlog( current);
    }

    @GetMapping("/{id}")
    public Result queryBlogById(@PathVariable("id") Long id) {
        return blogFacade.queryBlogById(id);
    }

    @PutMapping("/likes/{id}")
    public Result queryBlogLikes(@PathVariable("id") Long id) {
        return blogFacade.queryBlogLikes(id);
    }

    @GetMapping("of/follow")
    public ScrollResult queryBlogOfFollow(@RequestParam("max") Long max,
                                          @RequestParam(value = "offset", defaultValue = "0") Integer offset) {
        return blogFacade.queryBlogOfFollow(max, offset);
    }
}
