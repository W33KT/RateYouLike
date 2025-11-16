package com.hmdp.utils;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author tankaiwen
 */
@Data
@Accessors(chain = true)
public class RedisData {
    private LocalDateTime expireTime = LocalDateTime.MIN;
    private Object data;
}
