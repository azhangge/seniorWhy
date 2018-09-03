package com.nd.auxo.recommend.core.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * REDIS缓存名称常量
 *
 * @author wbh
 * @version latest
 * @date 2016/08/12
 */
public class RedisCacheNames {
    //KV键值系统配置
    public static final String KV_SYSTEM_LIST = "kv:system:list:tmp1";
    /**
     * 缓存过期时间配置
     *
     * @return Map：key=cacheName，value=seconds
     */
    public static Map<String, Long> getExpires() {
        Map<String, Long> expires = new ConcurrentHashMap<String, Long>();
        long expire = 86400L;
        expires.put(KV_SYSTEM_LIST, expire);
        return expires;
    }
}
