package com.hmdp.service;

import com.hmdp.enums.VoucherTypeEnum;
import com.hmdp.dao.SeckillVoucherDAO;
import com.hmdp.dao.VoucherDAO;
import com.hmdp.dto.SeckillVoucherDTO;
import com.hmdp.dto.VoucherDTO;
import com.hmdp.entity.Voucher;
import com.hmdp.exception.SystemException;
import com.hmdp.service.transaction.VoucherTransactionService;
import com.hmdp.utils.ValidateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tankaiwen
 */
@Service
@Slf4j
public class VoucherService {
    @Resource
    private SeckillVoucherDAO seckillVoucherDAO;
    @Resource
    private VoucherDAO voucherDAO;
    @Resource
    private VoucherTransactionService voucherTransactionService;


    /**
     * add normal voucher
     * @param voucherDTO voucher info to insert
     * @return voucher id
     */
    public Long addVoucher(VoucherDTO voucherDTO) {
        try {
            // 1. validate insert params
            validateVoucherInsertParams(voucherDTO);
            ValidateUtils.isTrue(VoucherTypeEnum.NORMAL.getCode().equals(voucherDTO.getType()), "only normal voucher supported!");

            // 2. add voucher
            Voucher voucher = voucherDTO.convertToDB();
            voucherDAO.save(voucher);

            // 3. return voucher id
            return voucher.getId();
        } catch (DuplicateKeyException e) {;
            throw new SystemException("Voucher has been added, please refresh to check!", e);
        }
    }

    private void validateVoucherInsertParams(VoucherDTO voucherDTO) {
        ValidateUtils.notNull(voucherDTO, "voucher info is null");
        ValidateUtils.notNull(voucherDTO.getTitle(), "voucher title is null");
        ValidateUtils.notNull(voucherDTO.getPayValue(), "voucher price is null");
        ValidateUtils.notNull(voucherDTO.getActualValue(), "voucher value is null");
    }

    /**
     * add flash-sell voucher
     * @param seckillVoucherDTO flash-sell related info
     * @param voucherDTO voucher info
     */
    public Long addSeckillVoucher(SeckillVoucherDTO seckillVoucherDTO, VoucherDTO voucherDTO) {
        Long res;
        try {
            res = voucherTransactionService.addSeckillVoucherTransaction(seckillVoucherDTO, voucherDTO);
        } catch (Exception e) {
            throw new SystemException("Add flash-sell voucher failed, please refresh to check!", e);
        }
        return res;
    }
}
