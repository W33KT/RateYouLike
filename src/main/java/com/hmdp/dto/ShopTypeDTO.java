package com.hmdp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hmdp.entity.ShopType;
import com.hmdp.vo.response.ShopTypeQueryRespVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author tankaiwen
 */
@Data
@Accessors(chain = true)
public class ShopTypeDTO {
    private Long id;

    private String name;

    private String icon;

    private Integer sort;

    @JsonIgnore
    private LocalDateTime createTime;

    @JsonIgnore
    private LocalDateTime updateTime;

    public static ShopTypeDTO convertFromShopType(ShopType shopType) {
        return new ShopTypeDTO()
                .setId(shopType.getId())
                .setName(shopType.getName())
                .setIcon(shopType.getIcon())
                .setSort(shopType.getSort())
                .setCreateTime(shopType.getCreateTime())
                .setUpdateTime(shopType.getUpdateTime());
    }

    public ShopTypeQueryRespVO convertToVO() {
        return new ShopTypeQueryRespVO()
                .setId(getId())
                .setName(getName())
                .setIcon(getIcon())
                .setSort(getSort());
    }
}
