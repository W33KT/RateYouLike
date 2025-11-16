package com.hmdp.controller.consumer;

import com.alibaba.fastjson2.JSON;
import com.hmdp.dto.message.FlashSellVoucherOrderMessage;
import com.hmdp.service.VoucherOrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tankaiwen
 */
@Service
public class FlashSellVoucherConsumer {
    @Resource
    private VoucherOrderService voucherOrderService;

    @KafkaListener(topics = "seckill_order",
            groupId = "voucher-order-group")
    public void handle(String msg) {
        FlashSellVoucherOrderMessage message = JSON.parseObject(msg, FlashSellVoucherOrderMessage.class);
        Long userId = message.getUserId();
        Long voucherId = message.getVoucherId();
        Long orderId = message.getOrderId();

        voucherOrderService.createVoucherOrder(voucherId, userId, orderId);
    }
}
