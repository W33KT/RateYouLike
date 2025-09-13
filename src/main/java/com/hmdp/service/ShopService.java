package com.hmdp.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.hmdp.DO.ShopQueryDO;
import com.hmdp.aop.annotation.RedisLock;
import com.hmdp.constants.RedisConstants;
import com.hmdp.dao.ShopDAO;
import com.hmdp.dao.ShopTypeDAO;
import com.hmdp.dto.ShopDTO;
import com.hmdp.dto.ShopTypeDTO;
import com.hmdp.entity.Shop;
import com.hmdp.exception.BusinessException;
import com.hmdp.exception.SystemException;
import com.hmdp.utils.RedisData;
import com.hmdp.utils.RedisService;
import com.hmdp.utils.ThreadPoolHolder;
import com.hmdp.utils.ValidateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
    private ShopDAO shopDAO;
    @Resource
    private ShopTypeDAO shopTypeDAO;
    @Resource
    private RedisService redisService;

    public ShopDTO queryShopById(Long id) {
        // 1. validate query param
        ValidateUtils.notNull(id, "Shop id is null!");

        // 2. query shop list from redis
        String redisKey = RedisConstants.CACHE_SHOP_KEY + id;
        RedisData redisData = redisService.getData(redisKey);
        if (Objects.isNull(redisData)) {
            // 3. If not exist, return
            return null;
        }

        // 4. If existed, convert to shop info and expire time
        ShopDTO shopDTO = ((JSONObject) redisData.getData()).toJavaObject(ShopDTO.class);
        LocalDateTime expireTime = redisData.getExpireTime();

        if (LocalDateTime.now().isBefore(expireTime)) {
            // 5. if not expired, return
            return shopDTO;
        }

        // 6. if expired, refresh shop info in redis
        refreshShopInfoInRedis(id, RedisConstants.CACHE_SHOP_TTL);

        // 7. return expired shop info
        return shopDTO;

        /* TTL strategy
        // 3. judge whether shop info exits in redis, if yes then return
        if (StringUtils.isNotEmpty(shopJson)) {
            return JSON.parseObject(shopJson, ShopDTO.class);
        }

        // 4. if shopJson is empty string, notify client that shop info not exist directly
        if (StringUtils.equals("", shopJson)) {
            throw new BusinessException("shop not exist");
        }

        // 5. if no, query shop info from database
        List<ShopDTO> dbShopDTOList = ListUtils.emptyIfNull(shopDAO.queryShop(new ShopQueryDO().setId(id)))
                .stream()
                .map(ShopDTO::convertFromShop)
                .toList();

        // 6. if shop info doesn't exist in database, set empty string in redis, avoid cache avalanche by setting redis ttl with random number
        if (CollectionUtils.isEmpty(dbShopDTOList)) {
            stringRedisTemplate.opsForValue().set(redisKey, "",
                    RedisConstants.CACHE_NULL_TTL + RandomUtil.randomLong(0, 10), TimeUnit.MINUTES);
            throw new BusinessException("shop not exist");
        }

        // 7. save shop info to redis
        stringRedisTemplate.opsForValue().set(redisKey, JSON.toJSONString(dbShopDTOList.get(0)), RedisConstants.CACHE_SHOP_TTL, TimeUnit.MINUTES);

        // 8. return shop info
        return dbShopDTOList.get(0);*/
    }

    @RedisLock(key = RedisConstants.LOCK_SHOP_KEY, timeout = RedisConstants.LOCK_SHOP_TTL, unit = TimeUnit.SECONDS)
    public void refreshShopInfoInRedis(Long id, Long expireSec) {
        ThreadPoolHolder.CACHE_REFRESH_EXECUTOR.submit(() -> {
            ShopDTO shopDTO = ListUtils.emptyIfNull(shopDAO.queryShop(new ShopQueryDO().setId(id)))
                    .stream()
                    .map(ShopDTO::convertFromShop)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
            redisService.saveDataWithExpire(RedisConstants.CACHE_SHOP_KEY + id, shopDTO, expireSec);
        });
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

    @Transactional(rollbackFor = Exception.class)
    public Long saveNewShop(ShopDTO shopDTO) {
        try {
            // 1. validate param
            validateShopToInsert(shopDTO);

            // 2. convert to mybatis object
            Shop shop = shopDTO.convertToShopForInsert();

            // 3. insert shop info to database
            boolean res = shopDAO.save(shop);
            if (!res) {
                throw new SystemException("shop info DB insert failed");
            }

            // 4. return shop id
            return shop.getId();
        } catch (DuplicateKeyException e) {
            throw new BusinessException("Shop info exists!");
        }
    }

    private void validateShopToInsert(ShopDTO shopDTO) {
        ValidateUtils.notNull(shopDTO, "shop is null");
        ValidateUtils.notBlank(shopDTO.getName(), "shop name is blank");
        ValidateUtils.notNull(shopDTO.getTypeId(), "shop type is null");
        ValidateUtils.notBlank(shopDTO.getImages(), "shop images is blank");
        ValidateUtils.notBlank(shopDTO.getAddress(), "shop address is blank");
        ValidateUtils.notNull(shopDTO.getX(), "shop longitude is null");
        ValidateUtils.notNull(shopDTO.getY(), "shop latitude is null");
        ValidateUtils.notNull(shopDTO.getAvgPrice(), "shop average price is null");
        if (Objects.isNull(shopDTO.getSold())) {
            shopDTO.setSold(0);
        }
        if (Objects.isNull(shopDTO.getComments())) {
            shopDTO.setComments(0);
        }
        if (Objects.isNull(shopDTO.getScore())) {
            shopDTO.setScore(0);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateShop(ShopDTO shopDTO) {
        // 1. validate update param
        ValidateUtils.notNull(shopDTO, "shop info is null");
        ValidateUtils.notNull(shopDTO.getId(), "shop id is null");

        // 2. update shop info in DB
        shopDAO.updateShop(shopDTO);

        // 3. delete old shop info in Redis
        stringRedisTemplate.delete(RedisConstants.CACHE_SHOP_KEY + shopDTO.getId());
    }
}
