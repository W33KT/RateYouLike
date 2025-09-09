package com.hmdp.service;

import com.alibaba.fastjson2.JSON;
import com.hmdp.DO.ShopQueryDO;
import com.hmdp.constants.RedisConstants;
import com.hmdp.dao.IShopDAO;
import com.hmdp.dao.IShopTypeDAO;
import com.hmdp.dto.ShopDTO;
import com.hmdp.dto.ShopTypeDTO;
import com.hmdp.exception.BusinessException;
import com.hmdp.utils.ValidateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author tankaiwen
 */
@Service
@Slf4j
public class ShopService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private IShopDAO shopDAO;
    @Resource
    private IShopTypeDAO shopTypeDAO;

    public ShopDTO queryShopById(Long id) {
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
        List<ShopDTO> dbShopDTOList = ListUtils.emptyIfNull(shopDAO.queryShop(new ShopQueryDO().setId(id)))
                .stream()
                .map(ShopDTO::convertFromShop)
                .toList();
        if (CollectionUtils.isEmpty(dbShopDTOList)) {
            throw new BusinessException("shop not exist");
        }

        // 5. save shop info to redis
        stringRedisTemplate.opsForValue().set(redisKey, JSON.toJSONString(dbShopDTOList.get(0)), RedisConstants.CACHE_SHOP_TTL, TimeUnit.MINUTES);

        // 6. return shop info
        return dbShopDTOList.get(0);
    }

    public List<ShopTypeDTO> listShopTypes() {
        // 1. query shop type list from redis
        String redisKey = RedisConstants.CACHE_SHOP_TYPE_KEY;
        String shopTypeJson = stringRedisTemplate.opsForValue().get(redisKey);

        // 2. judge whether shop type list exits in redis, if yes then return
        if (StringUtils.isNotEmpty(shopTypeJson)) {
            return JSON.parseArray(shopTypeJson, ShopTypeDTO.class);
        }

        // 3. if no, query shop type list from database
        List<ShopTypeDTO> shopTypeDTOList = shopTypeDAO.list().stream().map(ShopTypeDTO::convertFromShopType).toList();
        if (CollectionUtils.isEmpty(shopTypeDTOList)) {
            throw new BusinessException("shop type not exist");
        }

        // 4. save shop type list to redis
        stringRedisTemplate.opsForValue().set(redisKey, JSON.toJSONString(shopTypeDTOList), RedisConstants.CACHE_SHOP_TYPE_TTL, TimeUnit.HOURS);

        // 5. return shop type list
        return shopTypeDTOList;
    }

}
