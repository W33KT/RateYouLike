package com.hmdp.dto;

import com.hmdp.entity.Voucher;
import com.hmdp.vo.request.SeckillVoucherAddReqVO;
import com.hmdp.vo.request.VoucherAddReqVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author tankaiwen
 */
@Data
@Accessors(chain = true)
public class VoucherDTO {
    private Long id;

    private Long shopId;

    private String title;

    private String subTitle;

    private String rules;

    private Long payValue;

    private Long actualValue;

    private Integer type;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public static VoucherDTO convertFromDB(Voucher voucher) {
        if (Objects.isNull(voucher)) {
            return null;
        }

        return new VoucherDTO()
                .setId(voucher.getId())
                .setShopId(voucher.getShopId())
                .setTitle(voucher.getTitle())
                .setSubTitle(voucher.getSubTitle())
                .setRules(voucher.getRules())
                .setPayValue(voucher.getPayValue())
                .setActualValue(voucher.getActualValue())
                .setType(voucher.getType())
                .setStatus(voucher.getStatus())
                .setCreateTime(voucher.getCreateTime())
                .setUpdateTime(voucher.getUpdateTime());
    }

    public static VoucherDTO convertFromVoucherAddReqVO(VoucherAddReqVO voucherAddReqVO) {
        if (Objects.isNull(voucherAddReqVO)) {
            return null;
        }

        return new VoucherDTO()
                .setShopId(voucherAddReqVO.getShopId())
                .setTitle(voucherAddReqVO.getTitle())
                .setSubTitle(voucherAddReqVO.getSubTitle())
                .setRules(voucherAddReqVO.getRules())
                .setPayValue(voucherAddReqVO.getPayValue())
                .setActualValue(voucherAddReqVO.getActualValue())
                .setType(voucherAddReqVO.getType())
                .setStatus(voucherAddReqVO.getStatus())
                .setCreateTime(LocalDateTime.now())
                .setUpdateTime(LocalDateTime.now());
    }

    public static VoucherDTO convertFromSeckillVoucherAddReqVO(SeckillVoucherAddReqVO seckillVoucherAddReqVO) {
        if (Objects.isNull(seckillVoucherAddReqVO)) {
            return null;
        }

        return new VoucherDTO()
                .setShopId(seckillVoucherAddReqVO.getShopId())
                .setTitle(seckillVoucherAddReqVO.getTitle())
                .setSubTitle(seckillVoucherAddReqVO.getSubTitle())
                .setRules(seckillVoucherAddReqVO.getRules())
                .setPayValue(seckillVoucherAddReqVO.getPayValue())
                .setActualValue(seckillVoucherAddReqVO.getActualValue())
                .setType(seckillVoucherAddReqVO.getType())
                .setStatus(seckillVoucherAddReqVO.getStatus())
                .setCreateTime(LocalDateTime.now())
                .setUpdateTime(LocalDateTime.now());
    }

    public Voucher convertToDB() {
        return new Voucher()
                .setId(this.getId())
                .setShopId(this.getShopId())
                .setTitle(this.getTitle())
                .setSubTitle(this.getSubTitle())
                .setRules(this.getRules())
                .setPayValue(this.getPayValue())
                .setActualValue(this.getActualValue())
                .setType(this.getType())
                .setStatus(this.getStatus())
                .setCreateTime(this.getCreateTime())
                .setUpdateTime(this.getUpdateTime());
    }
}
