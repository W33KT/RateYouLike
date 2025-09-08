package com.hmdp.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.DO.ShopQueryDO;
import com.hmdp.constants.RedisConstants;
import com.hmdp.dto.ShopDTO;
import com.hmdp.entity.Shop;
import com.hmdp.exception.BusinessException;
import com.hmdp.mapper.ShopMapper;
import com.hmdp.service.IShopService;
import com.hmdp.utils.ValidateUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author tankaiwen
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public ShopDTO queryShopByIdRedis(Long id) {
        // 1. validate query param
        ValidateUtils.notNull(id, "Shop id is null!");

        // 2. query shop list from redis
        String redisKey = RedisConstants.CACHE_SHOP_KEY + id;
        String shopJson = stringRedisTemplate.opsForValue().get(redisKey);

        // 3. judge whether shop info exits in redis, if yes then return
        if (StringUtils.isNotEmpty(shopJson)) {
            return JSON.parseObject(shopJson, ShopDTO.class);
        }

        // 4. if no, query shop info from database
        List<ShopDTO> dbShopDTOList = queryShopFromDateBase(new ShopQueryDO().setId(id));
        if (CollectionUtils.isEmpty(dbShopDTOList)) {
            throw new BusinessException("shop not exist");
        }

        // 5. save shop info to redis
        stringRedisTemplate.opsForValue().set(redisKey, JSON.toJSONString(dbShopDTOList.get(0)));

        // 6. return shop info
        return dbShopDTOList.get(0);
    }

    @Override
    public List<ShopDTO> queryShopFromDateBase(ShopQueryDO queryDO) {
        ValidateUtils.notNull(queryDO, "Shop query param is null!");

        LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
        // dynamic criteria
        if (Objects.nonNull(queryDO.getId())) {
            queryWrapper.eq(Shop::getId, queryDO.getId());
        }
        if (Objects.nonNull(queryDO.getTypeId())) {
            queryWrapper.eq(Shop::getTypeId, queryDO.getTypeId());
        }
        if (StringUtils.isNotBlank(queryDO.getName())) {
            queryWrapper.like(Shop::getName, queryDO.getName());
        }
        // page param
        if (Objects.nonNull(queryDO.getPage()) && queryDO.getPage().getCurrent() > 0 && queryDO.getPage().getSize() > 0) {
            return ListUtils.emptyIfNull(this.page(queryDO.getPage(), queryWrapper).getRecords())
                    .stream()
                    .map(ShopDTO::convertFromShop)
                    .collect(Collectors.toList());
        }

        return ListUtils.emptyIfNull(this.list(queryWrapper))
                .stream()
                .map(ShopDTO::convertFromShop)
                .collect(Collectors.toList());
    }
}
