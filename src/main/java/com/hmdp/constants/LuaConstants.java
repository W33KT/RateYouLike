package com.hmdp.constants;

import io.swagger.v3.oas.annotations.media.Schema;

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
}
