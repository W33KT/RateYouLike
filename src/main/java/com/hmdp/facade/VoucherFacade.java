package com.hmdp.facade;

import com.hmdp.dao.VoucherDAO;
import com.hmdp.dto.Result;
import com.hmdp.dto.SeckillVoucherDTO;
import com.hmdp.dto.VoucherDTO;
import com.hmdp.dto.VoucherInfoDTO;
import com.hmdp.service.VoucherService;
import com.hmdp.utils.ValidateUtils;
import com.hmdp.vo.request.SeckillVoucherAddReqVO;
import com.hmdp.vo.request.VoucherAddReqVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tankaiwen
 */
@Service
public class VoucherFacade {
    @Resource
    private VoucherService voucherService;
    @Resource
    private VoucherDAO voucherDAO;

    public Result addVoucher(VoucherAddReqVO reqVO) {
        Long voucherId = voucherService.addVoucher(VoucherDTO.convertFromVoucherAddReqVO(reqVO));

        return Result.ok(voucherId);
    }

    public Result addSeckillVoucher(SeckillVoucherAddReqVO reqVO) {
        Long voucherId = voucherService.addSeckillVoucher(SeckillVoucherDTO.convertFromSeckillVoucherAddReqVO(reqVO), VoucherDTO.convertFromSeckillVoucherAddReqVO(reqVO));

        return Result.ok(voucherId);
    }

    public Result queryVoucherOfShop(Long shopId) {
        ValidateUtils.notNull(shopId, "shop id is null!");

        List<VoucherInfoDTO> infoList = voucherDAO.queryVoucherOfShop(shopId);

        return Result.ok(infoList);
    }
}
