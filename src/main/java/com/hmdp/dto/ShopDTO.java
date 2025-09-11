package com.hmdp.dto;

import com.hmdp.entity.Shop;
import com.hmdp.vo.request.ShopUpdateReqVO;
import com.hmdp.vo.response.ShopQueryRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "longitude")
    private Double x;

    @Schema(description = "latitude")
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
                .setUpdateTime(shop.getUpdateTime());
    }

    public static ShopDTO convertFromShopUpdateReqVO(ShopUpdateReqVO reqVO) {
        if (Objects.isNull(reqVO)) {
            return null;
        }

        return new ShopDTO()
                .setId(reqVO.getId())
                .setName(reqVO.getName())
                .setTypeId(reqVO.getTypeId())
                .setImages(reqVO.getImages())
                .setArea(reqVO.getArea())
                .setAddress(reqVO.getAddress())
                .setX(reqVO.getX())
                .setY(reqVO.getY())
                .setAvgPrice(reqVO.getAvgPrice())
                .setSold(reqVO.getSold())
                .setComments(reqVO.getComments())
                .setScore(reqVO.getScore())
                .setOpenHours(reqVO.getOpenHours());
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

    public Shop convertToShopForInsert() {
        return new Shop()
                .setName( getName())
                .setTypeId( getTypeId())
                .setImages( getImages())
                .setArea( getArea())
                .setAddress( getAddress())
                .setX( getX())
                .setY( getY())
                .setAvgPrice( getAvgPrice())
                .setSold( getSold())
                .setComments( getComments())
                .setScore( getScore())
                .setOpenHours( getOpenHours())
                .setCreateTime( LocalDateTime.now())
                .setUpdateTime( LocalDateTime.now());
    }
}
