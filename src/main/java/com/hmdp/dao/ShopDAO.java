package com.hmdp.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hmdp.DO.ShopQueryDO;
import com.hmdp.dto.ShopDTO;
import com.hmdp.entity.Shop;

import java.util.List;

/**
 * @author tankaiwen
 */
public interface ShopDAO extends IService<Shop> {
    List<Shop> queryShop(ShopQueryDO queryDO);
    void updateShop(ShopDTO shopDTO);
    List<ShopDTO> pageQueryShopByType(Integer typeId, Integer offset, Integer size);
}
