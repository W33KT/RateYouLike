package com.hmdp.facade;

import com.hmdp.dto.Result;
import com.hmdp.dto.ShopDTO;
import com.hmdp.service.IShopService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tankaiwen
 */
@Service
public class ShopFacade {
    @Resource
    public IShopService shopService;

    public Result queryShopById(Long id) {
        ShopDTO shopDTO = shopService.queryShopByIdRedis(id);

        return Result.ok(shopDTO);
    }
}
