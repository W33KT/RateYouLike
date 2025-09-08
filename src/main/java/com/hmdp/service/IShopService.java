package com.hmdp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hmdp.DO.ShopQueryDO;
import com.hmdp.dto.ShopDTO;
import com.hmdp.entity.Shop;

import java.util.List;

/**
 * @author tankaiwen
 */
public interface IShopService extends IService<Shop> {
    ShopDTO queryShopByIdRedis(Long id);
    List<ShopDTO> queryShopFromDateBase(ShopQueryDO queryDO);
}
