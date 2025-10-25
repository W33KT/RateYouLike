package com.hmdp.service;

import com.alibaba.fastjson2.JSON;
import com.hmdp.aop.annotation.RedisLock;
import com.hmdp.constants.LuaConstants;
import com.hmdp.constants.RedisConstants;
import com.hmdp.dao.SeckillVoucherDAO;
import com.hmdp.dao.VoucherOrderDAO;
import com.hmdp.dto.message.FlashSellVoucherOrderMessage;
import com.hmdp.enums.FlashSellLuaRespEnum;
import com.hmdp.exception.BusinessException;
import com.hmdp.service.transaction.VoucherOrderTransactionService;
import com.hmdp.utils.IdGenerateService;
import com.hmdp.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author tankaiwen
 */
@Service
public class VoucherOrderService {
    @Resource
    private VoucherOrderDAO voucherOrderDAO;
    @Resource
    private SeckillVoucherDAO seckillVoucherDAO;
    @Resource
    private VoucherOrderTransactionService voucherOrderTransactionService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;
    @Resource
    private IdGenerateService idGenerateService;

    /**
     * claim flash-sell voucher (each user can only claim one voucher)
     * create flash-sell voucher order
     * @param voucherId flash-sell voucher id
     * @return flash-sell voucher order id
     */
    @Transactional(rollbackFor = Exception.class)
    public Long seckillVoucher(Long voucherId) {
        Long userId = UserHolder.getUser().getId();

        Long execute = stringRedisTemplate.execute(LuaConstants.FLASH_SELL_SCRIPT,
                Collections.emptyList(), voucherId.toString(), userId.toString());
        if (FlashSellLuaRespEnum.SUCCESS != FlashSellLuaRespEnum.getByCode(execute.intValue())) {
            throw new BusinessException(FlashSellLuaRespEnum.getByCode(execute.intValue()).getDesc());
        }

        Long orderId = idGenerateService.generateId(RedisConstants.ORDER_ID_GENERATE_KEY);

        FlashSellVoucherOrderMessage message = new FlashSellVoucherOrderMessage()
                .setUserId(userId)
                .setVoucherId(voucherId)
                .setOrderId(orderId);
        kafkaTemplate.send("seckill_order", JSON.toJSONString(message));

        return orderId;
    }

    @RedisLock(
            key = RedisConstants.LOCK_ORDER_KEY + " + #userId",
            timeout = RedisConstants.LOCK_ORDER_TTL,
            unit = TimeUnit.SECONDS
    )
    public void createVoucherOrder(Long voucherId, Long userId, Long orderId) {
        voucherOrderTransactionService.createVoucherOrder(voucherId, userId, orderId);
    }
}
