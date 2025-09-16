package com.hmdp.constants;

public class RedisConstants {
    public static final String LOGIN_CODE_KEY = "login:code:";
    public static final long LOGIN_CODE_TTL = 2L;
    public static final String LOGIN_USER_KEY = "login:token:";
    public static final long LOGIN_USER_TTL = 30L; // minutes

    public static final long CACHE_NULL_TTL = 2L;

    public static final long CACHE_SHOP_TTL = 30L; // minutes
    public static final String CACHE_SHOP_KEY = "cache:shop:";

    public static final long CACHE_SHOP_TYPE_TTL = 24L; // hours
    public static final String CACHE_SHOP_TYPE_KEY = "cache:shop:type:";

    public static final String LOCK_SHOP_KEY = "lock:shop:";
    public static final long LOCK_SHOP_TTL = 10L; // seconds

    public static final String SECKILL_STOCK_KEY = "seckill:stock:";
    public static final String BLOG_LIKED_KEY = "blog:liked:";
    public static final String FEED_KEY = "feed:";
    public static final String SHOP_GEO_KEY = "shop:geo:";
    public static final String USER_SIGN_KEY = "sign:";

    public static final long GLOBAL_KEY_EXPIRE_DAYS = 2L;
}
