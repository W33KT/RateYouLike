package com.hmdp.enums;

import lombok.Getter;

/**
 * @author tankaiwen
 */
public enum FlashSellLuaRespEnum {
    // -1: unknown failed, 0: success, 1: out of stock, 2: duplicate order for current user
    UNKNOWN_FAILED(-1, "Unknown failed!"),
    SUCCESS(0, "Success"),
    NOT_START(1, "Out of stock!"),
    NOT_END(2, "Duplicate order!");

    @Getter
    private final Integer code;
    @Getter
    private final String desc;

    FlashSellLuaRespEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static FlashSellLuaRespEnum getByCode(Integer code) {
        for (FlashSellLuaRespEnum value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return UNKNOWN_FAILED;
    }
}
