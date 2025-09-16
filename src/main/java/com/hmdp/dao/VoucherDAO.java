package com.hmdp.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hmdp.dto.VoucherInfoDTO;
import com.hmdp.entity.Voucher;

import java.util.List;

/**
 * @author tankaiwen
 */
public interface VoucherDAO extends IService<Voucher> {
    List<VoucherInfoDTO> queryVoucherOfShop(Long shopId);
}
