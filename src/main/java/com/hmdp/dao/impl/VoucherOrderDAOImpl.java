package com.hmdp.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.entity.VoucherOrder;
import com.hmdp.mapper.VoucherOrderMapper;
import com.hmdp.dao.VoucherOrderDAO;
import org.springframework.stereotype.Service;

/**
 * @author tankaiwen
 */
@Service
public class VoucherOrderDAOImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements VoucherOrderDAO {

}
