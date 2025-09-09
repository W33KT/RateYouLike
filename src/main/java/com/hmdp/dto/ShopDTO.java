package com.hmdp.dto;

import com.hmdp.entity.Shop;
import com.hmdp.vo.response.ShopQueryRespVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author tankaiwen
 */
@Data
@Accessors(chain = true)
public class ShopDTO {
    private Long id;

    private String name;

    private Long typeId;

    private String images;

    private String area;

    private String address;

    private Double x;

    private Double y;

    private Long avgPrice;

    private Integer sold;

    private Integer comments;

    private Integer score;

    private String openHours;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Double distance;

    public static ShopDTO convertFromShop(Shop shop) {
        if (Objects.isNull(shop)) {
            return null;
        }

        return new ShopDTO()
                .setId(shop.getId())
                .setName(shop.getName())
                .setTypeId(shop.getTypeId())
                .setImages(shop.getImages())
                .setArea(shop.getArea())
                .setAddress(shop.getAddress())
                .setX(shop.getX())
                .setY(shop.getY())
                .setAvgPrice(shop.getAvgPrice())
                .setSold(shop.getSold())
                .setComments(shop.getComments())
                .setScore(shop.getScore())
                .setOpenHours(shop.getOpenHours())
                .setCreateTime(shop.getCreateTime())
                .setUpdateTime(shop.getUpdateTime())
                .setDistance(shop.getDistance());
    }

    public ShopQueryRespVO convertToVO() {
        return new ShopQueryRespVO()
                .setId(getId())
                .setName(getName())
                .setTypeId(getTypeId())
                .setImages(getImages())
                .setArea(getArea())
                .setAddress(getAddress())
                .setX(getX())
                .setY(getY())
                .setAvgPrice(getAvgPrice())
                .setSold(getSold())
                .setComments(getComments())
                .setScore(getScore())
                .setOpenHours(getOpenHours())
                .setCreateTime(getCreateTime())
                .setUpdateTime(getUpdateTime())
                .setDistance(getDistance());
    }
}
