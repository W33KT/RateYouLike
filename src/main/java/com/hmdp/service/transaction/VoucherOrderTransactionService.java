package com.hmdp.service.transaction;

import com.hmdp.dao.SeckillVoucherDAO;
import com.hmdp.dao.VoucherOrderDAO;
import com.hmdp.entity.VoucherOrder;
import com.hmdp.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author tankaiwen
 */
@Service
public class VoucherOrderTransactionService {
    @Resource
    private VoucherOrderDAO voucherOrderDAO;
    @Resource
    private SeckillVoucherDAO seckillVoucherDAO;

    @Transactional(rollbackFor = Exception.class)
    public void createVoucherOrder(Long voucherId, Long userId, Long orderId) {
        // 1. decrease flash-sell voucher stock
        boolean success = seckillVoucherDAO.update()
                .setSql("stock = stock - 1")
                .eq("voucher_id", voucherId)
                .gt("stock", 0)
                .update();
        if (!success) {
            // flash-sell voucher stock decrease failed
            throw new BusinessException("Flash-sell voucher claim unsuccessful. Please try refreshing.");
        }

        // 2. create flash-sell voucher order
        VoucherOrder voucherOrder = new VoucherOrder()
                .setId(orderId)
                .setUserId(userId)
                .setVoucherId(voucherId);
        voucherOrderDAO.save(voucherOrder);
    }
}
