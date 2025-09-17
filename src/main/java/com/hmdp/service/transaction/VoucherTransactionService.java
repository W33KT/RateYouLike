package com.hmdp.service.transaction;

import com.hmdp.VoucherTypeEnum;
import com.hmdp.dao.SeckillVoucherDAO;
import com.hmdp.dao.VoucherDAO;
import com.hmdp.dto.SeckillVoucherDTO;
import com.hmdp.dto.VoucherDTO;
import com.hmdp.entity.Voucher;
import com.hmdp.utils.ValidateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author tankaiwen
 */
@Service
public class VoucherTransactionService {
    @Resource
    private SeckillVoucherDAO seckillVoucherDAO;
    @Resource
    private VoucherDAO voucherDAO;

    @Transactional(rollbackFor = Exception.class)
    public Long addSeckillVoucherTransaction(SeckillVoucherDTO seckillVoucherDTO, VoucherDTO voucherDTO) {
        validateVoucherInsertParams(voucherDTO);
        ValidateUtils.isTrue(VoucherTypeEnum.SECKILL.getCode().equals(voucherDTO.getType()), "only flash-sell voucher supported!");
        validateSeckillVoucherInsertParams(seckillVoucherDTO);

        Voucher voucher = voucherDTO.convertToDB();
        voucherDAO.save(voucher);

        seckillVoucherDTO.setVoucherId(voucher.getId());
        seckillVoucherDAO.save(seckillVoucherDTO.convertToDB());

        return voucher.getId();
    }

    private void validateVoucherInsertParams(VoucherDTO voucherDTO) {
        ValidateUtils.notNull(voucherDTO, "voucher info is null");
        ValidateUtils.notNull(voucherDTO.getTitle(), "voucher title is null");
        ValidateUtils.notNull(voucherDTO.getPayValue(), "voucher price is null");
        ValidateUtils.notNull(voucherDTO.getActualValue(), "voucher value is null");
    }

    private void validateSeckillVoucherInsertParams(SeckillVoucherDTO seckillVoucherDTO) {
        ValidateUtils.notNull(seckillVoucherDTO, "flash-sell voucher info is null");
        ValidateUtils.notNull(seckillVoucherDTO.getStock(), "seckill voucher stock is null");
    }
}
