package com.hmdp.facade;

import com.hmdp.dto.Result;
import com.hmdp.service.VoucherOrderService;
import com.hmdp.utils.ValidateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tankaiwen
 */
@Service
public class VoucherOrderFacade {
    @Resource
    private VoucherOrderService voucherOrderService;

    public Result seckillVoucher(Long voucherId) {
        ValidateUtils.notNull(voucherId, "voucher id is null!");

        Long orderId = voucherOrderService.seckillVoucher(voucherId);

        return Result.ok(orderId);
    }
}
