package com.hmdp.service;

import com.hmdp.aop.annotation.RedisLock;
import com.hmdp.constants.GlobalConstants;
import com.hmdp.constants.RedisConstants;
import com.hmdp.dao.SeckillVoucherDAO;
import com.hmdp.dao.VoucherOrderDAO;
import com.hmdp.entity.SeckillVoucher;
import com.hmdp.exception.BusinessException;
import com.hmdp.service.transaction.VoucherOrderTransactionService;
import com.hmdp.utils.UserHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Objects;
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

    /**
     * claim flash-sell voucher (each user can only claim one voucher)
     * create flash-sell voucher order
     * @param voucherId flash-sell voucher id
     * @return flash-sell voucher order id
     */
    @Transactional(rollbackFor = Exception.class)
    public Long seckillVoucher(Long voucherId) {
        // 1. query flash-sell voucher info
        SeckillVoucher seckillVoucher = seckillVoucherDAO.getById(voucherId);
        if (Objects.isNull(seckillVoucher)) {
            // flash-sell voucher does not exist
            throw new BusinessException("This flash-sell voucher does not exist!");
        }

        // 2. judge flash-sell voucher sale has started
        if (Objects.nonNull(seckillVoucher.getBeginTime()) && seckillVoucher.getBeginTime().isAfter(LocalDateTime.now())) {
            // flash-sell voucher sale has not started
            throw new BusinessException("This flash-sell voucher has not been available for sale!");
        }

        // 3. judge flash-sell voucher sale has ended
        if (Objects.nonNull(seckillVoucher.getEndTime()) && seckillVoucher.getEndTime().isBefore(LocalDateTime.now())) {
            // flash-sell voucher sale has ended
            throw new BusinessException("This flash-sell voucher sale has ended!");
        }

        // 4. judge flash-sell voucher stock
        if (Objects.nonNull(seckillVoucher.getStock()) && GlobalConstants.ONE_INTEGER.compareTo(seckillVoucher.getStock()) > 0) {
            // flash-sell voucher stock is not enough
            throw new BusinessException("This flash-sell voucher stock is not enough!");
        }

        // 5. judge user has claimed flash-sell voucher
        Long userId = UserHolder.getUser().getId();
        Long count = voucherOrderDAO.query()
                .eq("user_id", userId)
                .eq("voucher_id", voucherId)
                .count();
        if (Objects.nonNull(count) && GlobalConstants.ONE_LONG.compareTo(count) <= 0) {
            // user has claimed flash-sell voucher
            throw new BusinessException("You have claimed this flash-sell voucher!");
        }

        // 6. create flash-sell voucher order
        return createVoucherOrder(voucherId, userId);
    }

    @RedisLock(
            key = RedisConstants.LOCK_ORDER_KEY + " + #userId",
            timeout = RedisConstants.LOCK_ORDER_TTL,
            unit = TimeUnit.SECONDS
    )
    public Long createVoucherOrder(Long voucherId, Long userId) {
        return voucherOrderTransactionService.createVoucherOrder(voucherId, userId);
    }
}
