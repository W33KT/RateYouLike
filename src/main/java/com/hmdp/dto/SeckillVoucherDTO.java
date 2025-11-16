package com.hmdp.dto;

import com.hmdp.entity.SeckillVoucher;
import com.hmdp.vo.request.SeckillVoucherAddReqVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author tankaiwen
 */
@Data
@Accessors(chain = true)
public class SeckillVoucherDTO {
    @Schema(description = "voucher id")
    private Long voucherId;

    private Integer stock;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public static SeckillVoucherDTO convertFromDB(SeckillVoucher seckillVoucher) {
        if (Objects.isNull(seckillVoucher)) {
            return null;
        }

        return new SeckillVoucherDTO()
                .setVoucherId(seckillVoucher.getVoucherId())
                .setStock(seckillVoucher.getStock())
                .setBeginTime(seckillVoucher.getBeginTime())
                .setEndTime(seckillVoucher.getEndTime())
                .setCreateTime(seckillVoucher.getCreateTime())
                .setUpdateTime(seckillVoucher.getUpdateTime());
    }

    public static SeckillVoucherDTO convertFromSeckillVoucherAddReqVO(SeckillVoucherAddReqVO reqVO) {
        if (Objects.isNull(reqVO)) {
            return null;
        }

        return new SeckillVoucherDTO()
                .setStock(reqVO.getStock())
                .setBeginTime(reqVO.getBeginTime())
                .setEndTime(reqVO.getEndTime())
                .setCreateTime(LocalDateTime.now())
                .setUpdateTime(LocalDateTime.now());
    }

    public SeckillVoucher convertToDB() {
        return new SeckillVoucher()
                .setVoucherId(this.getVoucherId())
                .setStock(this.getStock())
                .setBeginTime(this.getBeginTime())
                .setEndTime(this.getEndTime())
                .setCreateTime(this.getCreateTime())
                .setUpdateTime(this.getUpdateTime());
    }
}
