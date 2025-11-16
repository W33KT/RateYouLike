package com.hmdp.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author tankaiwen
 */
@Data
@Accessors(chain = true)
public class VoucherAddReqVO {
    @Schema(description = "shop id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long shopId;

    @Schema(description = "title", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "sub title", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String subTitle;

    @Schema(description = "rules", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String rules;

    @Schema(description = "voucher price", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long payValue;

    @Schema(description = "voucher value", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long actualValue;

    @Schema(description = "voucher type", requiredMode = Schema.RequiredMode.NOT_REQUIRED, defaultValue = "0")
    private Integer type = 0;

    @Schema(description = "voucher status", requiredMode = Schema.RequiredMode.NOT_REQUIRED, defaultValue = "1")
    private Integer status;
}
