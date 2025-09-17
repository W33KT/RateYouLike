package com.hmdp.service.transaction;

import com.hmdp.constants.RedisConstants;
import com.hmdp.dao.SeckillVoucherDAO;
import com.hmdp.dao.VoucherOrderDAO;
import com.hmdp.entity.VoucherOrder;
import com.hmdp.exception.BusinessException;
import com.hmdp.utils.IdGenerateService;
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
    @Resource
    private IdGenerateService idGenerateService;

    @Transactional(rollbackFor = Exception.class)
    public Long createVoucherOrder(Long voucherId, Long userId) {
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
                .setId(idGenerateService.generateId(RedisConstants.ORDER_ID_GENERATE_KEY))
                .setUserId(userId)
                .setVoucherId(voucherId);
        voucherOrderDAO.save(voucherOrder);

        // 3. return flash-sell voucher order id
        return voucherOrder.getId();
    }
}
