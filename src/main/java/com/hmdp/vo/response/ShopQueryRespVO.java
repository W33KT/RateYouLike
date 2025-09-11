package com.hmdp.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author tankaiwen
 */
@Data
@Accessors(chain = true)
public class ShopQueryRespVO {
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
}
