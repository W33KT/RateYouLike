package com.hmdp.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dao.VoucherDAO;
import com.hmdp.dto.VoucherInfoDTO;
import com.hmdp.entity.Voucher;
import com.hmdp.mapper.VoucherMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tankaiwen
 */
@Service
public class VoucherDAOImpl extends ServiceImpl<VoucherMapper, Voucher> implements VoucherDAO {
    @Override
    public List<VoucherInfoDTO> queryVoucherOfShop(Long shopId) {
        // query voucher and flash-sell related info
        return getBaseMapper().queryVoucherOfShop(shopId);
    }
}
