package com.hmdp.enums;

/**
 * @author tankaiwen
 */
public enum VoucherTypeEnum {
    // normal voucher
    NORMAL(0, "normal voucher"),
    // flash-sell voucher
    SECKILL(1, "flash-sell voucher");
    private Integer code;
    private String desc;
    VoucherTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static VoucherTypeEnum getByCode(Integer code) {
        for (VoucherTypeEnum value : VoucherTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
