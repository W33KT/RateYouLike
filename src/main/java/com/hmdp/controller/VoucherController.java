package com.hmdp.controller;


import com.hmdp.dao.VoucherDAO;
import com.hmdp.dto.Result;
import com.hmdp.facade.VoucherFacade;
import com.hmdp.vo.request.SeckillVoucherAddReqVO;
import com.hmdp.vo.request.VoucherAddReqVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@RestController
@RequestMapping("/voucher")
public class VoucherController {

    @Resource
    private VoucherDAO voucherDAO;
    @Resource
    private VoucherFacade voucherFacade;

    /**
     * add voucher
     * @param reqVO voucher info
     * @return voucher id
     */
    @PostMapping
    public Result addVoucher(@RequestBody VoucherAddReqVO reqVO) {
        return voucherFacade.addVoucher(reqVO);
    }

    /**
     * add flash-sell voucher
     * @param reqVO voucher info and flash-sell info
     * @return voucher id
     */
    @PostMapping("seckill")
    public Result addSeckillVoucher(@RequestBody SeckillVoucherAddReqVO reqVO) {
        return voucherFacade.addSeckillVoucher(reqVO);
    }

    /**
     * query voucher list of a shop
     * @param shopId shop id
     * @return voucher info list
     */
    @GetMapping("/list/{shopId}")
    public Result queryVoucherOfShop(@PathVariable("shopId") Long shopId) {
       return voucherFacade.queryVoucherOfShop(shopId);
    }
}
