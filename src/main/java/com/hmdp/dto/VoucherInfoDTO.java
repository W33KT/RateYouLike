package com.hmdp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author tankaiwen
 */
@Data
@Accessors(chain = true)
@Schema(description = "voucher info, including basic info and flash-sell info")
public class VoucherInfoDTO {
    @Schema(description = "voucher id")
    private Long id;

    @Schema(description = "shop id")
    private Long shopId;

    @Schema(description = "voucher title")
    private String title;

    @Schema(description = "voucher sub title")
    private String subTitle;

    @Schema(description = "rules when using voucher")
    private String rules;

    @Schema(description = "voucher price")
    private Long payValue;

    @Schema(description = "voucher value")
    private Long actualValue;

    @Schema(description = "voucher type, 0: normal voucher, 1: flash-sell voucher")
    private Integer type;

    @Schema(description = "voucher status, 1. available, 2. removed, 3. expired")
    private Integer status;

    @Schema(description = "voucher stock")
    private Integer stock;

    @Schema(description = "voucher flash-sell begin time")
    private LocalDateTime beginTime;

    @Schema(description = "voucher flash-sell end time")
    private LocalDateTime endTime;

    @Schema(description = "voucher create time")
    private LocalDateTime createTime;

    @Schema(description = "voucher update time")
    private LocalDateTime updateTime;
}
