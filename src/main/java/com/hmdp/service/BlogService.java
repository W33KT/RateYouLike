package com.hmdp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmdp.constants.RedisConstants;
import com.hmdp.constants.SystemConstants;
import com.hmdp.dao.BlogDAO;
import com.hmdp.dao.UserDAO;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.Blog;
import com.hmdp.entity.User;
import com.hmdp.exception.BusinessException;
import com.hmdp.utils.RedisService;
import com.hmdp.utils.UserHolder;
import com.hmdp.utils.ValidateUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author tankaiwen
 */
@Service
public class BlogService {
    @Resource
    private BlogDAO blogDAO;
    @Resource
    private UserDAO userDAO;
    @Resource
    private RedisService redisService;

    public List<Blog> queryHotBlog(Integer offset) {
        ValidateUtils.notNull(offset, "offset is null!");
        ValidateUtils.isTrue(offset >= 0, "offset is less than 0!");

        // page query blogs order by number of likes
        Page<Blog> page = blogDAO.query()
                .orderByDesc("liked")
                .page(new Page<>(offset, SystemConstants.MAX_PAGE_SIZE));
        List<Blog> records = page.getRecords();

        // query user data (avatar & name)
        records.forEach(this::fillUserData);

        return records;
    }

    public Blog queryBlogById(Long id) {
        ValidateUtils.notNull(id, "id is null!");
        // query blog by id
        Blog blog = blogDAO.getById(id);
        if (Objects.isNull( blog)) {
            throw new BusinessException("blog not found!");
        }

        // query user data (avatar & name)
        fillUserData( blog);

        return blog;
    }

    private void fillUserData(Blog blog) {
        User user = userDAO.getById(blog.getUserId());
        if (Objects.isNull(user)) {
            return;
        }
        blog.setName(user.getNickName());
        blog.setIcon(user.getIcon());

        UserDTO currentUserDTO = UserHolder.getUser();
        if (Objects.isNull(currentUserDTO)) {
            return;
        }
        Boolean isLike = redisService.existInSortedSet(
                RedisConstants.BLOG_LIKED_KEY + blog.getId(),
                String.valueOf(currentUserDTO.getId()));

        blog.setIsLike(BooleanUtils.isTrue(isLike));
    }

    public void likeBlog(Long id) {
        ValidateUtils.notNull(id, "id is null!");
        String key = RedisConstants.BLOG_LIKED_KEY + id;
        Long userId = UserHolder.getUser().getId();

        if (redisService.existInSortedSet(key, userId.toString())) {
            boolean lines = blogDAO.update()
                    .setSql("liked = liked - 1")
                    .eq("id", id)
                    .update();
            if (lines) {
                redisService.removeFromSortedSet(key, userId.toString());
            }
            return;
        }

        boolean lines = blogDAO.update()
                .setSql("liked = liked + 1")
                .eq("id", id)
                .update();
        if (lines) {
            redisService.addToSortedSet(key, userId.toString(), System.currentTimeMillis());
        }
    }

    public List<UserDTO> queryBlogLikes(Long id) {
        String key = RedisConstants.BLOG_LIKED_KEY + id;

        Set<String> top5LikeUsers = redisService.queryFromSortedSet(key, 0, 4);
        if (Objects.isNull(top5LikeUsers) || CollectionUtils.isEmpty(top5LikeUsers)) {
            return Collections.emptyList();
        }

        List<Long> userIds = top5LikeUsers.stream().map(Long::valueOf).toList();
        String ids = StringUtils.join(",", userIds);

        List<User> users = userDAO.query()
                .in("id", userIds)
                .last("order by field(id," +ids + ")")
                .list();

        return ListUtils.emptyIfNull(users).stream()
                .map(UserDTO::convertFromUser)
                .toList();
    }
}
