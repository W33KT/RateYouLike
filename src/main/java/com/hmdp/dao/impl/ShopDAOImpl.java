package com.hmdp.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.DO.ShopQueryDO;
import com.hmdp.dao.IShopDAO;
import com.hmdp.entity.Shop;
import com.hmdp.mapper.ShopMapper;
import com.hmdp.utils.ValidateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author tankaiwen
 */
@Service
public class ShopDAOImpl extends ServiceImpl<ShopMapper, Shop> implements IShopDAO {
    @Override
    public List<Shop> queryShop(ShopQueryDO queryDO) {
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
            return this.page(queryDO.getPage(), queryWrapper).getRecords();
        }

        return this.list(queryWrapper);
    }
}
