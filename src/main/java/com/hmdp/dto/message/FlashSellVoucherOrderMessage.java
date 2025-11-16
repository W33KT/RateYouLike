package com.hmdp.dto.message;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author tankaiwen
 */
@Data
@Accessors(chain = true)
public class FlashSellVoucherOrderMessage {
    private Long userId;

    private Long voucherId;

    private Long orderId;
}
