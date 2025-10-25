package com.hmdp.constants;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;

/**
 * @author tankaiwen
 */
public class LuaConstants {
    @Schema(description = "Lua script for safe unlock: only delete key if value matches")
    public static final String UNLOCK_LUA =
            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                    "   return redis.call('del', KEYS[1]) " +
                    "else " +
                    "   return 0 " +
                    "end";

    public static final DefaultRedisScript<Long> FLASH_SELL_SCRIPT = new DefaultRedisScript<>();
    static {
        FLASH_SELL_SCRIPT.setLocation(new ClassPathResource("lua/flash_sell.lua"));
        FLASH_SELL_SCRIPT.setResultType(Long.class);
    }
}
