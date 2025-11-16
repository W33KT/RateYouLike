package com.hmdp.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.DO.ShopQueryDO;
import com.hmdp.constants.SystemConstants;
import com.hmdp.dao.ShopDAO;
import com.hmdp.dto.ShopDTO;
import com.hmdp.entity.Shop;
import com.hmdp.exception.SystemException;
import com.hmdp.mapper.ShopMapper;
import com.hmdp.utils.ValidateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author tankaiwen
 */
@Service
public class ShopDAOImpl extends ServiceImpl<ShopMapper, Shop> implements ShopDAO {
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

    @Override
    public void updateShop(ShopDTO shopDTO) {
        ValidateUtils.notNull(shopDTO, "shop info is null");
        ValidateUtils.notNull(shopDTO.getId(), "shop id is null");

        UpdateWrapper<Shop> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", shopDTO.getId());
        if (StringUtils.isNotBlank(shopDTO.getName())) {
            wrapper.set("name", shopDTO.getName());
        }
        if (Objects.nonNull(shopDTO.getTypeId())) {
            wrapper.set("type_id", shopDTO.getTypeId());
        }
        if (StringUtils.isNotBlank(shopDTO.getImages())) {
            wrapper.set("images", shopDTO.getImages());
        }
        // area could be set as empty string
        if (Objects.nonNull(shopDTO.getArea())) {
            wrapper.set("area", shopDTO.getArea());
        }
        if (StringUtils.isNotBlank(shopDTO.getAddress())) {
            wrapper.set("address", shopDTO.getAddress());
        }
        if (Objects.nonNull(shopDTO.getX())) {
            wrapper.set("x", shopDTO.getX());
        }
        if (Objects.nonNull(shopDTO.getY())) {
            wrapper.set("y", shopDTO.getY());
        }
        if (Objects.nonNull(shopDTO.getAvgPrice())) {
            wrapper.set("avg_price", shopDTO.getAvgPrice());
        }
        if (Objects.nonNull(shopDTO.getSold())) {
            wrapper.set("sold", shopDTO.getSold());
        }
        if (Objects.nonNull(shopDTO.getComments())) {
            wrapper.set("comments", shopDTO.getComments());
        }
        if (Objects.nonNull(shopDTO.getScore())) {
            wrapper.set("score", shopDTO.getScore());
        }
        // open_hours could be set as empty string
        if (Objects.nonNull(shopDTO.getOpenHours())) {
            wrapper.set("open_hours", shopDTO.getOpenHours());
        }
        wrapper.set("update_time", Objects.isNull(shopDTO.getUpdateTime()) ? LocalDateTime.now() : shopDTO.getUpdateTime());

        boolean result = update(wrapper);
        if ( !result) {
            throw new SystemException("Update shop info failed, please refresh this web page and retry!");
        }
    }

    @Override
    public List<ShopDTO> pageQueryShopByType(Integer typeId, Integer current, Integer size) {
        ValidateUtils.notNull(typeId, "typeId is null");
        ValidateUtils.notNull(current, "current is null");
        size = Objects.isNull(size) ? SystemConstants.DEFAULT_PAGE_SIZE : size;

        Page<Shop> page = query()
                .eq("type_id", typeId)
                .page(new Page<>(current, size));

        return page.getRecords().stream().map(ShopDTO::convertFromShop).toList();
    }
}
