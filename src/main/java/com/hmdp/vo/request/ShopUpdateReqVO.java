package com.hmdp.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author tankaiwen
 */
@Data
@Accessors(chain = true)
public class ShopUpdateReqVO {
    @Schema(description = "shop id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long id;

    @Schema(description = "shop name", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "shop type id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long typeId;

    @Schema(description = "shop images", requiredMode = Schema.RequiredMode.REQUIRED)
    private String images;

    @Schema(description = "shop area", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String area;

    @Schema(description = "shop address", requiredMode = Schema.RequiredMode.REQUIRED)
    private String address;

    @Schema(description = "longitude", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double x;

    @Schema(description = "latitude", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double y;

    @Schema(description = "open hours", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long avgPrice;

    @Schema(description = "sold", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer sold;

    @Schema(description = "comments", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer comments;

    @Schema(description = "score", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer score;

    @Schema(description = "open hours", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String openHours;


}
